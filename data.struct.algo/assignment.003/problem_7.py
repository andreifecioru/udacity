#!/usr/bin/env python


class RouteTrieNode:
    """ Represents a single node in the Trie """
    
    def __init__(self, handler=None):
        self.handler = handler
        self.children = {}

    def insert(self, path_element, handler=None):
        """ Add a child node in this Trie """
        if path_element not in self.children:
            self.children[path_element] = RouteTrieNode(handler)
        return self.children[path_element]


class RouteTrie:
    def __init__(self, root_handler, not_found_handler):
        """ Initialize this Trie (add a root node) and a 'not_found' generic handler."""
        self.not_found_handler = not_found_handler
        self.root = RouteTrieNode(root_handler)

    def insert(self, path_elements, handler):
        """ Add a word to the Trie """
        node = self.root
        # traverse the input path elements and create the corresponding nodes (if necessary)
        for path_element in path_elements[:-1]:
            node = self.root.insert(path_element)
        # install the handler only on the node corresponding to the last path element
        node.insert(path_elements[-1], handler)

    def find(self, path_elements):
        """ Find the Trie node that represents the input path (return the corresponding letter)."""
        node = self.root
        for path_element in path_elements:
            node = node.children.get(path_element, None)
            if node:
                continue
            else:
                return self.not_found_handler
        return node.handler or self.not_found_handler


class Router:
    '''The Router class will wrap the Trie and handle'''

    def __init__(self, root_handler, not_found_handler):
        self.router = RouteTrie(root_handler, not_found_handler)

    def add_handler(self, path, handler):
        path_elements = self.split_path(path)
        self.router.insert(path_elements, handler)

    def lookup(self, path):
        path_elements = self.split_path(path)
        return self.router.find(path_elements)

    def split_path(self, path):
        # make sure to get rid of empty path elements
        # this helps us to deal with paths like /home/about/ or /home//about
        return [path_element.strip()
                for path_element in path.split('/')
                if path_element.strip()]


if __name__ == '__main__':
    # create the router and add a route
    # remove the 'not found handler' if you did not implement this
    router = Router("root handler", "not found handler")
    router.add_handler("/home/about", "about handler")  # add a route

    # some lookups with the expected output
    print(router.lookup("/"))  # prints 'root handler'
    print(router.lookup("/home")) # prints 'not found handler' 
    print(router.lookup("/home/about"))  # prints 'about handler'
    print(router.lookup("/home/about/")) # prints 'about handler' 
    print(router.lookup("/home/about/me")) # prints 'not found handler'
