#!/usr/bin/env python

class Stack:
    def __init__(self):
        self.items = []
    
    def size(self):
        return len(self.items)
    
    def push(self, item):
        self.items.append(item)

    def pop(self):
        if self.size()==0:
            return None
        else:
            return self.items.pop()

    def __repr__(self):
        return str(self.items)

class Queue:
    def __init__(self):
        self.stack = Stack()
        
    def size(self):
        return self.stack.size()
        
    def enqueue(self, item):
        self.stack.push(item)
        
    def dequeue(self):
        if self.is_empty():
            return None

        tmp_stack = Stack()
        while self.stack.size() > 1:
            tmp_stack.push(self.stack.pop())

        ret_val = self.stack.pop()

        while tmp_stack.size() > 0:
            self.stack.push(tmp_stack.pop())

        return ret_val

    def is_empty(self):
        return self.size() == 0

    def __repr__(self):
        return str(self.stack)


class Queue2:
    def __init__(self):
        self.in_stack= Stack()
        self.out_stack = Stack()
        
    def size(self):
        return self.in_stack.size() + self.out_stack.size()
        
    def enqueue(self, item):
        self.in_stack.push(item)
        
    def dequeue(self):
        if self.out_stack.size() == 0:
            while self.in_stack.size() > 0:
                self.out_stack.push(self.in_stack.pop())
        return self.out_stack.pop()

    def is_empty(self):
        return self.size() == 0

    def __repr__(self):
        return str(self.stack)


if __name__ == '__main__':
    # Setup
    q = Queue2()
    q.enqueue(1)
    q.enqueue(2)
    q.enqueue(3)

    # Test size
    print ("Pass" if (q.size() == 3) else "Fail")

    # Test dequeue
    print ("Pass" if (q.dequeue() == 1) else "Fail")

    # Test enqueue
    q.enqueue(4)

    print ("Pass" if (q.dequeue() == 2) else "Fail")
    print ("Pass" if (q.dequeue() == 3) else "Fail")
    print ("Pass" if (q.dequeue() == 4) else "Fail")
    q.enqueue(5)
    print ("Pass" if (q.size() == 1) else "Fail")