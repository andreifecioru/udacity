#!/usr/bin/env python

correct = [[1,2,3],
           [2,3,1],
           [3,1,2]]

incorrect = [[1,2,3,4],
             [2,3,1,3],
             [3,1,2,3],
             [4,4,4,4]]

incorrect2 = [[1,2,3,4],
             [2,3,1,4],
             [4,1,2,3],
             [3,4,1,2]]

incorrect3 = [[1,2,3,4,5],
              [2,3,1,5,6],
              [4,5,2,1,3],
              [3,4,5,2,1],
              [5,6,4,3,2]]

incorrect4 = [['a','b','c'],
              ['b','c','a'],
              ['c','a','b']]

incorrect5 = [ [1, 1.5],
               [1.5, 1]]
               
# Define a function check_sudoku() here:
def check_sudoku(board):
    size = len(board)

    def check_board():
        for line in board:
            if len(line) != size:
                return False
        return True

    def check_set(_set):
        return sorted(_set) == list(range(1, len(_set) + 1))

    def transpose():
        transposed = [[0] * size] * size
        for i in range(size):
            for j in range(size):
                transposed[i][j] = board[j][i]
        return transposed

    if not check_board():
        return False

    for line in board:
        if not check_set(line):
            return False

    for col in transpose():
        if not check_set(col):
            return False

    return True


if __name__ == '__main__':
    print(check_sudoku(incorrect))
    #>>> False

    print(check_sudoku(correct))
    #>>> True

    print(check_sudoku(incorrect2))
    #>>> False

    print(check_sudoku(incorrect3))
    #>>> False

    print(check_sudoku(incorrect4))
    #>>> False

    print(check_sudoku(incorrect5))
    #>>> False
