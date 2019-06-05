#!/usr/bin/env python

def merge_sort(items):
    def _merge(l1, l2):
        result = []
        while l1 and l2:
            if l1[0] > l2[0]:
                result.append(l2.pop(0))
            else:
                result.append(l1.pop(0))
        result += l1 + l2
        return result

    def _sort(items):
        if len(items) == 1:
            return items

        if len(items) == 2:
            if items[0] > items[1]:
                items[0], items[1] = items[1], items[0]
            return items
        
        mid_idx = len(items) // 2

        left = items[:mid_idx]
        right = items[mid_idx:]

        return _merge(_sort(left), _sort(right))

    return _sort(items)


if __name__ == '__main__':
    test_list = [8, 3, 1, 7, 0, 10, 2]
    print(merge_sort(test_list))
