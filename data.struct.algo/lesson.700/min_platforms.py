#!/usr/bin/env python

from collections import namedtuple

Interval = namedtuple('Interval', ['start', 'end'])


def min_platforms(arrival, departure):
    """
    :param: arrival - list of arrival time
    :param: departure - list of departure time

    :return: the minimum number of platforms (int) required
    so that no train has to wait for other(s) to leave
    """
    assert(len(arrival) == len(departure))
    platforms = []

    def _park_train(interval):
        assert(interval.start < interval.end)
        for idx, _interval in enumerate(platforms):
            # let's look for an available platform
            if _interval.end <= interval.start or interval.end <= _interval.start:
                platforms[idx] = interval
                break
        # no available platform: add a new one
        else:
            platforms.append(interval)

    while arrival and departure:
        _park_train(Interval(arrival.pop(0), departure.pop(0)))

    return len(platforms)


def test_function(test_case):
    arrival = test_case[0]
    departure = test_case[1]
    solution = test_case[2]

    output = min_platforms(arrival, departure)
    if output == solution:
        print("Pass")
    else:
        print(f"Fail. Expected {solution} | Got: {output}")


if __name__ == '__main__':
    arrival = [900,  940, 950,  1100, 1500, 1800]
    departure = [910, 1200, 1120, 1130, 1900, 2000]
    test_case = [arrival, departure, 3]
    test_function(test_case)

    arrival = [200, 210, 300, 320, 350, 500]
    departure = [230, 340, 320, 430, 400, 520]
    test_case = [arrival, departure, 2]
    test_function(test_case)
