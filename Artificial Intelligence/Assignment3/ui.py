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
        print("Map loaded !")

    def save_map(self):
        map = input("Map name -> ")
        self._repository.cmap.saveMap(map)
        print("Map saved !")

    def visualize_map(self):
        pygame.init()
        running = True

        while running:
            screen = pygame.display.set_mode((400, 400))
            screen.fill(WHITE)
            screen.blit(self._controller.show_map(image(self._repository.cmap)), (0, 0))
            pygame.display.flip()

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False

                    break
            pygame.display.update()

        time.sleep(2)
        pygame.quit()

    def ea_menu(self):
        print("1. parameters setup")
        print("2. run the solver")
        print("3. visualise the statistics")
        print("4. view the drone moving on a path")
        print("5. exit")

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
        movingDrone(self._repository.cmap, self._path, 0.01)
        print("Solved !")

    def visualise_statistics(self):
        x = []
        average = []
        deviations = []

        for i in range(len(self._current_statistics)):
            x.append(i)
            average.append(self._current_statistics[i][0])
            deviations.append(self._current_statistics[i][1])
        plt.plot(x, average)
        plt.plot(x, deviations)
        plt.show()

    def moving_drone(self):
        movingDrone(self._repository.cmap, self._path, 0.8)
        print("Drone moved !")

    def run(self):
        key = 99
        map_options = {1: self.create_map, 2: self.load_map, 3: self.save_map, 4: self.visualize_map}
        ea_options = {1: self.setup_parameters, 2: self.run_solver, 3: self.visualise_statistics, 4: self.moving_drone}

        self.map_menu()
        key = int(input("-> "))
        while key != 5:
            map_options[key]()
            self.map_menu()
            key = int(input("-> "))

        key = 99

        self.ea_menu()
        key = int(input("-> "))
        while key != 5:
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
