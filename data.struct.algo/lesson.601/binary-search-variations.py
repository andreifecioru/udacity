#!/usr/bin/env python
def binary_search(array, target):
    '''Write a function that implements the binary search algorithm using iteration

    args:
      array: a sorted array of items of the same type
      target: the element you're searching for

    returns:
      int: the index of the target, if found, in the source
      -1: if the target is not found
    '''
    if not array:
        return -1

    start = 0
    end = len(array) - 1
    while start <= end:
        mid = start + (end - start) // 2
        if target == array[mid]:  # target found
            return mid

        if target < array[mid]:
            end = mid - 1
        else:
            start = mid + 1
    return -1


def find_first(array, target):
    idx = binary_search(array, target)
    while idx > 0 and array[idx - 1] == target:
        idx -= 1
    return idx


def contains(array, target):
    return binary_search(array, target) != -1


def first_and_last(array, target):
    idx = binary_search(array, target)
    start = idx
    end = idx
    while start > 0 and array[start - 1] == target:
        start -= 1
    while end < len(array) - 1 and array[end + 1] == target:
        end += 1
    return [start, end]


def test_function(test_case):
    input_list = test_case[0]
    number = test_case[1]
    solution = test_case[2]
    output = first_and_last(input_list, number)
    if output == solution:
        print("Pass")
    else:
        print("Fail", output)


if __name__ == '__main__':
    array = [1, 3, 5, 7, 7, 7, 8, 11, 12]
    print(find_first(array, 7))
    print('-------------------')

    letters = ['a', 'c', 'd', 'f', 'g']
    print(contains(letters, 'a'))  # True
    print(contains(letters, 'b'))  # False
    print('-------------------')

    input_list = [1]
    number = 1
    solution = [0, 0]
    test_case_1 = [input_list, number, solution]
    test_function(test_case_1)
    print('-------------------')

    input_list = [0, 1, 2, 3, 3, 3, 3, 4, 5, 6]
    number = 3
    solution = [3, 6]
    test_case_2 = [input_list, number, solution]
    test_function(test_case_2)
    print('-------------------')

    input_list = [0, 1, 2, 3, 4, 5]
    number = 5
    solution = [5, 5]
    test_case_3 = [input_list, number, solution]
    test_function(test_case_3)
    print('-------------------')

    input_list = [0, 1, 2, 3, 4, 5]
    number = 6
    solution = [-1, -1]
    test_case_4 = [input_list, number, solution]
    test_function(test_case_4)
    print('-------------------')
