#!/usr/bin/env python

from collections import deque

class Node:
    def __init__(self, value=None):
        self.value = value
        self.left = None
        self.right = None

class BT:

    def __init__(self, root=None):
        self.root = root
        self.traversal_modes = {
            'pre': BT._traverse_pre,
            'in': BT._traverse_in,
            'post': BT._traverse_post,
            'bfs': BT._traverse_bfs,
            'bfs2': BT._traverse_bfs2,
        }

    @staticmethod
    def _traverse_pre(node, f):
        if not node:
            return
        f(node.value)
        BT._traverse_pre(node.left, f)
        BT._traverse_pre(node.right, f)

    @staticmethod
    def _traverse_in(node, f):
        if not node:
            return
        BT._traverse_in(node.left, f)
        f(node.value)
        BT._traverse_in(node.right, f)

    @staticmethod
    def _traverse_post(node, f):
        if not node:
            return
        BT._traverse_post(node.left, f)
        BT._traverse_post(node.right, f)
        f(node.value)

    @staticmethod
    def _traverse_bfs(node, f):
        def _traverse(nodes):
            if not nodes:
                return
            next_lvl_nodes = []
            for node in nodes:
                f(node.value)
                if node.left:
                    next_lvl_nodes.append(node.left)
                if node.right:
                    next_lvl_nodes.append(node.right)
            _traverse(next_lvl_nodes)

        _traverse([node])

    @staticmethod
    def _traverse_bfs2(node, f):
        q = deque()
        q.appendleft(node)

        while q:
            node = q.pop()
            f(node.value)
            if node.left:
                q.appendleft(node.left)
            if node.right:
                q.appendleft(node.right)


    def foreach(self, f, method='pre'):
        self.traversal_modes.get(method, BT._traverse_pre)(self.root, f)


if __name__ == '__main__':
    # constructing the tree
    bt = BT()
    bt.root = Node('A')
    bt.root.left = Node('B')
    bt.root.right = Node('C')
    bt.root.left.left = Node('D')
    bt.root.left.right = Node('E')
    bt.root.right.right = Node('F')

    print('Traversal pre-order:')
    bt.foreach(print, method='pre')

    print('Traversal in-order:')
    bt.foreach(print, method='in')

    print('Traversal post-order:')
    bt.foreach(print, method='post')

    print('Traversal bfs:')
    bt.foreach(print, method='bfs')

    print('Traversal bfs2:')
    bt.foreach(print, method='bfs2')