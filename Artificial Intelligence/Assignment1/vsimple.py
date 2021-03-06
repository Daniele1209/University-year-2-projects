# import the pygame module, so you can use it
import pickle, pygame, sys
from pygame.locals import *
from random import random, randint
import numpy as np
import time

# Creating some colors
BLUE = (0, 0, 255)
GRAYBLUE = (50, 120, 120)
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
CYAN = (0, 255, 255)

# define directions
UP = 0
DOWN = 2
LEFT = 1
RIGHT = 3

# define indexes variations
v = [[-1, 0], [1, 0], [0, 1], [0, -1]]


class Environment:
    def __init__(self):
        self.__n = 20
        self.__m = 20
        self.__surface = np.zeros((self.__n, self.__m))

    def randomMap(self, fill=0.2):
        for i in range(self.__n):
            for j in range(self.__m):
                if random() <= fill:
                    self.__surface[i][j] = 1

    def __str__(self):
        string = ""
        for i in range(self.__n):
            for j in range(self.__m):
                string = string + str(int(self.__surface[i][j]))
            string = string + "\n"
        return string

    def readUDMSensors(self, x, y):
        readings = [0, 0, 0, 0]
        # UP 
        xf = x - 1
        while (xf >= 0) and (self.__surface[xf][y] == 0):
            xf = xf - 1
            readings[UP] = readings[UP] + 1
        # DOWN
        xf = x + 1
        while (xf < self.__n) and (self.__surface[xf][y] == 0):
            xf = xf + 1
            readings[DOWN] = readings[DOWN] + 1
        # LEFT
        yf = y + 1
        while (yf < self.__m) and (self.__surface[x][yf] == 0):
            yf = yf + 1
            readings[LEFT] = readings[LEFT] + 1
        # RIGHT
        yf = y - 1
        while (yf >= 0) and (self.__surface[x][yf] == 0):
            yf = yf - 1
            readings[RIGHT] = readings[RIGHT] + 1

        return readings

    def saveEnvironment(self, numFile):
        with open(numFile, 'wb') as f:
            pickle.dump(self, f)
            f.close()

    def loadEnvironment(self, numfile):
        with open(numfile, "rb") as f:
            dummy = pickle.load(f)
            self.__n = dummy.__n
            self.__m = dummy.__m
            self.__surface = dummy.__surface
            f.close()

    def image(self, colour=BLUE, background=WHITE):
        imagine = pygame.Surface((420, 420))
        brick = pygame.Surface((20, 20))
        brick.fill(BLUE)
        imagine.fill(WHITE)
        for i in range(self.__n):
            for j in range(self.__m):
                if (self.__surface[i][j] == 1):
                    imagine.blit(brick, (j * 20, i * 20))

        return imagine


class DMap:
    def __init__(self):
        self.__n = 20
        self.__m = 20
        self.surface = np.zeros((self.__n, self.__m))
        for i in range(self.__n):
            for j in range(self.__m):
                self.surface[i][j] = -1

    def markDetectedWalls(self, e, x, y):
        #   To DO
        # mark on this map the walls that you detect
        wals = e.readUDMSensors(x, y)
        i = x - 1
        if wals[UP] > 0:
            while ((i >= 0) and (i >= x - wals[UP])):
                self.surface[i][y] = 0
                i = i - 1
        if (i >= 0):
            self.surface[i][y] = 1

        i = x + 1
        if wals[DOWN] > 0:
            while ((i < self.__n) and (i <= x + wals[DOWN])):
                self.surface[i][y] = 0
                i = i + 1
        if (i < self.__n):
            self.surface[i][y] = 1

        j = y + 1
        if wals[LEFT] > 0:
            while ((j < self.__m) and (j <= y + wals[LEFT])):
                self.surface[x][j] = 0
                j = j + 1
        if (j < self.__m):
            self.surface[x][j] = 1

        j = y - 1
        if wals[RIGHT] > 0:
            while ((j >= 0) and (j >= y - wals[RIGHT])):
                self.surface[x][j] = 0
                j = j - 1
        if (j >= 0):
            self.surface[x][j] = 1

        return None

    def image(self, x, y, visited_list):

        imagine = pygame.Surface((420, 420))
        brick = pygame.Surface((20, 20))
        visited = pygame.Surface((20, 20))
        empty = pygame.Surface((20, 20))
        empty.fill(WHITE)
        brick.fill(BLACK)
        visited.fill(CYAN)
        imagine.fill(GRAYBLUE)

        for i in range(self.__n):
            for j in range(self.__m):
                if (self.surface[i][j] == 1):
                    imagine.blit(brick, (j * 20, i * 20))
                elif (self.surface[i][j] == 0):
                    imagine.blit(empty, (j * 20, i * 20))

        for i in visited_list:
            imagine.blit(visited, (i[1] * 20, i[0] * 20))

        drona = pygame.image.load("drona.png")
        imagine.blit(drona, (y * 20, x * 20))
        return imagine


