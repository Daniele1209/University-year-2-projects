import time
from random import randint

import pygame

from taks1.domain import Map, Drone
from taks1.service import Service
from pygame.locals import *

WHITE = (255, 255, 255)

class ui():

    def __init__(self):
        self.m = Map()
        self.service = Service()
        self.d = Drone(0, 0)

    def selectionMenu(self):
        print("Make a selection: aStar")
        print("                  Greedy")
        option = input("--> ")

        return option

    def main(self):

        option = self.selectionMenu()

        self.m.randomMap()
        self.m.saveMap("test2.map")
        #self.m.loadMap("test1.map")

        # initialize the pygame module
        pygame.init()
        # load and set the logo
        logo = pygame.image.load("logo32x32.png")
        pygame.display.set_icon(logo)
        pygame.display.set_caption("Path in simple environment")

        # we position the drone somewhere in the area
        x = randint(0, 19)
        y = randint(0, 19)

        # create drona
        d = Drone(x, y)

        #Get a random position as destination and chef if it is empty
        final_x = randint(0, 19)
        final_y = randint(0, 19)
        while self.m.surface[final_x][final_y] != 0:
            final_x = randint(0, 19)
            final_y = randint(0, 19)

        # create a surface on screen that has the size of 400 x 480
        screen = pygame.display.set_mode((400, 400))
        screen.fill(WHITE)

        # define a variable to control the main loop
        running = True

        # main loop
        while running:
            # event handling, gets all event from the event queue
            for event in pygame.event.get():
                # only do something if the event is of type QUIT
                if event.type == pygame.QUIT:
                    # change the value to False, to exit the main loop
                    running = False

                #if event.type == KEYDOWN:
                #    d.move(self.m)  # this call will be erased

            screen.blit(d.mapWithDrone(self.m.image(), final_x, final_y), (0, 0))
            pygame.display.flip()

            if option == "aStar":
                path = self.service.searchAStar(self.m, x, y, final_x, final_y)
            elif option == "Greedy":
                path = self.service.searchGreedy(self.m, x, y, final_x, final_y)

            if path:
                screen.blit(self.service.displayWithPath(self.m.image(), path), (0, 0))

        pygame.display.flip()
        time.sleep(2)

        pygame.quit()