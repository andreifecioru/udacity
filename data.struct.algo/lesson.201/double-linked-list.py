#!/usr/bin/env python

class DoubleNode:
    def __init__(self, value=None):
        self.value = value
        self.next = None
        self.prev = None

class DoublyLinkedList:
    def __init__(self):
        self.head = None
        self.tail = None

    @classmethod
    def from_list(cls, in_list):
        ret_val = cls()
        for value in in_list:
            ret_val.append(value)
        return ret_val

    def append(self, value):
        node = DoubleNode(value)
        if not self.head:
            self.head = node
            self.tail = node
        else:
            self.tail.next = node
            node.prev = self.tail
            self.tail = node

    def foreach(self, f, reverse=False):
        if reverse:
            node = self.tail
            while node:
                f(node.value)
                node = node.prev
        else:
            node = self.head
            while node:
                f(node.value)
                node = node.next


if __name__ == '__main__':
    dll = DoublyLinkedList.from_list([1, 2, 3, 4])
    dll.foreach(print)
    print('--------------')
    dll.foreach(print, reverse=True)
