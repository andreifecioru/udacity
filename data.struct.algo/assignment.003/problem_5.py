#!/usr/bin/env python

from ipywidgets import widgets
from IPython.display import display
from ipywidgets import interact


class TrieNode:
    """ Represents a single node in the Trie """

    def __init__(self):
        """ Initialize this node in the Trie """
        self.children = {}

    def insert(self, char):
        """ Add a child node in this Trie """
        if char not in self.children:
            self.children[char] = TrieNode()
        return self.children[char]

    def suffixes(self):
        ret_val = []

        # this is the recursive kernel for accumulating
        # suffixes.
        def _traverse(node, acc=''):
            nonlocal ret_val
            if not node.children:
                ret_val.append(acc)
            for c, n in node.children.items():
                _traverse(n, acc + c)
        
        _traverse(self)

        return ret_val


class Trie:
    """ The Trie itself containing the root node and insert/find functions """

    def __init__(self):
        """ Initialize this Trie (add a root node) """
        self.root = TrieNode()

    def insert(self, word):
        """ Add a word to the Trie """
        node = self.root
        for char in word:
            node = node.insert(char)

    def find(self, prefix):
        """ Find the Trie node that represents this prefix """
        node = self.root
        for char in prefix:
            if not node:
                return None
            node = node.children.get(char, None)

        return node


def f(prefix):
    if prefix != '':
        prefixNode = MyTrie.find(prefix)
        if prefixNode:
            print('\n'.join(prefixNode.suffixes()))
        else:
            print(prefix + " not found")
    else:
        print('')


if __name__ == "__main__":
    MyTrie = Trie()
    wordList = [
        "ant", "anthology", "antagonist", "antonym",
        "fun", "function", "factory",
        "trie", "trigger", "trigonometry", "tripod"
    ]

    for word in wordList:
        MyTrie.insert(word)

    interact(f,prefix='')
