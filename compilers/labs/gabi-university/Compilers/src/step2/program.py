#!/usr/bin/python3
import argparse
import collections
import os
import re
import sys
from collections import namedtuple

from src.log import Log
from .table import HashTable
from .tokens import Token, TokenStrings

MAX_LENGTH = 8
PIFTuple = namedtuple('PIFTuple', ['token', 'pos'])
TokenTuple = collections.namedtuple('TokenTuple', ['type', 'value', 'line', 'column'])


class ProgramRegex:
    token_specification = [
        ('NUMBER', r'\d+(\.\d*)?'),  # Integer or decimal number
        ('STRING', r'([\'"](?:[\w\d\s]?)+[\'"])'),
        ('ASSIGN', r'='),  # Assignment operator
        ('ID', r'[A-Za-z][A-Za-z0-9]*'),  # Identifiers
        ('OP', r'[+\-*/>\(\)]'),  # Arithmetic operators
        ('NEWLINE', r'\n'),  # Line endings
        ('SKIP', r'[ \t]+'),  # Skip over spaces and tabs
        ('MISMATCH', r'.'),  # Any other character
    ]

    def tokenize(self, code):
        tok_regex = '|'.join('(?P<%s>%s)' % pair for pair in self.token_specification)
        line_num = 1
        line_start = 0
        for mo in re.finditer(tok_regex, code):
            kind = mo.lastgroup
            value = mo.group(kind)

            def get_column():
                return mo.start() - line_start

            if kind == 'NEWLINE':
                # column for current newline
                column_newline = get_column()
                line_newline = line_num

                # Indicate start of next line
                line_start = mo.end()
                line_num += 1

                yield TokenTuple(Token.NEWLINE, None, line_newline, column_newline)
            elif kind == 'SKIP':
                pass
            elif kind == 'MISMATCH':
                raise RuntimeError('%r unexpected on line %d' % (value, line_num))
            elif kind == "NUMBER" or kind == "STRING":
                yield TokenTuple(Token.CONSTANT, value if kind == "STRING" else int(value), line_num, get_column())
            else:
                if kind == 'ID' and value.lower() in TokenStrings.KEYWORDS:
                    kind = value.lower()

                yield TokenTuple(kind, value, line_num, get_column())

    def scan(self, filename):
        # Read all file lines into a string
        with open(filename, 'r') as f:
            code = f.read()

        # Empty file
        if not code:
            Log.error('File "{0}" does not contain any valid RY code.'.format(filename))
            return

        for token in self.tokenize(code):
            print(token)


