#!/usr/bin/env python

from array import array

class Queue:
    def __init__(self):
        self.arr = array('i', [0 for _ in range(0, 10)])
        self.next_index = 0
        self.front_index = 0
        self.queue_size = 0

    def enqueue(self, value):
        if self.queue_size == len(self.arr) - 1:
            self._handle_full_capacity()

        self.arr[self.next_index] = value

        self.next_index = (self.next_index + 1) % len(self.arr)
        self.queue_size += 1

    def dequeue(self):
        if self.is_empty():
            return None

        ret_val = self.arr[self.front_index]

        self.front_index = (self.front_index + 1) % len(self.arr)
        self.queue_size -= 1

        return ret_val

    def size(self):
        return self.queue_size

    def front(self):
        if self.is_empty():
            return None
        return self.arr[self.front_index]

    def is_empty(self):
        return self.queue_size == 0

    def _handle_full_capacity(self):
        old_capacity = len(self.arr)
        self.arr[self.next_index:self.next_index] = array('i', [0 for _ in range(0, old_capacity)])
        if self.front_index > self.next_index:
            self.front_index += old_capacity
        print(self._show())

    def __repr__(self):
        front_index = self.front_index
        next_index = self.next_index

        if front_index <= next_index:
            return ', '.join([str(x) for x in self.arr[front_index:next_index]])
        else:
            return ', '. join([str(x) for x in self.arr[front_index:]] + 
                              [str(x) for x in self.arr[:next_index]])

    def _show(self):
        front_index = self.front_index
        next_index = self.next_index

        if front_index == next_index:
            return '[]'

        result = [str(x) for x in self.arr]
        result[front_index] = '(' + result[front_index] + ')'
        result[next_index] = '[' + result[next_index] + ']'

        return ', '.join(result)


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


