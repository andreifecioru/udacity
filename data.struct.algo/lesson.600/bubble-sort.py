#!/usr/bin/en python

def bubble_sort(items):
    is_sorted = False
    iteration = 0
    while not is_sorted:
        is_sorted = True
        for idx in range(0, len(items) - 1 - iteration):
            if items[idx] > items[idx + 1]:
                items[idx], items[idx + 1] = items[idx + 1], items[idx]
                is_sorted = False
        iteration += 1

if __name__ == '__main__':
    items = [4, 5, 2, 11, 9, 10]
    bubble_sort(items)
    print(items)