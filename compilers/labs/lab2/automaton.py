import re
from collections import defaultdict

import grammar
from util import Util


class FiniteAutomaton:
    PATTERN_TRANSITION = re.compile(r'δ\((\w+),\s*(\w+)\)\s*=\s*(\w+)')

    def __init__(self, states=set(), input_symbols=set(), transitions=defaultdict(list), start_state=None,
                 final_states=set()):

        # Q
        self.states = states

        # Σ
        self.input_symbols = input_symbols

        # δ : Q × Σ → Q
        self.transitions = transitions

        # q0 ∈ Q
        self.start_state = start_state

        # F ⊆ Q
        self.final_states = final_states

    def validate(self):
        if not self.states:
            raise Exception('FA does not contain any states')

        if not self.input_symbols:
            raise Exception('FA does not contain any input symbols')

        if not self.transitions:
            raise Exception('FA does not contain any transitions')

        if self.start_state is None:
            raise Exception('FA does not contain start state')

        if not self.final_states:
            raise Exception('FA does not contain final states')

        if self.start_state not in self.transitions:
            raise Exception('FA does not have any transitions for the start state')

        if self.states.difference(self.transitions.keys()):
            raise Exception('FA does not have transitions for all the states')

        if self.start_state not in self.states:
            raise Exception('FA does not have the start state belong to Q')

        if not self.final_states.issubset(self.states):
            raise Exception('FA final states are not a subset of Q')

    def to_regular_grammar(self):
        self.validate()

        # Initialise known
        non_terminals = self.states.copy()
        terminals = self.input_symbols.copy()
        start_symbol = self.start_state
        productions = defaultdict(list)

        # Initial state is also final state
        if start_symbol in self.final_states:
            productions[start_symbol].append(None)

        # Iterate over transitions
        for from_state in self.transitions:
            for value, to_state in self.transitions[from_state]:
                # goes to final state
                if to_state in self.final_states:
                    productions[from_state].append(value)

                # normal state, right linear
                productions[from_state].append(value + to_state)

        g = grammar.Grammar(start_symbol=start_symbol, terminals=terminals, non_terminals=non_terminals,
                            productions=productions)
        g.validate()

        return g

    def __str__(self):
        return 'Finite automaton:\n Q = {0},\n Σ = {1},\n δ = {2},\n q0 = {3},\n F = {4}' \
            .format(self.states, self.input_symbols, {key: self.transitions[key] for key in self.transitions},
                    self.start_state, self.final_states)

    @staticmethod
    def from_lines(lines):
        states, input_symbols, transitions, start_state, final_states = set(), set(), defaultdict(list), None, set()

        # Read lines
        for i, line in enumerate(lines):
            line_stripped = line.strip()

            # Start state is on the first line
            if i == 0:
                if Util.is_non_terminal(line_stripped):
                    start_state = line_stripped
                    states.add(start_state)
                    continue

                raise Exception('No valid start state found on first line')

            # Final states
            if i == 1:
                # final states are separated by space
                for s in line_stripped.split():
                    if Util.is_non_terminal(s):
                        final_states.add(s)
                        states.add(s)
                        continue

                    raise Exception('Found a non-valid final state on second line')
                continue

            # Ignore empty line
            if not line_stripped:
                continue

            # find transition on line δ(from_state, value) = to_state
            found = re.search(FiniteAutomaton.PATTERN_TRANSITION, line_stripped)
            if not found or len(found.groups()) != 3:
                raise Exception('Transition is not valid on line {0}'.format(i + 1))
            from_state, value, to_state = found.group(1), found.group(2), found.group(3)
            # print(from_state, value, to_state)

            # validate names
            if not Util.is_non_terminal(from_state):
                raise Exception('Transition from_state is not valid on line {0}'.format(i + 1))
            if not Util.is_terminal(value):
                raise Exception('Transition value is not valid on line {0}'.format(i + 1))
            if not Util.is_non_terminal(to_state):
                raise Exception('Transition to_state is not valid on line {0}'.format(i + 1))

            # Add to FA
            states.add(from_state)
            states.add(to_state)
            input_symbols.add(value)
            transitions[from_state].append((value, to_state))

        fa = FiniteAutomaton(states=states, input_symbols=input_symbols, transitions=transitions,
                             start_state=start_state, final_states=final_states)
        fa.validate()

        return fa
