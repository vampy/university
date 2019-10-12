#!/usr/bin/env python3


# generating permutations, binary strings of length n, subsets, partitions, Cartesian product
class BT:
    def __init__(self):
        pass

    def reject(self, candidate):
        return False

    def accept(self, candidate):
        return True

    def first(self, candidate):
        return []

    def next(self, candidate):
        return []

    def output(self, candidate):
        print(candidate)

    def bt(self, candidate):
        if self.reject(candidate):
            return
        if self.accept(candidate):
            self.output(candidate)

        solution = self.first(candidate)
        while solution:
            self.bt(solution)
            solution = self.next(solution)

    def run(self):
        self.bt([])


class BTParanthesis:
    def __init__(self, num_para):
        candidate = (2 * num_para + 1) * ['']
        print("Correct parenthesis")
        self.bt(num_para, candidate)
        print("\n")

    def reject(self, current_pos, num_para, num_open):
        return num_open > (2 * num_para - current_pos)  # 5 open, no sense to open another Parenthesis

    def accept(self, current_pos, num_para):
        return current_pos == 2 * num_para

    def output(self, candidate):
        print("".join(candidate[:-1]))

    def bt(self, num_para, candidate, current_pos=0, num_open=0):
        if self.reject(current_pos, num_para, num_open):
            return
        if self.accept(current_pos, num_para):
            self.output(candidate)

        candidate[current_pos] = '('
        self.bt(num_para, candidate, current_pos + 1, num_open + 1)
        if num_open:  # close
            candidate[current_pos] = ')'
            self.bt(num_para, candidate, current_pos + 1, num_open - 1)


class BTPermutations:
    def __init__(self, candidate=[]):
        print("Permutations")
        self.len_accept = len(candidate)
        self.bt(list(candidate))
        print("\n")

    def accept(self, current_pos):
        return self.len_accept == current_pos

    def output(self, candidate):
        print(", ".join([str(i) for i in candidate]))

    def bt(self, candidate, current_pos=0):
        if self.accept(current_pos):
            self.output(candidate)

        for i in range(current_pos, self.len_accept):
            candidate[current_pos], candidate[i] = candidate[i], candidate[current_pos]
            self.bt(candidate, current_pos + 1)
            candidate[current_pos], candidate[i] = candidate[i], candidate[current_pos]


class BTCombinations:
    def __init__(self, candidate, k):
        print("Combinations")
        self.k = k
        self.len_accept = len(candidate)
        self.bt(candidate)
        print("\n")

    def accept(self, current_pos, candidate):
        if self.k == current_pos:
            for i in range(self.k - 1):
                if candidate[i] > candidate[i + 1]:
                    return False

            return True

        return False

    def reject(self, current_pos, candidate):
        return (current_pos + 1) < self.len_accept and candidate[current_pos] > candidate[current_pos + 1]

    def output(self, candidate):
        print(", ".join([str(candidate[i]) for i in range(self.k)]))

    def bt(self, candidate, current_pos=0):
        # if self.reject(current_pos, candidate):
        #     return

        if self.accept(current_pos, candidate):
            self.output(candidate)

        for i in range(current_pos, self.len_accept):
            candidate[current_pos], candidate[i] = candidate[i], candidate[current_pos]
            self.bt(candidate, current_pos + 1)
            candidate[current_pos], candidate[i] = candidate[i], candidate[current_pos]


if __name__ == "__main__":
    #pr = BTParanthesis(3)
    BTPermutations([1, 2])
    BTCombinations([1, 2, 3, 4], 2)
