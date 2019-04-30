#!/usr/bin/env python

from array import array

class Stack:
    def __init__(self, initial_size=10):
        self.arr = array('i', [0 for _ in range(0, initial_size)])
        self.next_index = 0
        self.num_elements = 0

    def push(self, value):
        # increase capacity if needed
        if self.num_elements == self._arr_size():
            self.arr += array('i', [0 for _ in range(0, self._arr_size())])

        self.arr[self.next_index] = value
        self.next_index += 1
        self.num_elements += 1
        
    def pop(self):
        if self.is_empty():
            return None

        ret_val = self.top()

        self.arr[self.next_index - 1] = 0
        self.next_index -= 1
        self.num_elements -= 1

        # decrease capacity if needed
        if self.num_elements < int(self._arr_size() / 2):
            self.arr = self.arr[:int(self._arr_size() / 2)]

        return ret_val

    def top(self):
        if self.is_empty():
             return None
        return self.arr[self.next_index - 1]

    def _arr_size(self):
        return len(self.arr)

    def is_empty(self):
        return self.num_elements == 0

    def size(self):
        return self.num_elements

    def __repr__(self):
        return str(self.arr)

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
