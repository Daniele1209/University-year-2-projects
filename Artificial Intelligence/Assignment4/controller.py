import heapq
import time

import pygame

from gui import movingDrone
from repository import *
from domain import *
from utils import *
from tqdm import tqdm


class controller():
    def __init__(self, repository):
        self._repository = repository
        self._statistics = []
        self._pheromones = None
        self._visibility = None
        self._ant_colony_size = 100
        self._sensorPath_list = []
        self._probabilities = []

        self.statistics_history = []
        self.seeds = None
        self.nb_steps = None
        self.population_size = None
        self.nb_iterations = None
        self.crossover_probability = None
        self.mutation_probability = None
        self.current_iteration = 0
        self.sensor_positions = []
        self.sensor_list = []

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

    def get_sensor_list(self):
        return self.sensor_list

    def add_sensor_position(self, x, y, map):
        self.sensor_positions.append([x, y])
        sensorToAdd = Sensor(x, y, map)
        self.sensor_list.append(sensorToAdd)

    def get_sensor_positions(self):
        return self.sensor_positions

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
        parents = select[:len(select) // 2]
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
            seed(30 - i)
            population = self._repository.create_population([self.population_size, self.nb_steps])
            self._repository.add_population(population)
            self.run(i)

        end_time = time.time()
        print(f"Execution time: {end_time - start_time:0.5f}")

        return self._repository.get_first_path(), self._statistics, self.statistics_history

# ------------------- ACO TRAINING -------------------

    def run_aco_training(self):
        self.sensor_paths()
        self.setup_visibility_matrix()
        self.setup_pheromone_matrix()
        self._repository.add_colonies(self._ant_colony_size, self.nb_steps)
        bestAnt = None

        for iteration in tqdm(range(0, self.nb_iterations)):
            if iteration == 0:
                bestAnt = self.aco_iteration()
            else:
                currentAnt = self.aco_iteration()
                if len(bestAnt.path) >= len(currentAnt.path):
                    bestAnt = currentAnt

        return bestAnt

    def aco_iteration(self):
        first_done = False
        ant_colony = self._repository.get_last_colony()
        for ant in ant_colony.get_colony():
            visible_matrix = self._visibility.copy()

            while ant.steps > 0 and len(self.sensor_list) > len(ant.visited_sensors):
                for i in range(len(self.sensor_list)):
                    visible_matrix[i][ant.current_sensor] = 0

                self.probability_ranges(ant)
                if first_done:
                    nextSensor_probability = ant.choose_sensor(self._probabilities)
                    next_sensor = self.get_next_sensor(ant, nextSensor_probability)
                    if not next_sensor:
                        continue
                else:
                    next_sensor = 0
                    first_done = True

                path = self._sensorPath_list[ant.current_sensor][next_sensor]
                ant.setPath(path)
                ant.steps -= len(path)

                if ant.steps < 0:
                    ant.path = ant.path[0:len(ant.path) - abs(ant.steps)]
                    ant.steps = 0

                elif ant.steps > 0:
                    needed_energy = self.sensor_list[next_sensor].get_optimal()
                    ant.steps -= needed_energy

                    if ant.steps < 0:
                        old_energy = ant.steps + needed_energy
                        ant.sensor_squares[next_sensor] = \
                            self.sensor_list[next_sensor].seen_squares_energy[old_energy]
                        ant.energy = 0
                    else:
                        ant.sensor_squares[next_sensor] = \
                            self.sensor_list[next_sensor].seen_squares_energy[needed_energy]

                    ant.current_sensor = next_sensor
                    ant.visited_sensors.append(next_sensor)

        self.change_pheromone_matrix(ant_colony)
        chosen_ant = self.get_best_ant()
        ant_colony.reorganizeColony(self.nb_steps)
        return chosen_ant

    def get_next_sensor(self, ant, nextSensor_probability):
        current_chosen_sensor = 0
        nextSensor_probability += 1
        for i in range(1, len(self.sensor_list)):
            if i not in ant.visited_sensors:
                current_chosen_sensor += 1

                if current_chosen_sensor == nextSensor_probability:
                    return i

    def get_best_ant(self):
        fitness = -1
        best_ant = None
        for ant in self._repository.get_last_colony().ants:
            if ant.fitness() > fitness:
                fitness = ant.fitness()
                best_ant = ant
        return best_ant

    def probability_ranges(self, ant):
        number_list = []
        probability_list = []
        self._probabilities = []
        current_sensor = ant.current_sensor
        for i in range(1, len(self.sensor_list)):
            if i not in ant.visited_sensors:
                number_list.append(
                    self._pheromones[current_sensor][i] ** alpha * self._visibility[current_sensor][i] ** beta)

        denominator = sum(number_list)
        for number in number_list:
            probability_list.append(number / denominator)

        for i in range(len(probability_list)):
            self._probabilities.append(sum(probability_list[i:]))

    def setup_pheromone_matrix(self):
        self._pheromones = np.ones((len(self.sensor_list), len(self.sensor_list)))

    def setup_visibility_matrix(self):
        self._visibility = np.zeros((len(self.sensor_list), len(self.sensor_list)))

        for i in range(len(self.sensor_list)):
            for j in range(len(self.sensor_list)):
                if i != j:
                    self._visibility[i][j] = 1 / len(self._sensorPath_list[i][j])

        for i in range(0, len(self._sensorPath_list)):
            self._visibility[i][0] = 0

    def change_pheromone_matrix(self, ant_colony):
        for i in range(len(self.sensor_list)):
            for j in range(len(self.sensor_list)):
                self._pheromones[i][j] = (1 - coef) * self._pheromones[i][j]

        for ant in ant_colony.get_colony():
            visited = ant.visited_sensors
            for i in range(len(visited) - 1):
                self._pheromones[visited[i]][visited[i + 1]] += (1 / len(ant.path))

    def sensor_paths(self):
        self._sensorPath_list = [[] for _ in range(len(self.sensor_list))]
        for i in range(len(self.sensor_list)):
            for j in range(len(self.sensor_list)):
                self._sensorPath_list[i].append(
                    self.searchAStar(self._repository.cmap, self.sensor_list[i].x_position,
                                     self.sensor_list[i].y_position,
                                     self.sensor_list[j].x_position, self.sensor_list[j].y_position))

    def searchAStar(self, map, initialX, initialY, finalX, finalY):
        positions = ([-1, 0], [0, 1], [1, 0], [0, -1])

        open = []
        closed = []
        parent = {}

        start = [initialX, initialY]
        end = [finalX, finalY]
        parent[str(start)] = None
        parent[str(end)] = None

        totalDistance = np.zeros((map.n, map.m))
        startDistance = np.zeros((map.n, map.m))
        goalDistance = np.zeros((map.n, map.m))
        open.append(start)
        while len(open) > 0:
            open = self.sort_nodes(open, totalDistance)
            current_pos = open.pop(0)
            closed.append(current_pos)

            if current_pos == end:
                path = []
                while current_pos != start:
                    path.append([current_pos[0], current_pos[1]])
                    current_pos = parent[str(current_pos)]
                path.append([current_pos[0], current_pos[1]])
                return path[::-1]

            for i in range(0, 4):
                next_pos = [current_pos[0] + positions[i][0], current_pos[1] + positions[i][1]]
                if next_pos not in closed and 0 <= next_pos[0] < map.n and 0 <= next_pos[1] < map.m:
                    if map.surface[next_pos[1]][next_pos[0]] == 0:
                        parent[str(next_pos)] = current_pos
                        startDistance[next_pos[0]][next_pos[1]] = startDistance[current_pos[0]][current_pos[1]]
                        goalDistance[next_pos[0]][next_pos[1]] = 1
                        totalDistance[next_pos[0]][next_pos[1]] = startDistance[next_pos[0]][next_pos[1]] + goalDistance[next_pos[0]][next_pos[1]]

                        if self.add_open_position(open, next_pos, totalDistance):
                            open.append(next_pos)
        return None

    def add_open_position(self, open, new_node, totalDistance):
        for node in open:
            if node == new_node and totalDistance[node[0]][node[1]] <= totalDistance[new_node[0]][new_node[1]]:
                return False
        return True

    def sort_nodes(self, open, distance):
        list = open
        ordered = False
        while ordered == False:
            ordered = True
            for i in range(0, len(list) - 1):
                if distance[list[i][0]][list[i][1]] > distance[list[i + 1][0]][list[i + 1][1]]:
                    aux = list[i]
                    list[i] = list[i + 1]
                    list[i + 1] = aux
                    ordered = False
        return list
