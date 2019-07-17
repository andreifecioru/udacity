#!/usr/bin/env python

import networkx as nx
import matplotlib.pyplot as plt

DEFAULT_COLOR = 'black'
DEFAULT_SIZE = 40

class MapData:
    def __init__(self, cities, roads, default_size=DEFAULT_SIZE, default_color=DEFAULT_COLOR):
        self.cities = cities
        self.roads = roads
        for city, attrs in self.cities.items():
            attrs['color'] = default_color
            attrs['size'] = default_size
            attrs['label'] = city


ROMANIA_MAP_DATA = MapData(
    cities={
        'Oradea' : {'pos': (10, 99)},
        'Zerind' : {'pos': (7, 85)},
        'Arad' : {'pos': (5, 70)},
        'Timisoara' : {'pos': (5, 45)},
        'Lugoj' : {'pos': (15, 35)},
        'Mehadia' : {'pos': (17, 25)},
        'Drobeta' : {'pos': (16, 5)},
        'Craiova' : {'pos': (27, 2)},
        'Ram.V' : {'pos': (25, 39)},
        'Sibiu' : {'pos': (22, 60)},
        'Pitesti' : {'pos': (35, 30)},
        'Fagaras' : {'pos': (32, 58)},
        'Giurgiu' : {'pos': (42, 1)},
        'Bucuresti' : {'pos': (45, 15)},
        'Urziceni' : {'pos': (55, 25)},
        'Neamt' : {'pos': (50, 85)},
        'Iasi' : {'pos': (60, 77)},
        'Vaslui' : {'pos': (65, 60)},
        'Hirsova' : {'pos': (70, 25)},
        'Eforie' : {'pos': (85, 10)},
    },

    roads = [
        ('Oradea', 'Zerind', {'distance': 71}),
        ('Oradea', 'Sibiu', {'distance': 151}),
        ('Zerind', 'Arad', {'distance': 75}),
        ('Arad', 'Timisoara', {'distance': 118}),
        ('Arad', 'Sibiu', {'distance': 140}),
        ('Timisoara', 'Lugoj', {'distance': 111}),
        ('Lugoj', 'Mehadia', {'distance': 70}),
        ('Mehadia', 'Drobeta', {'distance': 75}),
        ('Craiova', 'Drobeta', {'distance': 120}),
        ('Sibiu', 'Ram.V', {'distance': 80}),
        ('Craiova', 'Ram.V', {'distance': 146}),
        ('Pitesti', 'Ram.V', {'distance': 97}),
        ('Pitesti', 'Craiova', {'distance': 138}),
        ('Sibiu', 'Fagaras', {'distance': 99}),
        ('Bucuresti', 'Fagaras', {'distance': 211}),
        ('Bucuresti', 'Giurgiu', {'distance': 90}),
        ('Bucuresti', 'Urziceni', {'distance': 85}),
        ('Bucuresti', 'Pitesti', {'distance': 101}),
        ('Hirsova', 'Urziceni', {'distance': 98}),
        ('Vaslui', 'Urziceni', {'distance': 142}),
        ('Vaslui', 'Iasi', {'distance': 92}),
        ('Neamt', 'Iasi', {'distance': 87}),
        ('Hirsova', 'Eforie', {'distance': 86}),
    ]
)


class Map:
    @staticmethod
    def initialize():
        plt.figure(figsize=(9, 9))
        plt.ion()
        plt.show()

    def __init__(self, map_data):
        self.map_data = map_data
        self.G = nx.Graph()
        self.G.add_nodes_from(self.map_data.cities.keys())
        self.G.add_edges_from(self.map_data.roads)
        self.draw()

    def draw(self):
        plt.clf()
        self._draw()
        plt.draw()
        plt.pause(0.001)

    def set_city_attrs(self, city, attrs):
        for k, v in attrs.items():
            self.map_data.cities[city][k] = v
        self.draw()

    def _draw(self):
        positions = {}
        color_map = []
        node_sizes = []
        label_pos = {}
        label_names = {}

        for city in self.G:
            city_pos = self.map_data.cities[city]['pos']
            positions[city] = city_pos
            color_map.append(self.map_data.cities[city]['color'])
            node_sizes.append(self.map_data.cities[city]['size'])
            label_pos[city] = (city_pos[0], city_pos[1] + 2)
            label_names[city] = self.map_data.cities[city]['label']

        nx.draw(self.G, pos=positions, node_size=node_sizes, node_color=color_map)
        nx.draw_networkx_labels(self.G, pos=label_pos, labels=label_names, font_size=8)

        edge_labels = {}
        for src, dst, attrs in self.map_data.roads:
            edge_labels[(src, dst)] = attrs['distance']
        nx.draw_networkx_edge_labels(self.G, pos=positions, edge_labels=edge_labels, font_color='grey', font_size=8)


if __name__ == '__main__':
    Map.initialize()

    colors = ['red', 'black', 'yellow', 'grey', 'green', 'blue']
    map = Map(ROMANIA_MAP_DATA)

    while True:
        color = colors.pop(0)
        colors.append(color)
        Map.default_color = color
        map.set_city_attrs('Bucuresti', {'color': color, 'size': 80})
        input('Press a key...')
