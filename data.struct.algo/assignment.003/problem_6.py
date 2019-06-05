import random


def get_min_max(ints):
    """
    Return a tuple(min, max) out of list of unsorted integers.

    Args:
        ints(list): list of integers containing one or more integers
    """
    if not ints:  # sanity check -> exit fast
        return (None, None)

    _min = ints[0]
    _max = ints[0]

    for current in ints:
        # update the min value is needed
        if current < _min:
            _min = current

        # update the max value is needed
        if current > _max:
            _max = current

    return (_min, _max)


def test():
    """ Simple testing function: 
        shuffle a list of numbers and check the min/max computation. 
    """
    l = [i for i in range(0, 10)]  # a list containing 0 - 9
    random.shuffle(l)

    print("Pass" if ((0, 9) == get_min_max(l)) else "Fail")


if __name__ == '__main__':
    # run the test function for 100 times
    for i in range(100):
        test()
