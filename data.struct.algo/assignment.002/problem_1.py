#!/usr/bin/env python

from collections import namedtuple

Entry = namedtuple('Entry', ['key', 'value'])

class Node:
    def __init__(self, data):
        self.data = data
        self.next = None
        self.prev = None

    def __repr__(self):
        return 'Node({})'.format(self.data)

class LinkedList:
    def __init__(self):
        self.head = None
        self.tail = None

    def insertInFront(self, node):
        if not self.head: # empty list
            self.head = node
            self.tail = node
        else:
            node.next = self.head
            self.head.prev = node
            self.head = node

    def removeTail(self):
        return self.removeNode(self.tail)

    def removeNode(self, node):
        if not node.next and not node.prev: # just one element in list
            assert(node == self.head)
            assert(node == self.tail)
            self.head = None
            self.tail = None
        elif not node.prev: # head removal
            assert(node == self.head)
            self.head = node.next
            self.head.prev = None
        elif not node.next: # tail removal
            assert(node == self.tail)
            self.tail = self.tail.prev
            self.tail.next = None
            node.prev = None
        else: # remove node in the middle
            node.prev.next = node.next
            node.next.prev = node.prev
            node.next = None
            node.prev = None
        # return back the removed node
        return node

class LRU_Cache(object):

    def __init__(self, capacity):
        # Initialize class variables
        self.hash = {}
        self.linked_list = LinkedList()
        self.capacity = capacity


    def get(self, key):
        if key in self.hash:
            # we have a cache hit 
            node = self.hash[key]
            # we promote the corresponding node to the front of the queue
            self.linked_list.removeNode(node)
            self.linked_list.insertInFront(node)
            # get the value
            return node.data.value
        else:
            # not in cache
            return -1


    def set(self, key, value):
        if self.get(key) == -1:
            # Value is not present in the cache
            if len(self.hash) == self.capacity:
                # we've reached maximum capacity - we need to evict
                # make room by removing the element at the rear of the linked list
                evicted_node = self.linked_list.removeTail()
                # and also remove it from the hash
                del self.hash[evicted_node.data.key]

            # at this point we know we have room to add one more
            node =  Node(Entry(key, value))
            # ... in the hash
            self.hash[key] = node
            # ... and also to the front of the queue
            self.linked_list.insertInFront(node)
        else:
            # Value is present in the cache - just replace the value
            self.hash[key].data.value = value


if __name__ == '__main__':
    # create a cache with 2 slots
    our_cache = LRU_Cache(2)

    # fill the cache with data
    our_cache.set(1, 1)
    our_cache.set(2, 2)

    print(our_cache.get(1))       # returns 1 => cache hit
    print(our_cache.get(2))       # returns 2 => cache hit
    print(our_cache.get(3))       # returns -1 => cache miss

    # add one more
    our_cache.set(3, 3)           # capacity overflow => key 1 (oldest) will be evicted

    print(our_cache.get(3))       # returns 3 => cache hit
    print(our_cache.get(1))       # returns -1 => cache miss (was evicted)

