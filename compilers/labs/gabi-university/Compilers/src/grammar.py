from collections import defaultdict

from src.util import Util
from src.step2.tokens import TokenStrings

class Grammar:
    def __init__(self, non_terminals=set(), terminals=set(), productions=defaultdict(list), start_symbol=None):
        self.is_left, self.is_right = False, False
        self.error_message = ''

        # N
        self.non_terminals = non_terminals

        # Σ
        self.terminals = terminals

        # P
        # A dictionary with keys being a string that goes into a list of lists
        self.productions = productions

        # S
        self.start_symbol = start_symbol

    def get_productions(self):
        return self.productions.copy()

    def get_terminals(self):
        return self.terminals.copy()

    def get_non_terminals(self):
        return self.non_terminals.copy()

    def get_starting_symbol(self):
        return self.start_symbol

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

        difference = self.non_terminals.difference(self.productions.keys())
        if difference:
            raise Exception('RG does not have productions for all non terminals ' + str(difference))

    def __str__(self):
        productions_str = ''
        for key in self.productions.keys():
            if self.productions[key]:
                productions_str += "\t\t" + key + ' -> ' + \
                         " | ".join([" ".join(production) for production in self.productions[key]])
            productions_str += "\n"

        return 'Grammar G=(N, Σ, P, S):\n\t N = {0},\n\t Σ = {1},\n\t S = {3},\n\t P =\n{2}\n' \
            .format(", ".join(self.non_terminals),
                    ", ".join(self.terminals),
                    productions_str,
                    self.start_symbol)

    @staticmethod
    def from_lines(lines, is_space_sep=True):
        non_terminals, terminals, productions = set(), set(), defaultdict(list)
        assert len(lines) > 3

        # Start symbols is on first line
        line_start_symbol = lines[0].strip()
        assert line_start_symbol
        start_symbol = line_start_symbol

        # Non Terminals are on the second line
        line_non_terminals = lines[1].strip()
        assert line_non_terminals
        for el in line_non_terminals.split(','):
            non_terminals.add(el.strip())

        # Terminals are on the third line
        line_terminals = lines[2].strip()
        assert line_terminals
        for el in line_terminals.split(','):
            terminals.add(el.strip())

        # Read the rest of the lines
        for line in lines[3:]:
            line_stripped = line.strip()

            # Ignore empty line
            if not line_stripped or line_stripped[0] == "#":
                continue

            # Split the productions by '->'
            line_split = line_stripped.split('->')

            # Discard broken lines or empty
            if any(not t for t in line_split):
                continue

            assert len(line_split) == 2

            # eg: declarationList -> declaration | declaration 25 declarationList
            #         lhs                              rhs
            lhs, rhs = line_split[0].strip(), line_split[1].strip()
            if lhs not in non_terminals:
                raise Exception("lhs = '{0}' non in non_terminals".format(lhs))

            # Read right side, eg: declaration | declaration 25 declarationList
            for el in rhs.split('|'):
                # the key declarationList will looks something like this, in the productions.
                # declarationList: [['declaration'], ['declaration', '25', 'declarationList']]
                insert_el = []

                # by space
                if is_space_sep:
                    for symbol in el.strip().split():
                        symbol = symbol.strip()

                        # Ignore empty symbol
                        if not symbol:
                            continue

                        # Found empty string symbol
                        if Util.is_empty_string(symbol):
                            productions[lhs].append('eps')
                            continue

                        insert_el.append(symbol)
                else:
                    for symbol in el.strip():
                        insert_el.append(symbol)

                # Add production
                productions[lhs].append(insert_el)

        # Validate
        g = Grammar(start_symbol=start_symbol, terminals=terminals, non_terminals=non_terminals,
                    productions=productions)
        g.validate()

        return g
