#!/usr/bin/env python

MID_VALUE = 1

def sort_012(input_list):
    """
    Given an input array consisting on only 0, 1, and 2, sort the array in a single traversal.

    Args:
        input_list(list): List to be sorted
    """
    low = 0
    high = len(input_list) - 1
    current = 0

    while current <= high:
        if input_list[current] < MID_VALUE:
            # we've encountered a "low" value: 
            # do a swap tp move it towards the start of the array 
            input_list[current], input_list[low] = input_list[low], input_list[current]
            low += 1
            current += 1

        elif input_list[current] > MID_VALUE:
            # we've encountered a "high" value: 
            # do a swap tp move it towards the end of the array 
            input_list[current], input_list[high] = input_list[high], input_list[current]
            high -= 1
        else:
            # we've encountered a "mid" value
            # do nothing, and move to the next element in the array
            current += 1

    return input_list


def test_function(test_case):
    sorted_array = sort_012(test_case)
    print(sorted_array)
    if sorted_array == sorted(test_case):
        print("Pass")
    else:
        print("Fail")


if __name__ == "__main__":
    test_function([0, 0, 2, 2, 2, 1, 1, 1, 2, 0, 2])
    test_function([2, 1, 2, 0, 0, 2, 1, 0, 1, 0, 0, 2, 2, 2, 1, 2, 0, 0, 0, 2, 1, 0, 2, 0, 0, 1])
    test_function([0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2])

