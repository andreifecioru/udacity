#!/usr/bin/env python

from collections import deque

class Node:
    def __init__(self, value=None):
        self.value = value
        self.left = None
        self.right = None

class BST:
    def __init__(self, root=None):
        self.root = root
        self.traversal_modes = {
            'pre': BST._traverse_pre,
            'in': BST._traverse_in,
            'post': BST._traverse_post,
            'bfs': BST._traverse_bfs,
            'bfs2': BST._traverse_bfs2,
        }

        self.insertion_modes = {
            'iter': BST._insert_iter,
            'rec': BST._insert_rec,
        }

    @staticmethod
    def _insert_iter(node, value):
        new_node = Node(value)
        if not node:
            node = new_node
            return node

        _node = node
        while True:
            if value < _node.value:
                if _node.left:
                    _node = _node.left
                else:
                    _node.left = new_node
                    break
            elif value > _node.value:
                if _node.right:
                    _node = _node.right
                else:
                    _node.right = new_node
                    break
            else:
                break

        return node

    @staticmethod
    def _insert_rec(node, value):
        new_node = Node(value)
        if not node:
            node = new_node
            return node

        if value < node.value:
            node.left = BST._insert_rec(node.left, value)

        if value > node.value:
            node.right = BST._insert_rec(node.right, value)
        
        return node

    @staticmethod
    def _search(node, value, parent=None):
        if not node:
            return (None, parent)

        if node.value == value:
            return (node, parent)

        if value < node.value:
            return BST._search(node.left, value, node)

        return BST._search(node.right, value, node)

    @staticmethod
    def _min(node, parent=None):
        assert(node)
        if not node.left:
            return (node, parent)
        return BST._min(node.left, node)

    def search(self, value):
        return bool(BST._search(self.root, value))

    @staticmethod
    def _delete(target, parent):
        if not target:
            return

        # target is a a leaf node
        if not target.left and not target.right:
            if parent.left == target:
                parent.left = None
            if parent.right == target:
                parent.right = None
            del target

        # target has both children
        elif target.left and target.right:
            suc, suc_parent = BST._min(target.right, target)
            target.value = suc.value
            BST._delete(suc, suc_parent)

        # target has a single child
        else:
            child = target.left or target.right
            target.value = child.value
            BST._delete(child, target)


    def delete(self, value):
        BST._delete(*BST._search(self.root, value))

    @staticmethod
    def _traverse_pre(node, f):
        if not node:
            return
        f(node.value)
        BST._traverse_pre(node.left, f)
        BST._traverse_pre(node.right, f)

    @staticmethod
    def _traverse_in(node, f):
        if not node:
            return
        BST._traverse_in(node.left, f)
        f(node.value)
        BST._traverse_in(node.right, f)

    @staticmethod
    def _traverse_post(node, f):
        if not node:
            return
        BST._traverse_post(node.left, f)
        BST._traverse_post(node.right, f)
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
        self.traversal_modes.get(method, BST._traverse_pre)(self.root, f)

    def insert(self, value, method='rec'):
        self.root = self.insertion_modes.get(method, BST._insert_rec)(self.root, value)


if __name__ == '__main__':
    bt = BST()
    bt.insert(5)
    bt.insert(4)
    bt.insert(2)
    bt.insert(8)
    bt.insert(7)
    bt.insert(6)
    bt.insert(15)
    bt.insert(13)
    bt.insert(17)


    print(f'Is 2 present? {bt.search(2)}')
    print(f'Is 7 present? {bt.search(7)}')
    print(f'Is 3 present? {bt.search(3)}')

    bt.delete(17)

    print('Traversal in-order:')
    bt.foreach(print, method='in')