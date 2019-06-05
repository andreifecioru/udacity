#!/usr/bin/env python


def enhanced_merge_sort(items):
    def _merge(left, right):
        inv_count = left[1] + right[1]
        l1, l2 = left[0], right[0]
        result = []
        while l1 and l2:
            if l1[0] > l2[0]:
                result.append(l2.pop(0))
                # if at any point the element in the left array is greater
                # than the one on the right, we increment the number of inversions
                # with the number of remaining items in the right array. 
                # (because we know both arrays are sorted at this point) 
                inv_count += len(l1)
            else:
                result.append(l1.pop(0))
        result += l1 + l2
        return (result, inv_count)

    def _sort(items):
        if len(items) <= 1:
            return (items, 0)

        mid_idx = len(items) // 2

        left = items[:mid_idx]
        right = items[mid_idx:]

        return _merge(_sort(left), _sort(right))

    return _sort(items)


def count_inversions(items):
    return enhanced_merge_sort(items)[1]


def test_function(test_case):
    arr = test_case[0]
    solution = test_case[1]
    result = count_inversions(arr)
    if result == solution:
        print("Pass")
    else:
        print(f"Fail. Expected: {solution}. Got: {result}")


if __name__ == "__main__":
    arr = [2, 5, 1, 3, 4]
    solution = 4
    test_case = [arr, solution]
    test_function(test_case)
