# -*- coding: utf-8 -*-
import copy
import pickle
from random import *
from utils import *
import numpy as np


# the glass gene can be replaced with int or float, or other types
# depending on your problem's representation

class gene:
    def __init__(self, size):
        # random between the 4 possible directions
        self.chromosome = [randint(1, 4) for _ in range(size)]


class Individual:
    def __init__(self, size=0):
        self.__size = size
        self.__x = gene(size).chromosome
        self.f = None

    def fitness(self, map, drone):
        # compute the fitness for the individual
        # and save it in self.__f
        self.f = 0
        path = self.computePath(map, drone)
        # print(path)
        visited = []
        directions = [[-1, 0], [0, 1], [1, 0], [0, -1]]

        for i in range(len(path)):
            x = path[i][0]
            y = path[i][1]

            if [x, y] not in visited:
                visited.append([x, y])
                if (0 > x or 0 > y or x >= map.n or y >= map.m) or map.surface[x][y] == 1:
                    break

                self.f += 1

                for dir in directions:
                    while (0 <= x + dir[0] < map.n and 0 <= y + dir[1] < map.m) and map.surface[x + dir[0]][y + dir[1]] != 1:
                        if [x + dir[0], y + dir[1]] not in visited:
                            visited.append([x + dir[0], y + dir[1]])
                            self.f += 1
                        x = x + dir[0]
                        y = y + dir[1]

    def computePath(self, map, drone):
        path = [[drone[0], drone[1]]]

        for i in self.__x:
            if i == 1:
                path.append([path[-1][0] - 1, path[-1][1]])
            elif i == 2:
                path.append([path[-1][0], path[-1][1] + 1])
            elif i == 3:
                path.append([path[-1][0] + 1, path[-1][1]])
            elif i == 4:
                path.append([path[-1][0], path[-1][1] - 1])

        valid_path = []
        for p in path:
            if 0 > p[0] or 0 > p[1] or p[0] >= map.n or p[1] >= map.m:
                break
            if map.surface[p[0]][p[1]] == 1:
                break
            valid_path.append(p)
        return valid_path

    def mutate(self, mutateProbability=0.04):
        if random() < mutateProbability:
            self.__x[randint(0, self.__size - 1)] = randint(1, 4)
            # perform a mutation with respect to the representation

    def crossover(self, otherParent, crossoverProbability=0.8):
        offspring1, offspring2 = Individual(self.__size), Individual(self.__size)
        if random() < crossoverProbability:
            position = randint(0, self.__size - 1)
            offspring1.__x = otherParent.__x[:position] + self.__x[position:]
            offspring2.__x = self.__x[:position] + otherParent.__x[position:]
            # perform the crossover between the self and the otherParent 

        return offspring1, offspring2


class Population():
    def __init__(self, populationSize=0, individualSize=0):
        self.__populationSize = populationSize
        self._v = [Individual(individualSize) for x in range(self.__populationSize)]

    def evaluate(self, map, drone):
        # evaluates the population
        for x in self._v:
            x.fitness(map, drone)

    def set_population(self, v):
        self._v = v

    def add_individual(self, individual, map, drone):
        individual.fitness(map, drone)
        self._v.append(individual)

    def set_individuals(self, indiv):
        self._v = indiv

    def size(self):
        return len(self._v)

    def get_first_path(self, map, drone):
        self.evaluate(map, drone)
        individuals = copy.deepcopy(self._v)

        done = False
        while not done:
            done = True

            for i in range(0, len(individuals) - 1):
                if individuals[i].f < individuals[i + 1].f:
                    saved_individual = individuals[i]
                    individuals[i] = individuals[i + 1]
                    individuals[i + 1] = saved_individual
                    done = False

        return individuals[0].computePath(map, drone)

    def selection(self, k=0):
        # perform a selection of k individuals from the population
        # and returns that selection
        selected = []
        individuals = copy.deepcopy(self._v)

        done = False
        while not done:
            done = True

            for i in range(0, len(individuals) - 1):
                if individuals[i].f < individuals[i+1].f:
                    saved_individual = individuals[i]
                    individuals[i] = individuals[i+1]
                    individuals[i+1] = saved_individual
                    done = False

        for i in range(0, k):
            selected.append(individuals[i])

        return selected

    def average_fitness_deviation(self, map, drone):
        fitness = []
        for x in self._v:
            x.fitness(map, drone)
            fitness.append(x.f)
        return [np.average(fitness), np.std(fitness)]

    def best_fitness(self, map, drone):
        self.evaluate(map, drone)

        aux_individuals = copy.deepcopy(self._v)

        done = False
        while not done:
            done = True

            for i in range(0, len(aux_individuals) - 1):
                if aux_individuals[i].f < aux_individuals[i + 1].f:
                    saved_individual = aux_individuals[i]
                    aux_individuals[i] = aux_individuals[i + 1]
                    aux_individuals[i + 1] = saved_individual
                    done = False

        return aux_individuals[0].f

class Map():
    def __init__(self, n=20, m=20):
        self.n = n
        self.m = m
        self.surface = np.zeros((self.n, self.m))

    def randomMap(self, fill=0.2):
        for i in range(self.n):
            for j in range(self.m):
                if random() <= fill:
                    self.surface[i][j] = 1

    def saveMap(self, numFile="test1.map"):
        with open(numFile, 'wb') as f:
            pickle.dump(self, f)
            f.close()

    def loadMap(self, numfile):
        with open(numfile, "rb") as f:
            dummy = pickle.load(f)
            self.n = dummy.n
            self.m = dummy.m
            self.surface = dummy.surface
            f.close()

    def __str__(self):
        string = ""
        for i in range(self.n):
            for j in range(self.m):
                string = string + str(int(self.surface[i][j]))
            string = string + "\n"
        return string
