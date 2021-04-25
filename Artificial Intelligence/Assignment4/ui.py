# -*- coding: utf-8 -*-


# imports
import matplotlib.pyplot as plt
from gui import *
from controller import *
from repository import *
from domain import *

class ui:
    def __init__(self, controller, repo):
        self._controller = controller
        self._repository = repo

        self._path = []
        self._result = []
        self._current_statistics = []

    def map_menu(self):
        print("1. create random map")
        print("2. load a map")
        print("3. save a map")
        print("4. visualise map")
        print("5. -> EA option")

    def create_map(self):
        self._repository.cmap.randomMap()
        self._repository.droneSpawnPosition()
        print("Map created !")

    def load_map(self):
        map = input("Map name -> ")
        self._repository.cmap.loadMap(map)

        file = open("sensors", "r")
        for line in file:
            sensor_position = line.strip()
            positions = sensor_position.split(" ")
            x_position = int(positions[0])
            y_position = int(positions[1])
            self._controller.add_sensor_position(x_position, y_position, self._repository.cmap)

        file.close()
        print("Map loaded !")

    def save_map(self):
        map = input("Map name -> ")
        self._repository.cmap.saveMap(map)
        print("Map saved !")

    def visualize_map(self):
        pygame.init()
        running = True

        sensor = pygame.image.load("sensor_small.png")

        while running:
            screen = pygame.display.set_mode((400, 400))
            screen.fill(WHITE)
            screen.blit(self._controller.show_map(image(self._repository.cmap)), (0, 0))

            sensor_positions = self._controller.get_sensor_positions()
            for position in sensor_positions:
                screen.blit(sensor, (position[0] * 20, position[1] * 20))

            pygame.display.flip()

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False

                    break
            pygame.display.update()

        time.sleep(2)
        pygame.quit()
        path = [[0, 0]]
        movingDrone(self._repository.cmap, path, self._controller, 0.1)

    def ea_menu(self):
        print("1. parameters setup")
        print("2. run the solver")
        print("3. visualise the statistics")
        print("4. view the drone moving on a path")
        print("5. run ACO")
        print("6. exit")

    def setup_parameters(self):
        file = open("configuration", "r")
        file.readline()
        self._controller.random_drone_position()
        self._controller.nb_of_steps(int(file.readline()))
        self._controller.population(int(file.readline()))
        self._controller.iterations(int(file.readline()))
        self._controller.set_crossover_probability(float(file.readline()))
        self._controller.set_mutation_probability(float(file.readline()))
        self._controller.set_seed_number(int(file.readline()))

        file.close()
        print("Setup done !")

    def run_solver(self):
        self._path, self._result, self._current_statistics = self._controller.solver()
        movingDrone(self._repository.cmap, self._path, self._controller, 0.01)
        print("Solved !")

    def visualise_statistics(self):
        x = []
        average = []
        deviations = []

        for i in range(len(self._current_statistics)):
            x.append(i)
            average.append(self._current_statistics[i][0])
            deviations.append(self._current_statistics[i][1])
        plt.plot(x, average, label="Average", linewidth=4)
        plt.plot(x, deviations, label="Stdev", linestyle='--')
        plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left', borderaxespad=0.)
        plt.show()

    def moving_drone(self):
        movingDrone(self._repository.cmap, self._path, self._controller, 1)
        print("Drone moved !")

    def aco_training(self):
        ant = self._controller.run_aco_training()
        self._path = ant.path

        sensor_list_energy = ant.sensor_squares
        for sensor, energy in sensor_list_energy.items():
            resulted_sensor = self._controller.sensor_list[sensor]
            for key, value in resulted_sensor.seen_squares_energy.items():
                if value == energy:
                    energy = key
                    break
            resulted_sensor.add_energy(energy)

        movingDrone(self._repository.cmap, self._path, self._controller, 0.1)
        print("Solved !")

    def run(self):
        key = 99
        map_options = {1: self.create_map, 2: self.load_map, 3: self.save_map, 4: self.visualize_map}
        ea_options = {1: self.setup_parameters, 2: self.run_solver, 3: self.visualise_statistics, 4: self.moving_drone, 5: self.aco_training}

        self.map_menu()
        key = int(input("-> "))
        while key != 5:
            map_options[key]()
            self.map_menu()
            key = int(input("-> "))

        key = 99

        self.ea_menu()
        key = int(input("-> "))
        while key != 6:
            ea_options[key]()
            self.ea_menu()
            key = int(input("-> "))

        print("Goodbye !")

# create a menu
#   1. map options:
#         a. create random map
#         b. load a map
#         c. save a map
#         d visualise map
#   2. EA options:
#         a. parameters setup
#         b. run the solver
#         c. visualise the statistics
#         d. view the drone moving on a path
#              function gui.movingDrone(currentMap, path, speed, markseen)
#              ATENTION! the function doesn't check if the path passes trough walls
