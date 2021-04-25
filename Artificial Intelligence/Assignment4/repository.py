# -*- coding: utf-8 -*-

import pickle
from domain import *


class repository():
    def __init__(self):
         
        self.__populations = []
        self.drone = [0, 0]
        self.cmap = Map()
        self._colonies = []
        
    def create_population(self, args):
        # args = [populationSize, individualSize] -- you can add more args    
        return Population(args[0], args[1])

    def add_population(self, population):
        self.__populations.append(population)

    def add_colonies(self, ant_number, energy):
        self._colonies.append(AntColony(ant_number, energy))

    def get_last_colony(self):
        return self._colonies[-1]

    def add_individual(self, individual, population):
        population.add_individual(individual, self.cmap, self.drone)

    def change_drone_position(self, x, y):
        self.drone = [x, y]

    def latest_population(self):
        return self.__populations[-1]

    def get_first_path(self):
        return self.__populations[-1].get_first_path(self.cmap, self.drone)

    def evaluate_population(self, pop):
        pop.evaluate(self.cmap, self.drone)

    #get first random free position
    def droneSpawnPosition(self):

        initial_x = randint(0, self.cmap.n - 1)
        initial_y = randint(0, self.cmap.m - 1)

        while self.cmap.surface[initial_x][initial_y] != 0:
            initial_x = randint(0, self.cmap.n - 1)
            initial_y = randint(0, self.cmap.m - 1)

        self.drone = [initial_x, initial_y]

    def average_fitness_deviation(self):
        return self.__populations[-1].average_fitness_deviation(self.cmap, self.drone)

    def best_fitness_population(self):
        return self.__populations[-1].best_fitness(self.cmap, self.drone)
