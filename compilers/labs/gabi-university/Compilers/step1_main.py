#!/usr/bin/python3

from src.grammar import Grammar
from src.LR0Parser import LR0Parser
from src.util import Util

FILE_PATH = "src/step1/"


def test_sequences():
    try:
        parser = LR0Parser(Grammar.from_lines(Util.get_lines_filename(FILE_PATH + "grLR0.txt"), False))
        assert parser.parse('aaaacc')
        assert not parser.parse('aaacd')
        assert not parser.parse('aaccddsdsds')

        parser = LR0Parser(Grammar.from_lines(Util.get_lines_filename(FILE_PATH + "grLR0(1).txt"), False))
        assert parser.parse('001223')
        assert not parser.parse('001225')
        assert not parser.parse('001223123121312')
    except FileNotFoundError:
        assert False


if __name__ == "__main__":
    # test_sequences()

    # 1st default file
    input_file = "grLR0.txt"
    sequence = "aaaacc"  # good
    sequence = "aaacd"     # bad
    sequence = "aaccddsdsds"  # bad

    # 2nd default file
    input_file = "grLR0(1).txt"
    sequence = "001223"  # good
    sequence = "001225"
    sequence = "001223123121312"

    input_file = input("Input file('1'/'2' for default files): ")
    if input_file == "1":
        input_file = "grLR0.txt"
    elif input_file == "2":
        input_file = "grLR0(1).txt"
    try:
        grammar = Grammar.from_lines(Util.get_lines_filename(FILE_PATH + input_file), False)
        parser = LR0Parser(grammar)
        sequence = input("Enter the sequence(q to exit): ")
        while sequence != "q":
            config = parser.parse(sequence)
            if config:
                print("Output stack: {0}".format(config.output_stack))

            sequence = input("Enter the sequence(q to exit): ")
    except FileNotFoundError:
        print('No such file or directory:', input_file)
