#!/usr/bin/env python

def prod(a,b):
    output = a * b
    return output

def fact_gen():
    i = 1
    n = i
    while True:
        output = prod(n, i)
        yield output
        i += 1
        n = output


# Test block
my_gen = fact_gen()
num = 5
for i in range(num):
    print(next(my_gen))

# Correct result when num = 5:
# 1
# 2
# 6
# 24
# 120