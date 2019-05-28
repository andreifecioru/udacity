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
    end = len(array)
    while start <= end:
        mid = start + (end - start) // 2
        if target == array[mid]:  # target found
            return mid

        if target < array[mid]:
            end = mid - 1
        else:
            start = mid + 1

    return -1


def binary_search_recursive(array, target):
    '''Write a function that implements the binary search algorithm using recursion

    args:
      array: a sorted array of items of the same type
      target: the element you're searching for

    returns:
      int: the index of the target, if found, in the source
      -1: if the target is not found
    '''
    def search(start, end):
        mid = start + (end - start) // 2
        if array[mid] == target:
            return mid

        if target < array[mid]:
            return search(start, mid - 1)

        return search(mid + 1, end)

    return search(0, len(array) - 1)


def test_function(test_case):
    # answer = binary_search(test_case[0], test_case[1])
    answer = binary_search_recursive(test_case[0], test_case[1])
    if answer == test_case[2]:
        print("Pass!")
    else:
        print("Fail!")


if __name__ == '__main__':
    array = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    target = 6
    index = 6
    test_case = [array, target, index]
    test_function(test_case)
