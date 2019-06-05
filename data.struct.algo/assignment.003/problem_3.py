#!/usr/bin/env python

from operator import gt, lt

class Heap:
    """
        Implementation of a heap data structure. 
        Supports both min and max heaps. 
    """
    supported_types = ["min", "max"]

    def __init__(self, initial_capacity=100, heap_type="max"):
        if heap_type not in Heap.supported_types:
            raise ValueError(f"Unsupported heap type: {heap_type}. Choose one of: {supported_types}.")

        self._type = heap_type
        self._data = [None for x in range(initial_capacity)]
        self._size = 0

    def insert(self, value):
        self._increase_capacity()
        self._data[self._size] = value
        self._size += 1
        self._up_heap(self._size - 1)

    def remove(self):
        ret_val = self._data[0]
        self._data[0] = self._data[self._size - 1]
        self._data[self._size - 1] = None
        self._size -= 1
        if self._size > 0:
            self._down_heap(0)
        return ret_val

    def _increase_capacity(self):
        if self._size == len(self._data):
            self._data = self._data + [None for x in range(len(self._data))]

    def _up_heap(self, idx):
        if idx == 0:
            return

        comparator = gt if self._type == "max" else lt
        parent_idx = (idx - 1) // 2
        if comparator(self._data[idx], self._data[parent_idx]):
            self._data[idx], self._data[parent_idx] = self._data[parent_idx], self._data[idx]
            self._up_heap(parent_idx)

    def _down_heap(self, idx):
        selector = Heap._max if self._type == "max" else Heap._min
        parent = self._item_with_index(idx)
        left = self._item_with_index(2 * idx + 1)
        right = self._item_with_index(2 * idx + 2)

        swap_idx = selector(parent, left, right)[1]
        if swap_idx != idx:
            self._data[idx], self._data[swap_idx] = self._data[swap_idx], self._data[idx]
            self._down_heap(swap_idx)

    def _item_with_index(self, idx):
        return (self._data[idx], idx) if idx < self._size else None

    @staticmethod
    def _min(*args):
        args = [arg for arg in args if arg is not None]
        return min(args)

    @staticmethod
    def _max(*args):
        args = [arg for arg in args if arg is not None]
        return max(args)

    def __repr__(self):
        return f'{"MaxHeap" if self._type == "max" else "MinHeap"}: {str([d for d in self._data if d is not None])}'

    def __len__(self):
        return self._size


def rearrange_digits(input_list):
    """
    Rearrange Array Elements so as to form two number such that their sum is maximum.

    Args:
        input_list(list): Input List
    Returns:
        (int),(int): Two maximum sums
    """

    # insert all digits in a max-heap
    # se we can extract then in descending order
    heap = Heap(heap_type="max")
    for digit in input_list:
        heap.insert(digit)

    left, right = 0, 0

    # extract the digits one by one from the max-heap
    # and distribute them evenly between the two output numbers
    while True:
        if len(heap) > 0:
            left = left * 10 + heap.remove()

        if len(heap)> 0:
            right = right * 10 + heap.remove()

        if len(heap) == 0:
            break

    return [left, right]


def test_function(test_case):
    output = rearrange_digits(test_case[0])
    solution = test_case[1]
    if sum(output) == sum(solution):
        print("Pass")
    else:
        print(f"Fail. Expected {solution}. Got: {output}.")


if __name__ == "__main__":
    test_function([[], [0, 0]])
    test_function([[1], [1, 0]])
    test_function([[1, 2, 3, 4, 5], [542, 31]])
    test_function([[4, 6, 2, 5, 9, 8], [964, 852]])

