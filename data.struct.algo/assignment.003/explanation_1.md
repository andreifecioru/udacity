The solution is a variation of the binary search algorithm. 

When looking for the square root of value `X` one can re-formulate the problem as follows: given a sequence of consecutive values in range `(0, X)` find the one value `V` for which `V * V == X`. That value `V` is the square root. 

Since the sequence of consecutive numbers are by definition sorted, we can use the binary search approach coupled with the `V * V == X` predicate. This results in an algorithm of `O(log(n))` complexity.