class Program:
    # This only works if there are not whitespace in the find
    PATTERN_FIND_TOKEN = re.compile(r"(.*)(\[|\]|\(|\)|<|>|<=|>=|==|!=|\*|/|\+|=|,)(.*)")
    PATTERN_FIND_STRING = re.compile(r'([\'"](?:[\w\d\s]?)+[\'"])', re.IGNORECASE)

    def __init__(self, program_internal_form=None, line_nr=0, line="", line_pos=0, quotes_index=0,
                 table_identifiers=HashTable(), table_constants=HashTable()):
        self.pif = [] if program_internal_form is None else program_internal_form

        self.code = ""
        self.lines = []

        # list of Token Tuples
        self.tokens = []

        self.line_nr = line_nr
        self.line = line
        self.line_len = len(line)
        self.line_pos = line_pos
        self.table_identifiers = table_identifiers
        self.table_constants = table_constants
        self.quotes_index = quotes_index

    def get_pif(self):
        return self.pif.copy()

    def get_tokens(self):
        return self.tokens

    def get_code(self):
        return self.code

    def get_line_nr(self, nr):
        return self.lines[nr - 1]

    def classify_token(self, string, string_original):
        if not string or not string.strip():
            return

        # Classify token
        ###########################
        # Found a token not an identifier or constant
        token = TokenStrings.get_token_for_string(string)
        if token is not None:
            self.tokens.append(TokenTuple(token, string_original, self.line_nr, self.line_pos))
            self.pif.append(PIFTuple(token, -1))

            return

        # Found identifier
        elif TokenStrings.is_valid_identifier(string):
            if len(string) > MAX_LENGTH:
                Log.error('Lexical error: The identifier "{0}" is larger than {2} chars on line {1}'
                          .format(string, self.line_nr, MAX_LENGTH))
                sys.exit(1)

            self.tokens.append(TokenTuple(Token.IDENTIFIER, string_original, self.line_nr, self.line_pos))
            self.pif.append(PIFTuple(Token.IDENTIFIER, self.table_identifiers.position(string)))
            return

        # Found numeric constant
        elif TokenStrings.is_valid_number_constant(string):
            self.tokens.append(TokenTuple(Token.CONSTANT, string_original, self.line_nr, self.line_pos))
            self.pif.append(PIFTuple(Token.CONSTANT, self.table_constants.position(string)))
            return

        # Found char/string constant
        else:
            found_single = string.find("'")
            found_double = string.find('"')

            if found_single != -1 or found_double != -1:
                char_find = "'" if found_single != -1 else '"'

                ####################################################
                found = re.findall(self.PATTERN_FIND_STRING, self.line)
                if not found:
                    Log.error('Lexical error: The quote {0} does not have a matching quote on line {1}'
                              .format(char_find, self.line_nr))
                    sys.exit(1)

                self.quotes_index += 1
                found_index = 0
                found_pos = 0
                found_search = ''

                # We do this so that we can have multiple char/string constants on the same line
                while found_index <= self.quotes_index:
                    # print('String index = {0}, found = {1}'.format(progam.quotes_index, found))
                    try:
                        found_search = found[self.quotes_index]
                    except IndexError:
                        Log.error('Lexical error: The quote {0} does not have a matching quote on line {1}'
                                  .format(char_find, self.line_nr))
                        sys.exit(1)

                    found_pos = self.line.find(found_search, found_pos + 1)
                    found_index += 1

                if char_find == "'" and len(found_search) > 3:
                    Log.error('Lexical error: The char literal  {0} on line {1} can not contain a string literal'
                              .format(found_search, self.line_nr))
                    sys.exit(1)

                # from the string start, surpass string
                end_position = found_pos + len(found_search)

                self.tokens.append(TokenTuple(Token.CONSTANT, string_original, self.line_nr, self.line_pos))
                self.pif.append(PIFTuple(Token.CONSTANT, self.table_constants.position(found_search)))
                return end_position

        # Try to split token, continue and do it recursively
        token_split = re.findall(self.PATTERN_FIND_TOKEN, string)
        assert len(token_split) <= 1

        # Can be "a=b" or "4," or "a=", etc..
        # where 'a' is left, '=' or ',' is sep and 'b' is right
        if token_split:
            left, sep, right = token_split[0]
            self.classify_token(left, left)
            self.classify_token(sep, sep)
            self.classify_token(right, right)
        else:
            Log.error('Lexical error: "{0}" on line {1}'.format(string, self.line_nr))
            sys.exit(1)

    # a. length at most 8 characters
    # b. separate tables for identifiers, respectively constants
    # c. hashing table
    def scan(self, filename):
        # Read all file lines into a string
        with open(filename, 'r') as f:
            program_str = f.read()

        # Empty file
        if program_str is None:
            Log.error('File "{0}" does not contain any valid RY code.'.format(filename))
            return

        # Create a copy
        self.code = program_str

        # Scan  program
        lines = program_str.split('\n')

        # TODO, fix when print(' ')
        for nr, line in enumerate(lines):
            # read line
            self.line_nr += 1
            self.line = line
            self.line_len = len(line)
            self.line_pos = 0
            self.quotes_index = -1

            # keep track of all lines
            self.lines.append(line)

            while self.line_pos < self.line_len:
                # Detect token
                line_char = line[self.line_pos]

                # Ignore white space
                if line_char == ' ':
                    self.line_pos += 1
                    continue

                # Split by whitespace, makes it easier to parse
                # Find next whitespace or end of line
                token_end = line.find(' ', self.line_pos)
                if token_end == -1:
                    token_end = self.line_len

                # keep original for when we have identifiers or literal strings
                token_string_original = line[self.line_pos:token_end]
                token_string = token_string_original.lower()

                # advance line_pos to surpass token
                self.line_pos = token_end

                # parse token separated by space
                value = self.classify_token(token_string, token_string_original)
                if value is not None:
                    self.line_pos = value
                else:
                    self.line_pos += 1

            self.tokens.append(TokenTuple(Token.NEWLINE, "", self.line_nr, self.line_pos))
            self.pif.append(PIFTuple(Token.NEWLINE, -1))

    def __repr__(self):
        return 'Program: line_nr = {0}, line_len = {2}, line_pos = {3}, quotes_index = {4}, line = <<<{1}>>>'.format(
                self.line_nr, self.line, self.line_len, self.line_pos, self.quotes_index)

    def __str__(self):
        return_str = "Symbol table: \n{0}\n\n{1}\n\n".format(str(self.table_identifiers), str(self.table_constants))
        # pprint(program_internal_form, indent=4)

        return_str += 'PIF: \n'
        for i, form in enumerate(self.pif):
            # if form.token is Token.NEWLINE:
                # print(str(form.token))
                # return_str += "\n"
                # continue

            return_str += "{0} {1:<25s} {2:>2}\n".format(i, repr(form.token), form.pos)

        return_str += '\nTokens: \n'
        for i, form in enumerate(self.tokens):
            # if form.type is Token.NEWLINE:
                # print(str(form.token))
                # return_str += "\n"
                # continue

            return_str += "{0} {1:<25s} {2:>2} {3:>10} {4:>3}\n".format(i, repr(form.type), form.value, form.line,
                                                                       form.column)

        return return_str


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('file', help="The source program file")
    args = parser.parse_args()

    if os.path.exists(args.file):
        Log.info('Scanning file {0}\n'.format(args.file))
        program = Program()
        program.scan(args.file)
        print(program)
    else:
        Log.error('File "{0}" does not exist.'.format(args.file))
