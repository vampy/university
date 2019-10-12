#!/usr/bin/python3
import argparse
import os
import sys

from src.util import Util
from src.step2.program import Program, ProgramRegex
from src.LR0Parser import LR0Parser
from src.grammar import Grammar
from src.log import Log

import re

if __name__ == '__main__':
    grammar = Grammar.from_lines(Util.get_lines_filename('src/step2/grammar.rg'))
    print(str(grammar))

    program = Program()
    program.scan('src/step2/code.ry')

    # print(program.get_pif(), '\n')
    print(program)

    # program_regex = ProgramRegex()
    # program_regex.scan('src/step2/code.ry')

    parser = LR0Parser(grammar, program)
    config = parser.parse(program.get_pif())
    if config is not None:
        print("Sequence: accepted!")
        print(program.get_code())
        print("Output stack: ", config.output_stack)
        parser.print_productions_list()
        Log.success("ACCEPT")

    # parser = argparse.ArgumentParser()
    # parser.add_argument('file', help="The source program file")
    # args = parser.parse_args()
    # if os.path.exists(args.file):
    #     Log.info('Scanning file {0}\n'.format(args.file))
    #     program = Program()
    #     program.scan(args.file)
    #     print(program)
    # else:
    #     Log.error('File "{0}" does not exist.'.format(args.file))
