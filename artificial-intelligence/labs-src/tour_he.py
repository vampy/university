#!/usr/bin/env python3
import sys


class Board:
    def __init__(self, rows, cols):
        self.rows = rows
        self.cols = cols
        self.board = []
        self.counter = 0
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


class BoardHeuristic(Board):
    # move_x, move_y
    moves = ((-1, 2), (-2, 1), (1, 2), (2, 1), (2, -1), (1, -2), (-1, -2), (-2, -1))

    def solve_kt(self, pos_x=0, pos_y=0):
        # init variables
        self.board[pos_x][pos_y] = self.counter
        self.counter += 1

        while True:
            if self.counter == self.rows * self.cols:
                return True

            lowest_legal = None
            lowest_move = None
            for move_x, move_y in self.moves:
                check_x, check_y = pos_x + move_x, pos_y + move_y

                if self.is_safe(check_x, check_y):
                    moves = self.get_legal_moves(check_x, check_y)

                    if lowest_legal is None or moves < lowest_legal:
                        lowest_legal = moves
                        lowest_move = (move_x, move_y)

            if lowest_move is None:
                return False

            # move piece to the piece with the fewest onwards moves
            pos_x += lowest_move[0]
            pos_y += lowest_move[1]
            self.board[pos_x][pos_y] = self.counter
            self.counter += 1

        return False

    def get_legal_moves(self, x, y):  # get nr of legal moves from x and y
        """
        Calculate the number of legal moves you can do from square x, y
        Used for Warnsdorf's rule, see: https://en.wikipedia.org/wiki/Knight%27s_tour#Warnsdorf.27s_rule
        :param x: the row of our current square
        :param y: the column of our current square
        :return: the number of legal moves, maximum is 8
        """
        moves = 0
        for move_x, move_y in self.moves:
            if self.is_safe(move_x + x, move_y + y):
                moves += 1

        return moves


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

    board = BoardHeuristic(N, M)
    if board.solve_kt(start_x, start_y) is False:
        print("Could not walk all the board")
    print("Walked: %d" % board.counter)
    print(board)
