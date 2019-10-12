from collections import defaultdict

import automaton
from util import Util


class Grammar:
    def __init__(self, non_terminals=set(), terminals=set(), productions=defaultdict(list), start_symbol=None):
        self.is_left, self.is_right = False, False
        self.error_message = ''

        # N
        self.non_terminals = non_terminals

        # Σ
        self.terminals = terminals

        # P
        self.productions = productions

        # S
        self.start_symbol = start_symbol

    def is_regular(self):
        self.is_left, self.is_right = False, False

        # If epsilon on the starting symbol then S can not appear on the left side or right side of any production
        start_has_epsilon = any(p is None for p in self.productions[self.start_symbol])

        for symbol in self.productions:
            for production in self.productions[symbol]:
                # https://en.wikipedia.org/wiki/Regular_grammar

                # Check rule 3, None represents epsilon (empty string)
                if production is None:
                    # Epsilon can only appear on the starting symbol
                    if symbol != self.start_symbol:
                        self.is_left, self.is_right = False, False
                        self.error_message = 'Epsilon appears into a non starting symbol ({0})'.format(symbol)
                        return False

                    continue

                len_production = len(production)
                # Contains too many symbols
                if len_production > 2:
                    self.is_left, self.is_right = False, False
                    self.error_message = 'Non-Terminal goes into more than two symbols'
                    return False

                # Check rule 1
                first_is_terminal = Util.is_terminal(production[0])
                if len_production == 1:
                    if not first_is_terminal:
                        self.is_left, self.is_right = False, False
                        self.error_message = 'Non-Terminal goes into a non-terminal'
                        return False

                    continue

                # Check rule 2, decides if left or right linear
                second_is_terminal = Util.is_terminal(production[1])

                if first_is_terminal and not second_is_terminal and not self.is_left:
                    # Right regular

                    # Check epsilon case
                    if start_has_epsilon and production[1] == self.start_symbol:
                        self.is_left, self.is_right = False, False
                        self.error_message = 'Symbol {0} goes into the starting symbol, that has epsilon' \
                            .format(symbol)
                        return False

                    self.is_left, self.is_right = False, True
                elif not first_is_terminal and second_is_terminal and not self.is_right:
                    # Left regular

                    # Check epsilon case
                    if start_has_epsilon and production[0] == self.start_symbol:
                        self.is_left, self.is_right = False, False
                        self.error_message = 'Symbol {0} goes into the starting symbol, that has epsilon' \
                            .format(symbol)
                        return False

                    self.is_left, self.is_right = True, False
                else:
                    # Not regular language
                    self.is_left, self.is_right = False, False
                    self.error_message = 'Symbol {0} does not go into non-terminal|terminal or ' \
                                         'terminal|non-terminal'.format(symbol)
                    return False

                assert len_production == 2

        return self.is_left or self.is_right

    def validate(self):
        if self.start_symbol is None:
            raise Exception('No starting symbol is present')

        if not self.non_terminals:
            raise Exception('RG does not contain any non terminal symbols')

        if not self.terminals:
            raise Exception('RG does not contain any terminal symbols')

        if not self.productions:
            raise Exception('RG does not contain any productions')

        if self.start_symbol not in self.productions:
            raise Exception('RG does not have any productions for the start symbol')

        if self.non_terminals.difference(self.productions.keys()):
            raise Exception('RG does not have productions for all non terminals')

    def to_finite_automaton(self):
        self.validate()
        if not self.is_regular():
            raise Exception('Grammar is not regular, can not convert to FA')

        # Assume K is not a symbol
        final_symbol = 'K'
        if final_symbol in self.non_terminals:
            raise Exception('Symbol K is already in the non terminals')

        # Initialize states
        states = self.non_terminals.copy() | {final_symbol}
        input_symbols = self.terminals.copy()
        start_state = self.start_symbol
        final_states = {final_symbol}

        transitions = defaultdict(list)
        transitions[final_symbol] = []

        # Iterate over productions
        for from_symbol in self.productions:
            for production in self.productions[from_symbol]:
                # Epsilon found
                if production is None:
                    final_states.add(from_symbol)
                    continue

                first = production[0]
                if len(production) == 1:
                    # Add to final symbol
                    transitions[from_symbol].append((first, final_symbol))
                    continue

                # Two values in the production
                # Be careful if it is left or right regular
                second = production[1]
                if self.is_left:
                    first, second = second, first

                transitions[from_symbol].append((first, second))

        fa = automaton.FiniteAutomaton(states=states, input_symbols=input_symbols, transitions=transitions,
                                       start_state=start_state, final_states=final_states)
        fa.validate()

        return fa

    def __str__(self):
        return 'Grammar:\n N = {0},\n Σ = {1},\n P = {2},\n S = {3}\n' \
            .format(self.non_terminals, self.terminals, {key: self.productions[key] for key in self.productions},
                    self.start_symbol)

    @staticmethod
    def from_lines(lines):
        non_terminals, terminals, productions, start_symbol = set(), set(), defaultdict(list), None

        # Read lines
        for i, line in enumerate(lines):
            line_stripped = line.strip()

            # Start symbol is on first line
            if i == 0:
                if Util.is_non_terminal(line_stripped):
                    start_symbol = line_stripped
                    non_terminals.add(start_symbol)
                    continue

                raise Exception('No valid starting symbol on first line')

            # Ignore empty line
            if not line_stripped:
                continue

            # Split the productions by '->'
            line_split = line_stripped.split('->')

            # Discard broken lines or empty
            if any(not t for t in line_split):
                continue

            # eg: S => aA
            left_symbol = line_split[0].strip()
            if not Util.is_non_terminal(left_symbol):
                raise Exception('The left symbol = "{0}" is not valid non terminal'.format(left_symbol))
            non_terminals.add(left_symbol)

            # Read right side, eg: A => b | B, right side is 'b | B'
            right_side = line_split[1].strip().split('|')
            for symbol in right_side:
                symbol = symbol.strip()

                # Ignore empty symbol
                if not symbol:
                    continue

                # Found empty string symbol
                if Util.is_empty_string(symbol):
                    productions[left_symbol].append(None)
                    continue

                # Must be valid non terminals and terminals
                for char in symbol:
                    if Util.is_terminal(char):
                        terminals.add(char)
                    elif Util.is_non_terminal(char):
                        non_terminals.add(char)
                    else:
                        raise Exception('The char = "{0}" is not valid'.format(char))

                # Add production
                productions[left_symbol].append(symbol)

        # Validate
        g = Grammar(start_symbol=start_symbol, terminals=terminals, non_terminals=non_terminals,
                    productions=productions)
        g.validate()

        return g
