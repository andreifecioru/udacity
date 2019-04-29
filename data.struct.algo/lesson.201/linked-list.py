#!/usr/bin/env python

from operator import add


class Node:
    def __init__(self, value):
        self.value = value
        self.next = None

class LinkedList:
    def __init__(self):
        self.head = None
        
    def prepend(self, value):
        """ Prepend a value to the beginning of the list. """
        if not self.head:
            self.head = Node(value)
        else:
            node = Node(value)
            node.next = self.head
            self.head = node
    
    def append(self, value):
        """ Append a value to the end of the list. """
        
        if not self.head:
            self.head = Node(value)
        else:
            new_node = Node(value)
            node = self.head
            while node.next:
                node = node.next
            node.next = new_node
    
    def search(self, value):
        """ Search the linked list for a node with the requested value and return the node. """
        node = self.head
        while node:
            if node.value == value:
                return node
            node = node.next
        return None
        
    def remove(self, value):
        """ Remove first occurrence of value. """
        if self.head.value == value:
            old_head = self.head
            self.head = self.head.next
            del old_head
            return
        
        node = self.head.next
        prev = self.head
        while node:
            if node.value == value:
                prev.next = node.next
                del node
                return
            prev = node
            node = node.next
    
    def pop(self):
        """ Return the first node's value and remove it from the list. """
        if not self.head:
            return None
        old_head = self.head
        ret_val = old_head.value
        self.head = self.head.next
        del old_head
        return ret_val
    
    def insert(self, value, pos):
        """ Insert value at pos position in the list. If pos is larger than the
            length of the list, append to the end of the list. """
        assert(pos >= 0)
        size = self.size()

        if pos == 0:
            self.prepend(value)
            return

        if pos >= size:
            self.append(value)
            return
        

        node = self.head.next
        prev = self.head

        for _ in range(1, pos):
            prev = node
            node = node.next

        new_node = Node(value)
        prev.next = new_node
        new_node.next = node

    def size(self):
        """ Return the size or length of the linked list. """
        length = 0
        def inc_len(_):
            nonlocal length
            length += 1

        self.foreach(inc_len)
        return length

    def foreach(self, f):
        node = self.head
        while node:
            f(node.value)
            node = node.next
    
    def to_list(self):
        out = []
        self.foreach(lambda x: out.append(x))
        return out

    @classmethod
    def from_list(cls, in_list):
        ret_val = LinkedList()
        for value in reversed(in_list):
            ret_val.prepend(value)
        return ret_val


def reverse(llist):
    out = LinkedList()
    llist.foreach(lambda x: out.prepend(x))
    return out

def is_circular(llist):
    slow = llist.head
    fast = llist.head

    while(fast):
        fast = fast.next
        if not fast:
            return False
        if slow == fast:
            return True

        fast = fast.next
        if not fast:
            return False
        if slow == fast:
            return True

        slow = slow.next
    return False

if __name__ == '__main__':
    # Test prepend
    linked_list = LinkedList()
    linked_list.prepend(1)
    assert linked_list.to_list() == [1], f"list contents: {linked_list.to_list()}"
    linked_list.append(3)
    linked_list.prepend(2)
    assert linked_list.to_list() == [2, 1, 3], f"list contents: {linked_list.to_list()}"

    print(f"list contents: {reverse(linked_list).to_list()}")

        
    # Test append
    linked_list = LinkedList()
    linked_list.append(1)
    assert linked_list.to_list() == [1], f"list contents: {linked_list.to_list()}"
    linked_list.append(3)
    assert linked_list.to_list() == [1, 3], f"list contents: {linked_list.to_list()}"

    # Test search
    linked_list.prepend(2)
    linked_list.prepend(1)
    linked_list.append(4)
    linked_list.append(3)
    assert linked_list.search(1).value == 1, f"list contents: {linked_list.to_list()}"
    assert linked_list.search(4).value == 4, f"list contents: {linked_list.to_list()}"

    # Test remove
    linked_list.remove(1)
    assert linked_list.to_list() == [2, 1, 3, 4, 3], f"list contents: {linked_list.to_list()}"
    linked_list.remove(3)
    assert linked_list.to_list() == [2, 1, 4, 3], f"list contents: {linked_list.to_list()}"
    linked_list.remove(3)
    assert linked_list.to_list() == [2, 1, 4], f"list contents: {linked_list.to_list()}"

    # Test pop
    value = linked_list.pop()
    assert value == 2, f"list contents: {linked_list.to_list()}"
    assert linked_list.head.value == 1, f"list contents: {linked_list.to_list()}"

    # Test insert 
    linked_list.insert(5, 0)
    assert linked_list.to_list() == [5, 1, 4], f"list contents: {linked_list.to_list()}"
    linked_list.insert(2, 1)
    assert linked_list.to_list() == [5, 2, 1, 4], f"list contents: {linked_list.to_list()}"
    linked_list.insert(3, 6)
    assert linked_list.to_list() == [5, 2, 1, 4, 3], f"list contents: {linked_list.to_list()}"

    # Test size
    assert linked_list.size() == 5, f"list contents: {linked_list.to_list()}"


    # Creating a loop where the last node points back to the second node
    list_with_loop = LinkedList.from_list([2, -1, 3, 0, 5])
    loop_start = list_with_loop.head.next

    node = list_with_loop.head
    while node.next: 
        node = node.next   
    node.next = loop_start

    print("Pass" if (is_circular(list_with_loop) == True) else "Fail")
    print("Pass" if (is_circular(LinkedList.from_list([-4, 7, 2, 5, -1])) == False) else "Fail")
    print("Pass" if (is_circular(LinkedList.from_list([1])) == False) else "Fail")