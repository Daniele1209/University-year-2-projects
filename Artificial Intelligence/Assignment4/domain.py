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
        path = self.get_valid_path(map, drone)
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
                    while ((0 <= x + dir[0] < map.n and 0 <= y + dir[1] < map.m) and map.surface[x + dir[0]][y + dir[1]] != 1):
                        if [x + dir[0], y + dir[1]] not in visited:
                            visited.append([x + dir[0], y + dir[1]])
                            self.f += 1
                        x = x + dir[0]
                        y = y + dir[1]

    def get_valid_path(self, map, drone):
        start_position = [drone[0], drone[1]]
        path = []
        path.append(start_position)

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


class Population:
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

        return individuals[0].get_valid_path(map, drone)

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

class Map:
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


class Sensor:
    def __init__(self, x_position, y_position, map, energy=0):
        self.x_position = x_position
        self.y_position = y_position
        self._energy = energy
        self.visible_squares = []
        self._map = map
        self._maximum_squares = self.optimal_squares()
        self.seen_squares_energy = {1: self.get_max_squares(self.x_position, self.y_position, 1),
                                    2: self.get_max_squares(self.x_position, self.y_position, 2),
                                    3: self.get_max_squares(self.x_position, self.y_position, 3),
                                    4: self.get_max_squares(self.x_position, self.y_position, 4),
                                    5: self.get_max_squares(self.x_position, self.y_position, 5)}
        self._optimal_energy = self.optimal_energy_needed()

    def get_needed_energy(self):
        return self._maximum_squares

    def get_position(self):
        return [self.x_position, self.y_position]

    def get_optimal(self):
        return self._optimal_energy

    def set_energy(self, energy):
        self._energy = energy

    def add_energy(self, energy_amount):
        # Find the first up position that is not seen by the sensor
        energy_N = energy_amount
        i_N = self.x_position - 1
        j_N = self.y_position
        self.addition_loop(energy_N, i_N, j_N, "N")

        # Find the first down position that is not seen by the sensor
        energy_S = energy_amount
        i_S = self.x_position + 1
        j_S = self.y_position
        self.addition_loop(energy_S, i_S, j_S, "S")

        # Find the first left position that is not seen by the sensor
        energy_W = energy_amount
        i_W = self.x_position
        j_W = self.y_position - 1
        self.addition_loop(energy_W, i_W, j_W, "W")

        # Find the first upper position that is not seen by the sensor
        energy_E = energy_amount
        i_E = self.x_position
        j_E = self.y_position + 1
        self.addition_loop(energy_E, i_E, j_E, "E")

    def addition_loop(self, energy, i, j, direction):
        while energy != 0 and 20 > i >= 0 and 20 > j >= 0:
            if self._map.surface[j][i] == 1:
                break
            else:
                if [i, j] not in self.visible_squares:
                    self.visible_squares.append([i, j])
                    energy -= 1
            if direction == "N":
                i -= 1
            elif direction == "S":
                i += 1
            elif direction == "W":
                j -= 1
            elif direction == "E":
                j += 1

    def optimal_energy_needed(self):
        squares = self._maximum_squares
        for key, value in self.seen_squares_energy.items():
            if value == squares:
                return key

    def optimal_squares(self):
        max_squares = 0

        for e in range(1, 6):
            energy = self.get_max_squares(self.x_position, self.y_position, e)
            if energy > max_squares:
                max_squares = energy

        return max_squares

    def get_max_squares(self, i, j, energy):
        max_visible_squares = 0
        max_visible_squares += self.squares_loop(i - 1, j, "N", energy)
        max_visible_squares += self.squares_loop(i + 1, j, "S", energy)
        max_visible_squares += self.squares_loop(i, j - 1, "W", energy)
        max_visible_squares += self.squares_loop(i, j + 1, "E", energy)

        return max_visible_squares

    def squares_loop(self, i, j, direction, energy):
        square_count = 0
        while energy != 0 and 20 > i >= 0 and 20 > j >= 0:
            if self._map.surface[j][i] == 1:
                break
            square_count += 1
            energy -= 1
            if direction == "N":
                i -= 1
            elif direction == "S":
                i += 1
            elif direction == "W":
                j -= 1
            elif direction == "E":
                j += 1

        return square_count


class Ant:
    def __init__(self, steps):
        self.steps = steps
        self._sensor_list = []
        self.visited_sensors = []
        self.path = []
        self.current_sensor = 0
        self.sensor_squares = {}

    def fitness(self):
        score = sum(self.sensor_squares.values())
        return score

    def setPath(self, path):
        for position in path:
            self.path.append(position)

    def choose_sensor(self, probabilities):
        random_prob = random()
        for i in range(0, len(probabilities) - 1):
            if probabilities[i] > random_prob >= probabilities[i + 1]:
                return i
        return len(probabilities) - 1

class AntColony:
    def __init__(self, size, energy):
        self._size = size
        self.ants = [Ant(energy) for _ in range(size)]

    def get_colony(self):
        return self.ants

    def reorganizeColony(self, energy):
        self.ants = [Ant(energy) for _ in range(self._size)]
