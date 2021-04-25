import time

import pygame

from gui import movingDrone
from repository import *


class controller():
    def __init__(self, repository):
        self._repository = repository
        self._statistics = []

        self.statistics_history = []
        self.seeds = None
        self.nb_steps = None
        self.population_size = None
        self.nb_iterations = None
        self.crossover_probability = None
        self.mutation_probability = None
        self.current_iteration = 0

    def position_drone(self, string):
        positions = string.split(",")
        position_x = int(positions[0])
        position_y = int(positions[1])
        self._repository.change_drone_position(position_x, position_y)

    def random_drone_position(self):
        self._repository.droneSpawnPosition()

    def nb_of_steps(self, steps):
        self.nb_steps = steps

    def population(self, pop):
        self.population_size = pop

    def iterations(self, iterations):
        self.nb_iterations = iterations

    def set_crossover_probability(self, cp):
        self.crossover_probability = cp

    def set_mutation_probability(self, mp):
        self.mutation_probability = mp

    def set_seed_number(self, seeds):
        self.seeds = seeds

    def show_map(self, map):
        drone = pygame.image.load("drona.png")
        map.blit(drone, (0, 0))
        return map

    def iteration(self, args=0):
        # args - list of parameters needed to run one iteration
        # a iteration:
        # selection of the parents
        # create offsprings by crossover of the parents
        # apply some mutations
        # selection of the survivors
        self.current_iteration += 1
        population = self._repository.latest_population()
        self._repository.evaluate_population(population)

        select = population.selection(population.size() - 1)
        parents = select[:len(select)//2]
        pairs = len(parents) // 2
        used_p = []
        pair_numbers = 0

        for i in range(pairs):
            first = parents[randint(0, len(parents) - 1)]
            sec = parents[randint(0, len(parents) - 1)]
            if [first, sec] not in used_p:
                pair_numbers += 2
                used_p.append([first, sec])
                cross_1, cross_2 = first.crossover(sec, self.crossover_probability)
                cross_1.mutate(self.mutation_probability)
                cross_2.mutate(self.mutation_probability)
                self._repository.add_individual(cross_1, population)
                self._repository.add_individual(cross_2, population)

        select = population.selection(population.size() - pair_numbers)
        population.set_individuals(select)

        
    def run(self, args):
        # args - list of parameters needed in order to run the algorithm
        
        # until stop condition
        #    perform an iteration
        #    save the information need it for the statistics
        
        # return the results and the info for statistics
        self.statistics_history = []

        for i in range(0, self.nb_iterations):
            self.iteration()
            if args == self.seeds - 1:

                self.statistics_history.append(self._repository.average_fitness_deviation())

        self._statistics.append(self._repository.best_fitness_population())
    
    
    def solver(self):
        counter = 0
        # args - list of parameters needed in order to run the solver
        
        # create the population,
        # run the algorithm
        # return the results and the statistics
        start_time = time.time()
        for i in range(self.seeds):
            counter += 1
            print("Population " + str(counter) + " ...")
            seed(30-i)
            population = self._repository.create_population([self.population_size, self.nb_steps])
            self._repository.add_population(population)
            self.run(i)

        end_time = time.time()
        print(f"Execution time: {end_time - start_time:0.5f}")

        return self._repository.get_first_path(), self._statistics, self.statistics_history
