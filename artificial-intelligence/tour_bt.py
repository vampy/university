#!/usr/bin/env python3
import sys
import copy


class Board:
    def __init__(self, rows, cols):
        self.rows = rows
        self.cols = cols
        self.board = []
        self.initial_fill()

    def initial_fill(self):
        for i in range(self.rows):
            self.board.append(self.cols * [-1])

    def is_safe(self, x, y):
        """
        Check if coordinates are in range
        :param x:
        :param y:
        :return: boolean
        """
        return (0 <= x < self.rows) and (0 <= y < self.cols) and (self.board[x][y] == -1)

    def __str__(self):
        return "\n".join([", ".join(["%2i" % i for i in row]) for row in self.board])


class BoardKT(Board):
    # move_x, move_y
    moves = ((-1, 2), (-2, 1), (1, 2), (2, 1), (2, -1), (1, -2), (-1, -2), (-2, -1))

    def __init__(self, rows, cols):
        Board.__init__(self, rows, cols)
        self.bs_board = copy.deepcopy(self.board)
        self.bs_counter = 0

    def solve_kt(self, start_x=0, start_y=0):
        self.board[start_x][start_y] = 0

        return self.fill_kt(start_x, start_y, 1)

    def fill_kt(self, x, y, counter):
        if counter > self.bs_counter:  # find the closest solution
            self.bs_counter = counter
            self.bs_board = copy.deepcopy(self.board)

        if counter == self.rows * self.cols:
            return True

        for move in self.moves:  # iterate over all possible positions
            move_x, move_y = x + move[0], y + move[1]
            if self.is_safe(move_x, move_y):
                self.board[move_x][move_y] = counter  # set current position

                # check next move
                if self.fill_kt(move_x, move_y, counter + 1):
                    return True
                else:
                    self.board[move_x][move_y] = -1  # backtrack

        return False

    def __str__(self):
        return "\n".join([", ".join(["%2i" % i for i in row]) for row in self.bs_board])


def exit_error(message):
    exit("ERROR: " + message)

if __name__ == "__main__":
    if len(sys.argv) < 3:
        exit_error(" usage is %s N M" % (sys.argv[0]))

    start_x, start_y = 0, 0
    if len(sys.argv) == 5:
        start_x, start_y = int(sys.argv[3]), int(sys.argv[4])

    N = int(sys.argv[1])
    M = int(sys.argv[2])
    print("Board size: %d X %d" % (N, M))

    # validate http://faculty.olin.edu/~sadams/DM/ktpaper.pdf
    if (N < 3 and M < 2) or (N < 2 and M < 3):
        exit_error("Board is not wide enough to construct a tour")

    board = BoardKT(N, M)
    if board.solve_kt(start_x, start_y) is False:
        print("Could not walk all the board")
    print("Walked: %d" % board.bs_counter)
    print(board)
