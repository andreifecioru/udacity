#!/usr/bin/env python

def smallest_positive(in_list):
    # TODO: Define a control structure that finds the smallest positive
    # number in in_list and returns the correct smallest number.
    ret_val = None
    for number in in_list:
        if number > 0 and (not ret_val or number < ret_val):
            ret_val = number
        
    return ret_val


if __name__ == '__main__':
    print(smallest_positive([4, -6, 7, 2, -4, 10]))
    # Correct output: 2

    print(smallest_positive([.2, 5, 3, -.1, 7, 7, 6]))
    # Correct output: 0.2

    print(smallest_positive([88.22, -17.41, -26.53, 18.04, -44.81, 7.52, 0.0, 20.98, 11.76]))