class Drone:
    def __init__(self, x, y, e):
        self.x = x
        self.y = y
        self.visited = []
        self.stack = []
        self.e = e

    def move(self, detectedMap):
        pressed_keys = pygame.key.get_pressed()
        if self.x > 0:
            if pressed_keys[K_UP] and detectedMap.surface[self.x - 1][self.y] == 0:
                self.x = self.x - 1
        if self.x < 19:
            if pressed_keys[K_DOWN] and detectedMap.surface[self.x + 1][self.y] == 0:
                self.x = self.x + 1

        if self.y > 0:
            if pressed_keys[K_LEFT] and detectedMap.surface[self.x][self.y - 1] == 0:
                self.y = self.y - 1
        if self.y < 19:
            if pressed_keys[K_RIGHT] and detectedMap.surface[self.x][self.y + 1] == 0:
                self.y = self.y + 1

    # Function used for movement
    def moveDSF(self, detectedMap):
        # Append the current position to the visited list
        self.visited.append([self.x, self.y])

        # Info about the walls from UDM sensors -> no need anymore
        # walls = self.e.readUDMSensors(row, column)

        # Check the positions, use the detectedMap to check for empty position and we check
        # if the position is not in the visited list yet

        if self.x > 0  and detectedMap.surface[self.x - 1][self.y] == 0 and not [self.x - 1, self.y] in self.visited:
            self.stack.append([self.x, self.y])
            self.x = self.x - 1
            self.visited.append([self.x, self.y])

        elif self.x < 19 and detectedMap.surface[self.x + 1][self.y] == 0 and not [self.x + 1, self.y] in self.visited:
            self.stack.append([self.x, self.y])
            self.x = self.x + 1
            self.visited.append([self.x, self.y])


        elif self.y > 0 and detectedMap.surface[self.x][self.y - 1] == 0 and not [self.x, self.y - 1] in self.visited:
            self.stack.append([self.x, self.y])
            self.y = self.y - 1
            self.visited.append([self.x, self.y])


        elif self.y < 19 and detectedMap.surface[self.x][self.y + 1] == 0 and not [self.x, self.y + 1] in self.visited:
            self.stack.append([self.x, self.y])
            self.y = self.y + 1
            self.visited.append([self.x, self.y])

        elif len(self.stack) != 0:
            self.x, self.y = self.stack.pop()


# define a main function
def main():
    # we create the environment
    e = Environment()
    e.loadEnvironment("test2.map")
    # print(str(e))

    # we create the map
    m = DMap()

    # initialize the pygame module
    pygame.init()
    # load and set the logo
    logo = pygame.image.load("logo32x32.png")
    pygame.display.set_icon(logo)
    pygame.display.set_caption("drone exploration")

    # we position the drone somewhere in the area
    x = randint(0, 19)
    y = randint(0, 19)

    # cream drona
    d = Drone(x, y, e)

    # Keep track of the time and the drone moves
    moves = 0
    start_time = time.perf_counter()

    # create a surface on screen that has the size of 800 x 480
    screen = pygame.display.set_mode((800, 400))
    screen.fill(WHITE)
    screen.blit(e.image(), (0, 0))

    # define a variable to control the main loop
    running = True

    # main loop
    while running:
        # event handling, gets all event from the event queue
        # for event in pygame.event.get():
        # only do something if the event is of type QUIT
        # if event.type == pygame.QUIT:
        # change the value to False, to exit the main loop
        # running = False
        # if event.type == KEYDOWN:
        # use this function instead of move

        moves += 1

        time.sleep(.05)
        d.moveDSF(m)

        m.markDetectedWalls(d.e, d.x, d.y)
        screen.blit(m.image(d.x, d.y, d.visited), (400, 0))
        pygame.display.flip()

        if len(d.stack) == 0 and len(d.visited) != 1:
            print("done")
            running = False

    pygame.quit()

    # Keeping a high score for the best times that we get nad printing them in a file

    end_time = time.perf_counter()
    finished_time = end_time - start_time
    print(f"Finished exploring in {finished_time:0.4f} seconds and {moves} moves")

    # Declare a dictionary of high scores
    score_list = {}

    # Open the high score file for reading
    file = open("scores.txt", "r")

    # Getting trough the high score file and prepare for sorting the scores
    for score in file.readlines():
        score = score.strip()
        sc = score.split(" ")
        score_list[sc[2]] = int(sc[1])

    file.close()
    score_list[str(finished_time)[:6]] = int(moves)

    # Now we get to the sorting part
    sorted_list = sorted(score_list.items(), key=lambda item: item[0], reverse=False)

    # And, at the end we write the high scores back into the file
    file = open("scores.txt", "w")

    index = 1
    for score in sorted_list:
        if index < 11:
            file.write(str(index) + ". " + str(score_list[score[0]]) + " " + str(score[0]) + "\n")
            index += 1
        else:
            break

    file.close()


# run the main function only if this module is executed as the main script
# (if you import this as a module then nothing is executed)
if __name__ == "__main__":
    # call the main function
    main()
