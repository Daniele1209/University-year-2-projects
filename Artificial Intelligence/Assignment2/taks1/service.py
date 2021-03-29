import heapq
from numpy import sqrt

from queue import PriorityQueue
import pygame

GREEN = (0, 255, 0)


class Service():

    # Manhattan distance
    def heuristic(self, a, b):
        return abs(a[0] - b[0]) + abs(a[1] - b[1])

    def searchHill(self, mapM, initialX, initialY, finalX, finalY):
            path = []
            positions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
            start = (initialX, initialY)
            goal = (finalX, finalY)

            current = start

            if start == goal:
                return path

            while current != goal:
                current_cost = self.heuristic(current, goal)

                for i, j in positions:
                    neighbor = (current[0] + i, current[1] + j)
                    if 0 <= neighbor[0] < mapM.surface.shape[0] and 0 <= neighbor[1] < mapM.surface.shape[1]:
                        cost = self.heuristic(neighbor, goal)

                        if cost < current_cost:
                            current = neighbor
                            path.append(current)
                            break

            return path

    def searchAStar(self, mapM, initialX, initialY, finalX, finalY):

        start = (initialX, initialY)
        goal = (finalX, finalY)

        positions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

        close_set = set()
        came_from = {}

        gscore = {start: 0}
        fscore = {start: self.heuristic(start, goal)}
        oheap = []

        heapq.heappush(oheap, (fscore[start], start))

        while oheap:
            current = heapq.heappop(oheap)[1]

            if current == goal:
                data = []

                while current in came_from:
                    data.append(current)
                    current = came_from[current]

                return data

            close_set.add(current)

            for i, j in positions:

                neighbor = current[0] + i, current[1] + j
                tentative_g_score = gscore[current] + 1

                if 0 <= neighbor[0] < mapM.surface.shape[0]:
                    if 0 <= neighbor[1] < mapM.surface.shape[1]:
                        if mapM.surface[neighbor[0]][neighbor[1]] == 1:
                            continue
                    else:
                        # array bound y walls
                        continue

                else:
                    # array bound x walls
                    continue

                if neighbor in close_set and tentative_g_score >= gscore.get(neighbor, 0):
                    continue

                if tentative_g_score < gscore.get(neighbor, 0) or neighbor not in [i[1] for i in oheap]:
                    came_from[neighbor] = current
                    gscore[neighbor] = tentative_g_score
                    fscore[neighbor] = tentative_g_score + self.heuristic(neighbor, goal)
                    heapq.heappush(oheap, (fscore[neighbor], neighbor))

        return False

    def searchGreedy(self, mapM, initialX, initialY, finalX, finalY):
        path = {}
        visited = []
        positions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
        final = (finalX, finalY)
        queue = []

        queue.append((0, (initialX, initialY)))

        while queue:

            current = queue.pop()[1]
            visited.append((current[0], current[1]))

            if current == final:
                data = []

                while current in path:
                    data.append(current)
                    current = path[current]

                return data

            minList = []

            for i, j in positions:

                neighbor = (current[0] + i, current[1] + j)

                if 0 <= neighbor[0] < mapM.surface.shape[0] and 0 <= neighbor[1] < mapM.surface.shape[1] and \
                        mapM.surface[neighbor[0]][neighbor[1]] == 0 and (neighbor[0], neighbor[1]) not in visited:
                        cost = self.heuristic(neighbor, final)
                        minList.append((cost, neighbor))

            minList.sort()
            if len(minList):
                minElem = minList.pop(0)
                path[minElem[1]] = current
                queue.append(minElem)
            else:
                raise Exception()

    def dummysearch(self):
        # example of some path in test1.map from [5,7] to [7,11]
        return [[5, 7], [5, 8], [5, 9], [5, 10], [5, 11], [6, 11], [7, 11]]

    def displayWithPath(self, image, path):
        mark = pygame.Surface((20, 20))
        mark.fill(GREEN)
        for move in path:
            image.blit(mark, (move[1] * 20, move[0] * 20))

        return image
