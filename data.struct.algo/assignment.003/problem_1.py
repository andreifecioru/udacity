#!/usr/bin/env python


def sqrt(number):
    """
    Calculate the floored square root of a number

    Args:
        number(int): Number to find the floored squared root
    Returns:
        int: Floored Square Root
    """
    def _compute(start, end):
        # this is the condition for stopping the recursive loop
        if start >= end - 1:
            return start

        mid = (start + end) // 2
        if mid * mid > number:
            return _compute(start, mid)
        else:
            return _compute(mid, end)

    if number < 0:
        raise ValueError("Cannot compute square root of negative number.")

    return _compute(0, number) if number > 1 else number


if __name__ == '__main__':
    print("Pass" if (3 == sqrt(9)) else "Fail")
    print("Pass" if (0 == sqrt(0)) else "Fail")
    print("Pass" if (4 == sqrt(16)) else "Fail")
    print("Pass" if (1 == sqrt(1)) else "Fail")
    print("Pass" if (5 == sqrt(27)) else "Fail")
