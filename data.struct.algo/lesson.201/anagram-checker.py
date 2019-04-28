#!/usr/bin/env python

def normalize(input):
    return input.replace(' ', '').lower()


def anagram_checker_1(str1, str2):
    """
    Check if the input strings are anagrams

    Args:
       str1(string),str2(string): Strings to be checked if they are anagrams
    Returns:
       bool: If strings are anagrams or not
    """
    def count_letters(input):
        ret_val = {}
        letters = (letter for letter in input.lower() if letter != ' ')
        for letter in letters:
            if ret_val.get(letter):
                ret_val[letter] += 1
            else:
                ret_val[letter] = 1

        return ret_val

    str1 = normalize(str1)
    str2 = normalize(str2)

    if (len(str1) != len(str2)):
        return False

    return count_letters(str1) == count_letters(str2)


def anagram_checker_2(str1, str2):

    str1 = normalize(str1)
    str2 = normalize(str2)

    if (len(str1) != len(str2)):
        return False

    return sorted(normalize(str1)) == sorted(normalize(str2))


anagram_checker = anagram_checker_2

if __name__ == '__main__':
    print ("Pass" if not (anagram_checker('water','waiter')) else "Fail")
    print ("Pass" if anagram_checker('Dormitory','Dirty room') else "Fail")
    print ("Pass" if anagram_checker('Slot machines', 'Cash lost in me') else "Fail")
    print ("Pass" if not (anagram_checker('A gentleman','Elegant men')) else "Fail")
    print ("Pass" if anagram_checker('Time and tide wait for no man','Notified madman into water') else "Fail")