#!/usr/bin/env python

def string_reverser(input):
    return input[::-1]


def word_flipper(our_string):
    """
    Flip the individual words in a sentence

    Args:
       our_string(string): String with words to flip
    Returns:
       string: String with words flipped
    """
    
    return ' '.join(string_reverser(word) for word in our_string.split(' '))

if __name__ == '__main__':
    print ("Pass" if ('retaw' == word_flipper('water')) else "Fail")
    print ("Pass" if ('sihT si na elpmaxe' == word_flipper('This is an example')) else "Fail")
    print ("Pass" if ('sihT si eno llams pets rof ...' == word_flipper('This is one small step for ...')) else "Fail")
