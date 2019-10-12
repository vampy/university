 
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