#!/usr/bin/python3
import os
import argparse
import re
import sys
from collections import namedtuple

from table import HashTable
from log import Log
from tokens import Token, TokenStrings

MAX_LENGTH = 8
PIFTuple = namedtuple('PIFTuple', ['token', 'pos'])


class Program:
    def __init__(self, program_internal_form=None, tokens_found=None, line_nr=0, line="", line_pos=0, quotes_index=0,
                 table_identifiers=HashTable(), table_constants=HashTable()):
        self.pif = [] if program_internal_form is None else program_internal_form
        self.tokens_found = set() if tokens_found is None else tokens_found
        self.line_nr = line_nr
        self.line = line
        self.line_len = len(line)
        self.line_pos = line_pos
        self.table_identifiers = table_identifiers
        self.table_constants = table_constants
        self.quotes_index = quotes_index

    def __str__(self, *args, **kwargs):
        return 'Program: line_nr = {0}, line_len = {2}, line_pos = {3}, quotes_index = {4}, line = <<<{1}>>>'.format(
            self.line_nr, self.line, self.line_len, self.line_pos, self.quotes_index)

    def classify_token(self, string, string_original):
        if not string or not string.strip():
            return

        # Classify token
        ###########################
        # Found a token not an identifier or constant
        token = TokenStrings.get_token_for_string(string)
        if token is not None:
            self.pif.append(PIFTuple(token, -1))
            self.tokens_found.add(token)

            return

        # Found identifier
        elif TokenStrings.is_valid_identifier(string):
            if len(string) > MAX_LENGTH:
                Log.error('Lexical error: The identifier "{0}" is larger than {2} chars on line {1}'
                          .format(string, self.line_nr, MAX_LENGTH))
                sys.exit(1)

            self.pif.append(PIFTuple(Token.IDENTIFIER, self.table_identifiers.position(string)))
            return

        # Found numeric constant
        elif TokenStrings.is_valid_number_constant(string):
            self.pif.append(PIFTuple(Token.CONSTANT, self.table_constants.position(string)))
            return

        # Found char/string constant
        else:
            found_single = string.find("'")
            found_double = string.find('"')

            if found_single != -1 or found_double != -1:
                char_find = "'" if found_single != -1 else '"'

                ####################################################
                found = re.findall(TokenStrings.PATTERN_FIND_STRING, self.line)
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

                self.pif.append(PIFTuple(Token.CONSTANT, self.table_constants.position(found_search)))
                return end_position

        # Try to split token, continue and do it recursively
        token_split = re.findall(TokenStrings.PATTERN_FIND_TOKEN, string)
        assert len(token_split) <= 1

        # Can be "a=b" or "4," or "a=", etc..
        # where 'a' is left, '=' or ',' is sep and 'b' is right
        if token_split:
            left, sep, right = token_split[0]
            self.classify_token(left, string_original)
            self.classify_token(sep, string_original)
            self.classify_token(right, string_original)
        else:
            Log.error('Lexical error: "{0}" on line {1}'.format(string, self.line_nr))
            sys.exit(1)

    @staticmethod
    # a. length at most 8 characters
    # b. separate tables for identifiers, respectively constants
    # c. hashing table
    def main(filename):
        program = Program()

        # Read all file lines into a string
        with open(filename, 'r') as f:
            program_str = f.read()

        # Empty file
        if program_str is None:
            Log.error('File "{0}" does not contain any valid RY code.'.format(filename))
            return

        # Scan  program
        lines = program_str.split('\n')

        for nr, line in enumerate(lines):
            # read line
            program.line_nr += 1
            program.line = line
            program.line_len = len(line)
            program.tokens_found = set()
            program.line_pos = 0
            program.quotes_index = -1

            while program.line_pos < program.line_len:
                # Detect token
                line_char = line[program.line_pos]

                # Ignore white space
                if line_char == ' ':
                    program.line_pos += 1
                    continue

                # Split by whitespace, makes it easier to parse
                # Find next whitespace or end of line
                token_end = line.find(' ', program.line_pos)
                if token_end == -1:
                    token_end = program.line_len

                # keep original for when we have identifiers or literal strings
                token_string_original = line[program.line_pos:token_end]
                token_string = token_string_original.lower()

                # advance line_pos to surpass token
                program.line_pos = token_end

                # parse token separated by space
                value = Program.classify_token(program, token_string, token_string_original)
                if value is not None:
                    program.line_pos = value
                else:
                    program.line_pos += 1

            program.pif.append(PIFTuple(Token.NEWLINE, -1))

        print('Symbol table: ')
        print(str(program.table_identifiers), end='\n' * 2)
        print(str(program.table_constants), end='\n' * 2)
        # pprint(program_internal_form, indent=4)

        print('PIF: ')
        for i, form in enumerate(program.pif):
            if form.token is Token.NEWLINE:
                # print(str(form.token))
                print('')
                continue

            print('{0} {1:<25s} {2:>2}'.format(i, repr(form.token), form.pos))


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('file', help="The source program file")
    args = parser.parse_args()

    if os.path.exists(args.file):
        Log.info('Scanning file {0}\n'.format(args.file))
        Program.main(args.file)
    else:
        Log.error('File "{0}" does not exist.'.format(args.file))
