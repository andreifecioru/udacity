#!/usr/bin/env python


def string_reverser(input):
    return input[::-1]


if __name__ == '__main__':
    print("Pass" if ('retaw' == string_reverser('water')) else "Fail")
    print("Pass" if ('!noitalupinam gnirts gnicitcarP' ==
        string_reverser('Practicing string manipulation!')) else "Fail")
    print("Pass" if ('3432 :si edoc esuoh ehT' == string_reverser('The house code is: 2343')) else "Fail")
