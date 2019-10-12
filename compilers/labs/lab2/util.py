#!/usr/bin/python


class Util:
    # Representation in our productions will be None
    EMPTY_STRING = ['eps', 'epsilon', 'ε']

    @staticmethod
    def is_empty_string(string):
        string_lower = string.lower()
        return any([string_lower == e for e in Util.EMPTY_STRING])

    @staticmethod
    def is_non_terminal(char):
        return char.isupper() and char.isalpha()

    @staticmethod
    def is_terminal(char):
        return (char.islower() and char.isalpha()) or char.isdigit()
