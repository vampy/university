from collections import namedtuple
from enum import IntEnum
from pprint import pprint

import src.step2.tokens as Tokens
from src.util import Util
from src.log import Log


class ActionValues(IntEnum):
    ACCEPT = -2
    SHIFT = -1


ConflictAction = namedtuple('ConflictAction', ['first', 'second'])
GOTOItem = namedtuple('GOTOItem', ['symbol', 'state_index'])
ParseTuple = namedtuple('ParsedTuple', ['working_stack', 'input_stack', 'output_stack'])
ProductionTuple = namedtuple('ProductionTuple', ['lhs', 'rhs'])


class TableConflictException(Exception):
    def __init__(self, value):
        self.value = value

    def __str__(self):
        return repr(self.value)


class ParsingException(Exception):
    def __init__(self, value):
        self.value = value

    def __str__(self):
        return repr(self.value)


class LR0Item:
    def __init__(self, lhs, before_dot, after_dot):
        """
        eg: S -> 1.A
        'S' is lhs
        ['1'] is before_dot
        ['A'] is after_dot

        :param lhs: string representing the symbol that this item goes from
        :param before_dot: list of all the symbols before the dot
        :param after_dot: list of all the symbols after the dot
        """
        self.lhs = lhs
        self.before_dot = before_dot
        self.after_dot = after_dot

    def __eq__(self, other):
        return self.lhs == other.lhs and self.before_dot == other.before_dot and self.after_dot == other.after_dot

    def __ne__(self, other):
        return not self.__eq__(other)

    def __str__(self):
        return "[" + self.lhs + " => " + ' '.join(self.before_dot) + '.' + ' '.join(self.after_dot) + "]"

    def __repr__(self):
        return str(self)


