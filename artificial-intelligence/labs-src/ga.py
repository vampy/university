#!/usr/bin/env python3
from random import random, shuffle, randint
import copy


class GA:
    def __init__(self):
        pass

    def initialize(self):
        pass

    def compute_fitness(self):
        pass

    def crossover(self):
        pass

    def mutate(self):
        pass


class Chromosome:
    def __init__(self, data, fitness=0):
        self.data = data
        self.fitness = fitness

    def __str__(self):
        return "data = %s, fitness = %s" % (str(self.data), str(self.fitness))


"""
4. Traveling salesman problem

There are n cities with direct connection between any of them. Distance between cities
are positive numbers. Find a minimal path that starts from city 1, goes to all other cities
exactly once and returns to city 1.

Example 1:
TSP problem - given a map with n cities, and the distance between any pair of cities, find the shortest path that visits each city exactly once.
representation: permutations of cities. If cities are labeled from 1 to n, then 1,2,3,...,n is a chromosome in our representation
fitness - the length of the path. sum all distances between all pairs of consecutive cities in the given chromosome. fitness must be minimized.
initialization - start with the identical permutation and perform n swaps. Or you can use the Knuth shuffle.
crossover - one cutting point with corrections. (must explain what correction means). If a city appears twice in an offspring,
one copy will be eliminated and another, which appears 0 times) is inserted in that position
mutation - swap 2 positions.
"""
class GATSP:
    def __init__(self, population_size, number_generations, cities, p_crossover=0.8, p_mutation=0.015):
        self.population_size = population_size
        assert population_size > 3
        self.number_generations = number_generations
        self.population = []
        self.cities = cities
        self.n = len(cities.keys())
        self.p_crossover = p_crossover
        self.p_mutation = p_mutation

    def get_distance(self, start, end):
        return self.cities[start][end]

    def tournament_selection(self, tournament_size=None):
        if tournament_size is None:
            tournament_size = self.population_size

        selected = randint(0, self.population_size - 1)
        for i in range(tournament_size):
            r = randint(0, self.population_size - 1)
            if self.population[r].fitness < self.population[selected].fitness:
                selected = r

        return self.population[selected]

    def crossover(self, parent1, parent2):
        # see http://permutationcity.darkredcomics.com/website/projects/mutants/tsp.html
        p1_data = parent1.data[:].sort()
        p2_data = parent2.data[:].sort(reversed=True)

        o1_data = p1_data[:]
        o2_data = p2_data[:]

    def one_point_crossover(self, parent1, parent2):
        cut = randint(1, self.n - 2)  # the cutting point should be after first gene and before the last one
        o1_data = parent1.data[:cut] + parent2.data[cut:]
        o2_data = parent2.data[:cut] + parent1.data[cut:]

        def apply_corrections(data):
            length = len(data)
            for i in range(1, length):
                city = data[i]

                # check duplicate
                if city in data[:i]:  # found city in back

                    # find new best city
                    for j in range(1, length + 1):
                        if j not in data:
                            data[i] = j
                            break

        # apply corrections, duplicate cities
        apply_corrections(o1_data)
        apply_corrections(o2_data)

        o1 = Chromosome(o1_data)
        o2 = Chromosome(o2_data)

        return o1, o2

    def mutate(self, chromosome):  # swap 2 cities
        if random() < self.p_mutation:
            start = randint(0, self.n - 1)
            end = randint(0, self.n - 1)
            chromosome.data[start], chromosome.data[end] = chromosome.data[end], chromosome.data[start]

    def compute_fitness(self, chromosome):
        chromosome.fitness = 0
        length = len(chromosome.data)
        for i in range(length - 1):  # sum all distances between cities
            chromosome.fitness += self.get_distance(chromosome.data[i], chromosome.data[i + 1])

        # back to initial city
        # chromosome.fitness += self.get_distance(chromosome.data[0], chromosome.data[-1])

    def initialize(self):
        for i in range(self.population_size):
            data = list(range(1, self.n + 1))

            # shuffle
            shuffle(data)

            # compute initial fitness
            chromosome = Chromosome(data)
            self.compute_fitness(chromosome)

            # add to populations
            self.population.append(chromosome)

        # sort by fitness
        self.population.sort(key=lambda x: x.fitness)

    def run(self):
        self.initialize()
        for pop in self.population:
            print(pop)

        # main loop
        print("main loop\n")
        for i in range(self.number_generations):
            for j in range(self.population_size):
                # choose best parents
                p1 = self.tournament_selection(2)
                p2 = self.tournament_selection(2)

                # crossover
                if random() < self.p_crossover:
                    o1, o2 = self.one_point_crossover(p1, p2)
                else:
                    o1, o2 = copy.deepcopy(p1), copy.deepcopy(p2)

                # mutate
                self.mutate(o1)
                self.mutate(o2)
                self.compute_fitness(o1)
                self.compute_fitness(o2)

                # copy the best of the two into our population
                best_child = min(o1, o2, key=lambda x: x.fitness)
                if best_child.fitness < self.population[self.population_size - 1].fitness:
                    self.population[self.population_size - 1] = best_child
                    # sort by fitness
                    self.population.sort(key=lambda x: x.fitness)

        print("Best: \n", self.population[0])

if __name__ == "__main__":

    # https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Weighted_K4.svg/220px-Weighted_K4.svg.png
    # A is 1, B is 2, C is 3, D is 4
    Cities = {
        1: {2: 20, 3: 42, 4: 35},
        2: {1: 20, 3: 30, 4: 34},
        3: {1: 42, 2: 30, 4: 12},
        4: {1: 35, 2: 34, 3: 12}
    }
    ga = GATSP(population_size=4, number_generations=1000, cities=Cities)
    ga.run()
    print("Running")

