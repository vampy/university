#!/usr/bin/python
import os
from log import Log
from enum import IntEnum, unique
from grammar import Grammar
from automaton import FiniteAutomaton


@unique
class Command(IntEnum):
    GRAMMAR_READ = 1
    GRAMMAR_DISPLAY = 2
    GRAMMAR_VERIFY = 3

    AUTOMATON_READ = 4
    AUTOMATON_DISPLAY = 5

    CONVERT_RG_TO_FA = 6
    CONVERT_FA_TO_RG = 7

    HELP = 99
    QUIT = 0


class Gui:
    @staticmethod
    def run():
        Log.info('Running...')

        try:
            grammar = Grammar.from_lines(Gui.get_lines_filename('grammar.rg'))
            print(grammar, grammar.is_regular(), grammar.is_left, grammar.is_right, end='\n' * 2)
            print(grammar.to_finite_automaton(), end='\n' * 2)
        except Exception as e:
            Log.error(grammar.error_message)
            Log.error(str(e))

        try:
            automaton = FiniteAutomaton.from_lines(Gui.get_lines_filename('automata.fa'))
            print(automaton, end='\n' * 2)
            print(automaton.to_regular_grammar())
        except Exception as e:
            Log.error(str(e))

        Gui.print_help_menu()
        grammar, automaton = None, None
        while True:
            try:
                command = Command(Gui.get_int('>>> '))
                if command is Command.QUIT:
                    print('\n\nQuitting...')
                    break

                elif command is Command.HELP:
                    Gui.print_help_menu()

                elif command is Command.GRAMMAR_READ:
                    filename = Gui.get_string('Filename = ')
                    grammar = Grammar.from_lines(Gui.get_lines_filename(filename))
                    Log.success('Success')

                elif command is Command.GRAMMAR_DISPLAY or command is Command.CONVERT_RG_TO_FA:
                    if grammar is None:
                        raise Exception('Please read a RG')

                    if command is Command.GRAMMAR_DISPLAY:
                        print(grammar)
                    else:
                        print(grammar.to_finite_automaton())
                    Log.success('Success')

                elif command is Command.GRAMMAR_VERIFY:
                    if grammar is None:
                        raise Exception('Please read a RG')

                    is_regular = grammar.is_regular()
                    if is_regular:
                        Log.success('Grammar is {0} regular'.format('left' if grammar.is_left else 'right'))
                    else:
                        Log.error('Grammar is NOT regular')

                elif command is Command.AUTOMATON_READ:
                    filename = Gui.get_string('Filename = ')
                    automaton = FiniteAutomaton.from_lines(Gui.get_lines_filename(filename))
                    Log.success('Success')

                elif command is Command.AUTOMATON_DISPLAY or command is Command.CONVERT_FA_TO_RG:
                    if automaton is None:
                        raise Exception('Please read a FA')

                    if command is Command.AUTOMATON_DISPLAY:
                        print(automaton)
                    else:
                        print(automaton.to_regular_grammar())
                    Log.success('Success')

                else:
                    print(command)
            except Exception as e:
                Log.error(str(e))

    @staticmethod
    def get_lines_filename(filename):
        if not os.path.exists(filename):
            raise FileExistsError('The file "{0}" does not exist'.format(filename))

        with open(filename, 'r') as f:
            lines = f.readlines()

        return lines

    @staticmethod
    def print_help_menu():
        print('{0}. Read grammar'.format(Command.GRAMMAR_READ))
        print('{0}. Display grammar'.format(Command.GRAMMAR_DISPLAY))
        print('{0}. Verify grammar'.format(Command.GRAMMAR_VERIFY), end='\n' * 2)

        print('{0}. Read FA'.format(Command.AUTOMATON_READ))
        print('{0}. Display FA'.format(Command.AUTOMATON_DISPLAY), end='\n' * 2)

        print('{0}. Convert RG to FA'.format(Command.CONVERT_RG_TO_FA))
        print('{0}. Convert RG to RG'.format(Command.CONVERT_FA_TO_RG), end='\n' * 2)

        print('{0}. Help menu'.format(Command.HELP))
        print('{0}. Quit'.format(Command.QUIT), end='\n' * 2)

    @staticmethod
    def get_int(prompt, prompt_retry='Retry again..', is_retry=False):
        if is_retry:
            print(prompt_retry)

        try:
            return int(Gui.get_string(prompt))
        except ValueError:
            return Gui.get_int(prompt, prompt_retry, True)

    @staticmethod
    def get_string(prompt):

        try:
            # Do not allow empty input
            user_input = input(prompt)
            if not user_input:
                return Gui.get_string(prompt)

            return user_input
        except EOFError:  # Ctrl-D
            return Command.QUIT
        except KeyboardInterrupt:  # Ctrl-C
            return Command.QUIT