class LR0Parser:
    def __init__(self, grammar, lexer=None):
        self.lexer = lexer
        self.starting_symbol = grammar.get_starting_symbol()
        self.s_prime_symbol = self.starting_symbol + "'"

        self.terminals = grammar.get_terminals()
        self.non_terminals = grammar.get_non_terminals()
        self.terminals_and_non_terminals = self.terminals | self.non_terminals
        self.all_states = []
        self.table = []
        self.productions = grammar.get_productions()

        # Construct the canonical collection
        self.canonical_col()

        # Normalize list of productions, so that it is a single list of tuples
        # Goes from (start symbol, [list of productions]), eg: program -> declarationList 25 compoundStatement
        # (program, [declarationList, 25, compoundStatement])
        self.productions_list = [ProductionTuple(self.s_prime_symbol, [self.starting_symbol])]
        for from_symbol in self.productions:
            for from_list in self.productions[from_symbol]:
                self.productions_list.append(ProductionTuple(from_symbol, from_list))

        self.generate_table()

        self.print_productions_list()
        self.print_states()
        self.print_table()

    def print_table(self):
        state_length = 4
        action_length = 26
        str_format = "{:^" + str(state_length) + "}{:^" + str(action_length) + "}"
        goto_format = "{:^18}"
        state_format = "s{0}"
        left_margin = " " * (state_length + action_length)

        def get_action_str(action):
            if isinstance(action, ActionValues):
                return action.name

            if isinstance(action, int):
                return "REDUCE {0}".format(action)

            return str(action)

        def find_goto_item(symbol, items):
            for item in items:
                if symbol == item.symbol:
                    return item

            return None

        def get_goto_list(row, symbols):
            goto = []
            for symbol in symbols:
                item = find_goto_item(symbol, row[1:])
                if item is None:  # fill with empty space
                    goto.append(goto_format.format("_"))
                    continue

                # symbol exists in the goto item list
                goto.append(goto_format.format(state_format.format(item.state_index)))

            return goto

        # build header
        goto = [goto_format.format(symbol) for symbol in self.non_terminals]
        goto.extend([goto_format.format(symbol) for symbol in self.terminals])

        print("\n{:^120}".format("TABLE"))
        print(str_format.format("State", "Action") + left_margin + "GOTO", end='\n' * 2)
        print(left_margin + "".join(goto))
        for i, row in enumerate(self.table):
            goto = []
            state_str = state_format.format(i)
            action = row[0]

            # conflict in the action field
            if isinstance(action, set):
                action_str = ", ".join([get_action_str(item) for item in action])
            else:
                action_str = get_action_str(action)

            # build goto
            goto.extend(get_goto_list(row, self.non_terminals))
            goto.extend(get_goto_list(row, self.terminals))

            print(str_format.format(state_str, action_str) + "".join(goto))

    def print_productions_list(self):
        print("\n{:^120}".format("PRODUCTIONS LIST"))
        for i, prod in enumerate(self.productions_list):
            print("nr = {0}, {1}".format(i, "{0} = {1}".format(prod.lhs, " ".join(prod.rhs))))

    def print_states(self):
        print("\n{:^120}".format("STATES"))
        for i, str_state in enumerate([", ".join(str(i) for i in state) for state in self.all_states]):
            print("s{0} = {{{1}}}".format(i, str_state))

    def closure(self, closure):
        changed = True
        while changed:
            changed = False
            for item in closure:
                # No symbols after dot aka the dot is at the end
                if not item.after_dot:
                    continue

                # First symbol after the dot is a non terminal
                if item.after_dot[0] in self.non_terminals:
                    lhs = item.after_dot[0]
                    assert lhs in self.productions

                    # Get all the productions of the non terminal after the dot
                    for lhs_list in self.productions[lhs]:
                        new_item = LR0Item(lhs, [], lhs_list)

                        # It is a new item
                        if new_item not in closure:
                            closure.append(new_item)
                            changed = True

        return closure

    def goto(self, state, symbol):
        output = []
        for item in state:
            # the dot is at the end
            if not item.after_dot:
                continue

            # the dot is followed by the searched symbol
            if symbol == item.after_dot[0]:
                # move the dot after our found symbol
                before_dot = item.before_dot[:]
                before_dot.append(symbol)

                # build a new state
                output.append(LR0Item(item.lhs, before_dot, item.after_dot[1:]))

        # expand this by applying closure over it
        return self.closure(output)

    def canonical_col(self):
        # S' -> .<starting symbol>
        s0 = self.closure([LR0Item(self.s_prime_symbol, [], [self.starting_symbol])])
        self.all_states.append(s0)

        changed = True
        while changed:
            changed = False
            for state in self.all_states:
                # Try to find the symbols in the existing states so that we can construct new unique states
                for symbol in self.terminals_and_non_terminals:
                    new_state = self.goto(state, symbol)
                    if new_state and new_state not in self.all_states:
                        self.all_states.append(new_state)
                        changed = True

    def generate_table(self):
        # Construct sparse table
        for state in self.all_states:
            row = []

            # Complete actions
            for item in state:
                if item.after_dot:
                    if not row:  # shift
                        row.append(ActionValues.SHIFT)
                    elif row[0] != ActionValues.SHIFT:  # conflict, shift - reduce, shift - accept
                        if isinstance(row[0], set):
                            row[0].add(ActionValues.SHIFT)
                        else:
                            row[0] = {row[0], ActionValues.SHIFT}

                        # index = self.all_states.index(state)
                        # Log.error('ERROR', row, " ||| ", item, " ||| ", self.all_states[index])
                        # raise TableConflictException("Shift - reduce conflict in state " + str(index))
                else:
                    if not row:  # Add action
                        if item.lhs == self.s_prime_symbol:  # accept
                            row.append(ActionValues.ACCEPT)
                        else:  # reduce

                            # Find production index
                            reduced = False
                            for i, production in enumerate(self.productions_list):
                                if production.lhs == item.lhs and production.rhs == item.before_dot:
                                    row.append(i)
                                    reduced = True
                                    break

                            if not reduced:
                                raise TableConflictException("No reduction found for item {0}".format(item))

                    # TODO, find  out if we really need the code below
                    elif row[0] < ActionValues.SHIFT:  # Shift - accept conflict
                        raise TableConflictException(
                                "Shift - accept conflict in state {0}".format(self.all_states.index(state)))
                    elif row[0] == ActionValues.SHIFT:  # Shift - reduce conflict
                        raise TableConflictException(
                                "Shift - reduce conflict in state {0}".format(self.all_states.index(state)))
                    elif row[0] > ActionValues.SHIFT:  # Reduce - reduce conflict
                        raise TableConflictException(
                                "Reduce - reduce conflict in state {0}".format(self.all_states.index(state)))
                    else:
                        raise TableConflictException("Should never reach here")

            # Complete goto
            for symbol in self.terminals_and_non_terminals:
                goto_state = self.goto(state, symbol)
                if goto_state:
                    index = self.all_states.index(goto_state)
                    row.append(GOTOItem(symbol, index))

            self.table.append(row)

    def _get_table_action(self, state_index):
        return self.table[state_index][0]

    def _get_table_goto(self, state_index, prediction):
        for goto in self.table[state_index][1:]:
            if goto.symbol == prediction:
                return goto.state_index

        raise ParsingException("Unexpected character")

    def print_error(self, msg, sequence, input_stack):
        if self.lexer:
            # parsed_sequence = sequence[:len(sequence) - len(input_stack)]
            # parsed_sequence_str = " ".join([el.token.name for el in parsed_sequence])
            # input_stack_str = " ".join([Tokens.Token(int(el)).name for el in input_stack])

            # error_message = "{0}: {1} {2}".format(msg, parsed_sequence_str, input_stack_str)
            # offset = len(error_message) - len(input_stack_str)
            # error_message += "\n" + offset * " " + "^"
            error_line = input_stack[0]
            Log.error("{0}: on line {1}".format(msg, error_line.line))
            Log.error(self.lexer.get_line_nr(error_line.line))
            Log.error(" " * error_line.column + "^")
        else:
            print(input_stack)
            parsed_sequence = sequence[:len(sequence) - len(input_stack)]
            error_message = msg + ": " + str(parsed_sequence) + "".join(input_stack)
            offset = len(error_message) - len(input_stack)
            error_message += "\n" + offset * " " + "^"
            Log.error(error_message)

    def parse(self, sequence=None):
        # include indices only for states
        if self.lexer:
            # input_stack = [str(int(el.type)) for el in self.lexer.get_tokens()]
            input_stack = self.lexer.get_tokens()
        else:
            assert sequence
            input_stack = list(sequence)

        config = ParseTuple([0], input_stack, [])
        prediction = None
        while True:
            action = self._get_table_action(config.working_stack[-1])

            # Take first element from the input stack
            if config.input_stack:

                if self.lexer:
                    prediction = str(int(config.input_stack[0].type))

                    # TODO, fix hack, ignore multiple empty lines
                    if len(config.input_stack) > 1 and config.input_stack[0].type is Tokens.Token.NEWLINE and \
                            config.input_stack[1].type is Tokens.Token.NEWLINE:
                        config.input_stack.pop(0)
                        continue
                else:
                    prediction = config.input_stack[0]

            # resolve conflict, prefer shift action where possible
            if isinstance(action, set):
                assert len(action) == 2
                this = self
                first_action, second_action = action
                # first action is always a shift/accept
                if first_action > second_action:
                    first_action, second_action = second_action, first_action

                def can_shift(act):
                    try:
                        this._get_table_goto(config.working_stack[-1], prediction)
                        return True
                    except ParsingException:
                        return False

                # shift - reduce
                if first_action is ActionValues.SHIFT and second_action > ActionValues.SHIFT:
                    if can_shift(first_action):  # shift if we can
                        action = first_action
                    else:  # fallback to reduce
                        action = second_action

                # accept - shift
                elif first_action is ActionValues.ACCEPT and second_action is ActionValues.SHIFT:
                    if config.input_stack and can_shift(second_action):  # shift if we can
                        action = second_action
                    else:  # fallback to accept
                        action = first_action

                else:
                    raise ParsingException("Unexpected conflict {0}".format(action))

            if not config.input_stack and action == ActionValues.ACCEPT:
                return config

            elif action == ActionValues.SHIFT:
                try:
                    index_state = self._get_table_goto(config.working_stack[-1], prediction)
                except ParsingException as e:
                    self.print_error(e.value, sequence, config.input_stack)
                    return None

                # found goto, add to working stack and remove from input stack
                config.working_stack.extend([prediction, index_state])

                try:
                    config.input_stack.pop(0)
                except IndexError:
                    self.print_error("Unexpected end of input sequence", sequence, config.input_stack)
                    return None

            elif action > ActionValues.SHIFT:  # REDUCE

                # remove from working stack until we find our first symbol in the production
                production = self.productions_list[action]
                while production.rhs[0] != config.working_stack.pop():
                    pass

                # Add lhs to our working stack and get next goto
                config.working_stack.append(production.lhs)
                try:
                    index_state = self._get_table_goto(config.working_stack[-2], production.lhs)
                except ParsingException as e:
                    self.print_error(e.value, sequence, config.input_stack)
                    return None

                config.working_stack.append(index_state)
                config.output_stack.insert(0, action)

            else:
                self.print_error("Unexpected character", sequence, config.input_stack)
                return None
