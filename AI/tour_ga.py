#!/usr/bin/env python3
from random import random, randint
import copy
import sys


class Chromosome:
    def __init__(self, data, fitness=0):
        self.data = data
        self.fitness = fitness

    def __str__(self):
        return "data = %s, fitness = %s" % (str(self.data), str(self.fitness))


# http://citeseerx.ist.psu.edu/viewdoc/download?rep=rep1&type=pdf&doi=10.1.1.115.3709
# http://athena.ecs.csus.edu/~gordonvs/papers/knightstour.pdf
class GATour:
    map_moves = {  # row, col
        "000": (2, 1),
        "001": (1, 2),
        "010": (-1, 2),
        "011": (-2, 1),
        "100": (-2, -1),
        "101": (-1, -2),
        "110": (1, -2),
        "111": (2, -1)
    }

    def __init__(self, population_size, number_generations, rows=8, cols=8, start_row=0, start_col=0, p_crossover=0.8,
                 p_mutation=0.01):
        assert population_size > 3

        self.rows = rows
        self.cols = cols
        self.start_row = start_row
        self.start_col = start_col
        self.total_moves = self.rows * self.cols - 1  # total amount of moves possible, 63 for a 8*8 board
        self.population_size = population_size
        self.number_generations = number_generations
        self.population = []
        self.p_crossover = p_crossover
        self.p_mutation = p_mutation

        # initial board template that is filled with -1, meaning that the position was not walked on
        self.board_walk = []

        # just a board that has numbers from 1 to total_moves + 1, used only for debugging and testing
        self.board_positions = []

    def sort_population(self):
        # sort by fitness descending
        self.population.sort(key=lambda x: x.fitness, reverse=True)

    def one_point_crossover(self, parent1, parent2):
        # the cutting point should be after first gene and before the last one
        cut = randint(1, self.total_moves * 3 - 2)
        o1_data = parent1.data[:cut] + parent2.data[cut:]
        o2_data = parent2.data[:cut] + parent1.data[cut:]

        return Chromosome(o1_data), Chromosome(o2_data)

    def mutate(self, chromosome):
        """
        Mutate chromosome by flipping the bits
        We can do this because of our representation of our knights and moves
        :param chromosome:
        :return:
        """
        data = list(chromosome.data)
        for i in range(len(data)):
            if random() < self.p_mutation:  # flip bits
                if data[i] == '0':
                    data[i] = '1'
                elif data[i] == '1':
                    data[i] = '0'

        chromosome.data = "".join(data)

    def repair_chromosome(self, chromosome, board, working_row, working_col, broken_pos=0):
        """
        Try to repair chromosome when computing the fitness function
        :param chromosome: the chromosome that will be modified
        :param board: the current board
        :param working_row: the last row position
        :param working_col: the last col position
        :param broken_pos: the position in the chromosome.data that the bad gene started, of length 3
        :return: True if something was repaired, False otherwise
        """
        assert broken_pos >= 0

        lowest_str = None
        lowest_legal = None
        for move_str in self.map_moves:
            move_x, move_y = self.map_moves[move_str]
            pos_x, pos_y = working_row + move_x, working_col + move_y

            # found potential move
            if self.is_position_safe(board, pos_x, pos_y):
                moves = self.get_legal_moves(board, pos_x, pos_y)

                # found better position
                if lowest_legal is None or moves < lowest_legal:
                    lowest_legal = moves
                    lowest_str = move_str

        # repair
        if lowest_str is not None:
            # print("Found repair=", lowest_str)

            # copy repair, python string are immutable, convert it to a list then back to a string again
            l = list(chromosome.data)
            for i in range(3):
                l[broken_pos + i] = lowest_str[i]

            chromosome.data = "".join(l)

            return True

        return False

    def fitness(self, chromosome):
        """
        We compute the fitness for each chromosome by counting the number of legal moves represented in each
        chromosome. An illegal move can be made by a knight when it jumps off the board or returns to a previously
        visited square.
        :param chromosome:
        :return: fitness
        """
        board = copy.deepcopy(self.board_walk)
        current_row, current_col, current = self.start_row, self.start_col, 0
        board[current_row][current_col] = current
        current += 1
        fitness = 0

        for i in range(0, len(chromosome.data), 3):
            move_str = chromosome.data[i:i + 3]  # take the 3 bit string representing the chromosome

            move_x, move_y = self.map_moves[move_str]
            current_row += move_x
            current_col += move_y

            if self.is_position_safe(board, current_row, current_col):
                fitness += 1
                current += 1
                board[current_row][current_col] = current
                # the bits which follow an illegal move are ignored
            elif self.repair_chromosome(chromosome, board, current_row - move_x, current_col - move_y, i):
                return self.fitness(chromosome)
            else:
                break

        chromosome.fitness = fitness
        return fitness

    def initialize(self):
        """
        Init the board positions and data, initial population
        """

        # generate helper matrix of board positions
        counter = 1
        for i in range(self.rows):
            self.board_positions.append([-1] * self.cols)
            for j in range(self.cols):
                self.board_positions[i][j] = counter
                counter += 1

        # generate template board for walking on, -1 means the position was not walked on
        for i in range(self.rows):
            self.board_walk.append(self.cols * [-1])

        # generate chromosomes
        moves = list(self.map_moves.keys())
        length_moves = len(moves) - 1
        for i in range(self.population_size):
            # generate strings of total_moves * 3 bits
            population = ""
            for j in range(self.total_moves):
                population += moves[randint(0, length_moves)]

            # generate chromosome and compute fitness
            chromosome = Chromosome(population)
            self.fitness(chromosome)

            # add to the population
            self.population.append(chromosome)

        self.sort_population()

    def tournament_selection(self, tournament_size=None):
        """
        Select a chromosome by using random selection
        :param tournament_size:
        """
        if tournament_size is None:
            tournament_size = self.population_size

        selected = randint(0, self.population_size - 1)
        for i in range(tournament_size):
            r = randint(0, self.population_size - 1)
            if self.population[r].fitness > self.population[selected].fitness:
                selected = r

        return self.population[selected]

    def run(self):
        """
        Run the main GA program
        """
        self.initialize()

        best_moves = 0
        for i in range(self.number_generations):
            if best_moves == self.total_moves:
                print("Found solution prematurely = ", best_moves)
                break

            for j in range(self.population_size):
                # choose parents
                p1 = self.tournament_selection()
                p2 = self.tournament_selection()

                # crossover
                if random() < self.p_crossover:
                    o1, o2 = self.one_point_crossover(p1, p2)
                else:
                    o1, o2 = copy.deepcopy(p1), copy.deepcopy(p2)

                # mutate
                self.mutate(o1)
                self.mutate(o2)
                self.fitness(o1)
                self.fitness(o2)

                # copy the best of the two into our population
                best_child = max(o1, o2, key=lambda x: x.fitness)
                if best_child.fitness > self.population[self.population_size - 1].fitness:
                    self.population[self.population_size - 1] = best_child

                    self.sort_population()

                # find only one solution
                best_moves = max(best_moves, max(o1, o2, p1, p2, key=lambda x: x.fitness).fitness)

        # best solution
        print(self.board_to_str(self.get_board_walked(self.population[0])))

    def get_board_walked(self, chromosome):
        """
        Fill a board with the walk data from the chromosome
        :param chromosome:
        :return:
        """
        board = copy.deepcopy(self.board_walk)
        current_row, current_col, current = self.start_row, self.start_col, 0
        board[current_row][current_col] = current
        current += 1

        for i in range(0, len(chromosome.data), 3):
            move_str = chromosome.data[i:i + 3]  # take the 3 bit string representing the chromosome

            move_x, move_y = self.map_moves[move_str]
            current_row += move_x
            current_col += move_y

            if self.is_position_safe(board, current_row, current_col):
                board[current_row][current_col] = current
                current += 1
            else:  # the bits which follow an illegal move are ignored
                break

        return board

    def get_legal_moves(self, board, x, y):  # get nr of legal moves from x and y
        """
        Calculate the number of legal moves you can do from square x, y
        Used for Warnsdorf's rule, see: https://en.wikipedia.org/wiki/Knight%27s_tour#Warnsdorf.27s_rule
        :param board: the board we are doing the legal moves calculation
        :param x: the row of our current square
        :param y: the column of our current square
        :return: the number of legal moves, maximum is 8
        """
        moves = 0
        for move_x, move_y in self.map_moves.values():
            if self.is_position_safe(board, move_x + x, move_y + y):
                moves += 1

        return moves

    def is_position_safe(self, board, x, y, check_if_empty=True):
        """
        Check if coordinates are in the limits of the board and if the check_if_empty is set to true
        also check if position x, y is empty
        """
        on_board = (0 <= x < self.rows) and (0 <= y < self.cols)
        if check_if_empty:  # check if x, y is already occupied
            return on_board and board[x][y] == -1

        return on_board

    @staticmethod
    def board_to_str(board):
        return "\n".join([", ".join(["%2i" % i for i in row]) for row in board])


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

    tour = GATour(rows=N, cols=M, population_size=100, number_generations=200, start_row=start_x, start_col=start_y)
    tour.run()
