#!/usr/bin/env python


def rotated_array_search(input_list, number):
    """
    Find the index by searching in a rotated sorted array

    Args:
        input_list(array), number(int): Input array to search and the target
    Returns:
        int: Index or -1
    """
    def _pivot_index(start, end):
        """
        Determine the pivot in the input list. The pivot is the lowest
        value in the input list if that value is not at index 0.

        If the lowest value is at index 0 return None (which means that)
        the input list is not rotated at all.
        """
        if start == end - 1:
            return end if input_list[start] > input_list[end] else None

        mid = (start + end) // 2

        if input_list[start] > input_list[mid]:
            return _pivot_index(start, mid)

        return _pivot_index(mid, end)

    def _bin_search(start, end):
        """
        Plain-old binary search: input list and target number are
        passed in implicitly via closure.
        """
        if start == end:
            return start if input_list[start] == number else -1

        mid = (start + end) // 2

        if input_list[mid] == number:
            return mid

        if input_list[mid] > number:
            return _bin_search(start, mid - 1)

        return _bin_search(mid + 1, end)

    # determine the pivot (a binary-search variant -> O(log(n))
    pivot_idx = _pivot_index(0, len(input_list) - 1)

    # if there is no pivot just do the classic binary search (O(log(n)))
    if not pivot_idx:
        return _bin_search(0, len(input_list) - 1)

    # is the pivot the target? If so, just return it
    if input_list[pivot_idx] == number:
        return pivot_idx

    # now we decide whether we should to a binary search to the right or to the
    # left of the pivot. Either way we get O(log(n))
    if input_list[0] <= number:
        return _bin_search(0, pivot_idx - 1)

    return _bin_search(pivot_idx + 1, len(input_list) - 1)


def linear_search(input_list, number):
    for index, element in enumerate(input_list):
        if element == number:
            return index
    return -1


def test_function(test_case):
    input_list = test_case[0]
    number = test_case[1]
    result = rotated_array_search(input_list, number)
    expected = linear_search(input_list, number)
    if result == expected:
        print("Pass")
    else:
        print(f"Fail. Expected: {expected} | Got: {result}")


if __name__ == '__main__':
    test_function([[6, 7, 8, 9, 10, 1, 2, 3, 4], 6])
    test_function([[6, 7, 8, 9, 10, 1, 2, 3, 4], 1])
    test_function([[6, 7, 8, 1, 2, 3, 4], 8])
    test_function([[6, 7, 8, 1, 2, 3, 4], 1])
    test_function([[6, 7, 8, 1, 2, 3, 4], 10])
    test_function([[1, 2, 3, 4, 6, 7, 8], 10])
