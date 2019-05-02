#!/usr/bin/env python

class Node:
    def __init__(self, value):
        self.value = value
        self.next = None


class Stack:
    def __init__(self):
        self.tos = None
        self.num_elements = 0

    def push(self, value):
        if not self.tos:
            self.tos = Node(value)
        else:
            new_tos = Node(value)
            new_tos.next = self.tos
            self.tos = new_tos
        self.num_elements += 1

    def top(self):
        if not self.is_empty():
            return self.tos.value
        return None

    def pop(self):
        if not self.is_empty():
            old_tos = self.tos
            ret_val = old_tos.value
            self.tos = self.tos.next
            del old_tos
            self.num_elements -= 1
            return ret_val
        return None

    def is_empty(self):
        return self.num_elements == 0

    def size(self):
        return self.num_elements

    def __repr__(self):
        ret_val = self._foldLeft('', lambda acc, value: acc + str(value) + ', ')
        return '[' + ret_val[:-2] + ']' if ret_val else '[]'

    def _foldLeft(self, zero, f):
        if not self.tos:
            return zero

        acc = zero
        node = self.tos
        while node:
            acc = f(acc, node.value)
            node = node.next

        return acc


if __name__ == '__main__':
    stack = Stack()

    print(stack)
    print(stack.is_empty())

    for x in range(0, 20):
        stack.push(x)

    print(stack)
    print(stack.top())

    for _ in range(0, 15):
        print(stack.pop())

    print(stack)
    print(stack.is_empty())
