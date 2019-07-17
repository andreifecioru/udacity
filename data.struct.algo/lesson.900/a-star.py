#!/usr/bin/env python

from collections import namedtuple
from operator import attrgetter

from map import Map, ROMANIA_MAP_DATA


Path = namedtuple('Path', ['name', 'distance', 'path'])

class PathFinder:
    EXPLORED_COLOR = 'grey'
    FRONTIER_COLOR = 'green'
    START_COLOR = 'blue'
    GOAL_COLOR = 'yellow'
    CURRENT_COLOR = 'red'

    DEFAULT_SIZE = 40
    GOAL_SIZE = 100
    CURRENT_SIZE = 80

    def __init__(self, map, start, goal):

        self.map = map
        self.start = start
        self.goal = goal
        self.explored = {self.start}
        self.current = Path(self.start, 0, [self.start])
        self.frontier = {}
        self.solution = None
        self._update_map()

    def explore(self, interactive=False):
        if self.current.name != self.goal:
            # update the frontier nodes
            for city, attrs in self.map.G[self.current.name].items():
                # ignore all nodes that were already explored
                if city in self.explored:
                    continue

                new_frontier_city = Path(name=city, 
                                                distance=self.current.distance + attrs['distance'], 
                                                path=self.current.path[:] + [city])
                if city not in self.frontier or new_frontier_city.distance < self.frontier[city].distance:
                    self.frontier[city] = new_frontier_city

        # update explored nodes
        self.explored.add(self.current.name)

        # update current node
        self.current = min(self.frontier.values(), key=attrgetter('distance'))

        # update the solution
        if self.current.name == self.goal:
            self.solution = self.current if not self.solution or self.current.distance < self.solution.distance else self.solution

        # remove the new current node from the frontier nodes
        del self.frontier[self.current.name]
            
        self._update_map()

        if interactive:
            input('Press a key...')

        # we're not done while there are still nodes left in the frontier 
        return not self.frontier

    def _update_map(self):
        self.map.set_city_attrs(self.start, {'color': PathFinder.START_COLOR})
        self.map.set_city_attrs(self.goal, {'color': PathFinder.GOAL_COLOR, 'size': PathFinder.GOAL_SIZE})

        for city in self.explored:
            self.map.set_city_attrs(city, {
                'color': PathFinder.EXPLORED_COLOR,
                'size': PathFinder.DEFAULT_SIZE,
            })

        for city in self.frontier.values():
            self.map.set_city_attrs(city.name, {
                'color': PathFinder.FRONTIER_COLOR, 
                'size': PathFinder.DEFAULT_SIZE,
                'label': f'{city.name} ({city.distance})'
            })

        self.map.set_city_attrs(self.current.name, {
            'color': PathFinder.CURRENT_COLOR,
            'size': PathFinder.CURRENT_SIZE,
            'label': f'{self.current.name} ({self.current.distance})',
        })
        

if __name__ == '__main__':
    Map.initialize()
    pf = PathFinder(Map(ROMANIA_MAP_DATA), 'Arad', 'Bucuresti')

    while True:
        is_done = pf.explore(interactive=False)

        if is_done:
            break

    print(pf.solution)

    input('Press [enter] to continue...')