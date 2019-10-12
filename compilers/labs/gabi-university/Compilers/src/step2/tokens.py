import re
from enum import IntEnum, unique

from src.util import Util


@unique
class Token(IntEnum):
    IDENTIFIER = 0
    CONSTANT = 1

    ARRAY = 3
    INT = 4
    CHAR = 5
    FLOAT = 6

    WHILE = 10
    DO = 11
    BEGIN = 12
    END = 13
    IF = 14
    THEN = 15
    ELSE = 16

    INPUT = 20
    PRINT = 21

    NEWLINE = 25
    LEFT_SQB = 26        # [
    RIGHT_SQB = 27       # ]
    LEFT_PAR = 28        # (
    RIGHT_PAR = 29       # )
    LESS = 30            # <
    GREATER = 31         # >
    LESS_EQUAL = 32      # <=
    GREATER_EQUAL = 33   # >=
    EQ_EQUAL = 34        # ==
    NOT_EQUAL = 35       # !=
    STAR = 36            # *
    SLASH = 37           # /
    PLUS = 38            # +
    MINUS = 39           # -
    EQUAL = 40           # =
    COMMA = 41           # ,


class TokenStrings:
    KEYWORDS = {'int', 'float', 'char', 'array', 'while', 'do', 'begin', 'end', 'if', 'then', 'else', 'input', 'print'}
    OTHER_TOKENS = {'[', ']', '(', ')', '<', '>', '<=', '>=', '==', '!=', '*', '/', '+', '=', ','}

    _TOKENS_STRING_MAP = {
        'array': Token.ARRAY,
        'int': Token.INT,
        'char': Token.CHAR,
        'float': Token.FLOAT,

        'while': Token.WHILE,
        'do': Token.DO,
        'begin': Token.BEGIN,
        'end': Token.END,
        'if': Token.IF,
        'then': Token.THEN,
        'else': Token.ELSE,

        'input': Token.INPUT,
        'print': Token.PRINT,

        '\n': Token.NEWLINE,
        '[': Token.LEFT_SQB ,
        ']': Token.RIGHT_SQB,
        '(': Token.LEFT_PAR,
        ')': Token.RIGHT_PAR,
        '<': Token.LESS,
        '>': Token.GREATER,
        '<=': Token.LESS_EQUAL,
        '>=': Token.GREATER_EQUAL,
        '==': Token.EQ_EQUAL,
        '!=': Token.NOT_EQUAL,
        '*': Token.STAR,
        '/': Token.SLASH,
        '+': Token.PLUS,
        '-': Token.MINUS,
        '=': Token.EQUAL,
        ',': Token.COMMA
    }

    @staticmethod
    def get_token_for_string(string):
        """
        Get the mapping from a string literal to a token
        :param string:
        :return: the Token type or None if the string is not found
        """
        return TokenStrings._TOKENS_STRING_MAP.get(string)

    @staticmethod
    def is_valid_identifier(string):
        return string and string not in TokenStrings.KEYWORDS \
               and string.isalnum() and not string[0].isdigit()

    @staticmethod
    def is_valid_number_constant(string):
        if not string:
            return

        converted = Util.convert_to_int(string)
        if converted is None:
            converted = Util.convert_to_float(string)

        return converted is not None



