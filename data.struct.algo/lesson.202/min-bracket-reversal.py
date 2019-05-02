#!/usr/bin/env python

class LinkedListNode:

    def __init__(self, data):
        self.data = data
        self.next = None

class Stack:

    def __init__(self):
        self.num_elements = 0
        self.head = None

    def push(self, data):
        new_node = LinkedListNode(data)
        if self.head is None:
            self.head = new_node
        else:
            new_node.next = self.head
            self.head = new_node
        self.num_elements += 1

    def pop(self):
        if self.is_empty():
            return None
        temp = self.head.data
        self.head = self.head.next
        self.num_elements -= 1
        return temp

    def top(self):
        if self.head is None:
            return None
        return self.head.data

    def size(self):
        return self.num_elements

    def is_empty(self):
        return self.num_elements == 0

    def __repr__(self):
        if self.is_empty():
            return '[]'
        ret_val = '['
        node = self.head
        while node.next:
            ret_val += str(node.data) + ', '
            node = node.next
        return ret_val + str(node.data) + ']'


def minimum_bracket_reversals(input_string):
    """
    Calculate the number of reversals to fix the brackets

    Args:
       input_string(string): Strings to be used for bracket reversal calculation
    Returns:
       int: Number of bracket reversals needed
    """
    if len(input_string) % 2 != 0:
        return -1

    stack = Stack()

    # remove all parens that are already paired
    for letter in input_string:
        if stack.is_empty():
            stack.push(letter)
            continue

        top = stack.top()
        if top == "{" and letter == "}":
            stack.pop()
        else:
            stack.push(letter)

    ret_val = 0
    while not stack.is_empty():
        p1 = stack.pop()
        p2 = stack.pop()
        if p1 == p2: # matching parens (need to switch one to pair them)
            ret_val += 1
        else: # back-to-back parens (need to switch both to pair them them)
            ret_val += 2

    return ret_val


def test_function(test_case):
    input_string = test_case[0]
    expected_output = test_case[1]
    output = minimum_bracket_reversals(input_string)
    
    if output == expected_output:
        print("Pass")
    else:
        print("Fail")


if __name__ == '__main__':
    test_case_1 = ["}}}}", 2]
    test_function(test_case_1)

    test_case_2 = ["}}{{", 2]          
    test_function(test_case_2)

    test_case_3 = ["{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{}}}}}", 13]
    test_function(test_case_3)

    test_case_4= ["}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{", 2]
    test_function(test_case_4)

    test_case_5 = ["}}{}{}{}{}{}{}{}{}{}{}{}{}{}{}", 1]
    test_function(test_case_5)
