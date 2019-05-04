#!/usr/bin/env python
import copy


def permute(l):
    """
    Return a list of permutations
    
    Examples:
       permute([0, 1]) returns [ [0, 1], [1, 0] ]
    
    Args:
      l(list): list of items to be permuted
    
    Returns:
      list of permutation with each permuted item being represented by a list
    """
    if len(l) <= 1:
        return [l]
    
    head = l[0]
    tail_permutations = permute(l[1:])

    ret_val = []
    for perm in tail_permutations:
        for idx in range(0, len(perm) + 1):
            ret_val.append(perm[0:idx] + [head] + perm[idx:])

    return ret_val


# Helper Function
def check_output(output, expected_output):
    """
    Return True if output and expected_output
    contains the same lists, False otherwise.
    
    Note that the ordering of the list is not important.
    
    Examples:
        check_output([ [0, 1], [1, 0] ], [ [1, 0], [0, 1] ]) returns True

    Args:
        output(list): list of list
        expected_output(list): list of list
    
    Returns:
        bool
    """
    print(output)
    o = copy.deepcopy(output)  # so that we don't mutate input
    e = copy.deepcopy(expected_output)  # so that we don't mutate expected output
    
    o.sort()
    e.sort()
    return o == e

if __name__ == '__main__':
    print ("Pass" if  (check_output(permute([]), [[]])) else "Fail")
    print ("Pass" if  (check_output(permute([0]), [[0]])) else "Fail")
    print ("Pass" if  (check_output(permute([0, 1]), [[0, 1], [1, 0]])) else "Fail")
    print ("Pass" if  (check_output(permute([0, 1, 2]), [[0, 1, 2], [0, 2, 1], [1, 0, 2], [1, 2, 0], [2, 0, 1], [2, 1, 0]])) else "Fail")