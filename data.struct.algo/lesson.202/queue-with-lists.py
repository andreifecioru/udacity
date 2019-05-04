#!/usr/bin/env python

class Node:
    def __init__(self, value):
        self.value = value
        self.next = None

class Queue:
    def __init__(self):
        self.head = None
        self.tail = None
        self.num_elements = 0

    def enqueue(self, value):
        node = Node(value)
        if self.is_empty():
            self.head = node
            self.tail = node
        else:
            self.tail.next = node
            self.tail = node
        self.num_elements += 1

    def front(self):
        if self.is_empty():
            return None

        return self.head.value

    def dequeue(self):
        if self.is_empty():
            return None

        old_head = self.head
        ret_val = old_head.value
        self.head = old_head.next
        del old_head
        self.num_elements -= 1
        return ret_val

    def is_empty(self):
        return self.num_elements == 0

    def size(self):
        return self.num_elements

    def __repr__(self):
        if self.is_empty():
            return '[]'

        ret_val = '['
        node = self.head
        while node:
            ret_val += str(node.value) + ', '
            node = node.next
        return ret_val[:-2] + ']'

if __name__ == '__main__':
    queue = Queue()

    print(queue)
    print(queue.is_empty())

    for x in range(0, 20):
        queue.enqueue(x)

    print(queue)
    print(queue.front())

    for _ in range(0, 15):
        print(queue.dequeue())

    print(queue)
    print(queue.is_empty())



