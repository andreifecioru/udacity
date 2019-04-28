#!/usr/bin/env python
def hamming_distance(str1, str2):
    """
    Calculate the hamming distance of the two strings

    Args:
       str1(string),str2(string): Strings to be used for finding the hamming distance
    Returns:
       int: Hamming Distance
    """
    if len(str1) != len(str2):
        return None

    diff = [(lambda letters: letters[0] != letters[1])(letters) for letters in zip(str1, str2)]
    return diff.count(True)
    

if __name__ == '__main__':
    print ("Pass" if (10 == hamming_distance('ACTTGACCGGG','GATCCGGTACA')) else "Fail")
    print ("Pass" if  (1 == hamming_distance('shove','stove')) else "Fail")
    print ("Pass" if  (None == hamming_distance('Slot machines', 'Cash lost in me')) else "Fail")
    print ("Pass" if  (9 == hamming_distance('A gentleman','Elegant men')) else "Fail")
    print ("Pass" if  (2 == hamming_distance('0101010100011101','0101010100010001')) else "Fail")