The solution is yet another variation on the binary search algorithm. The twist in this case is an efficient way of finding the index of the so-called _pivot_ (i.e. the index of the lowest value in the array).

If the pivot is 0 (i.e. the lowest value is at the beginning of the array) we end up with a classic binary search problem (which is `O(log(n))`).

Otherwise we divide the problem into 2 sub-problems:
  - if the target is larger than the first element in the array, we know we have to do a binary search to the **left** of the pivot
  - if the target is smaller than the first element in the array, we must perform a binary search to the **right** of the pivot

In both cases we end up with `O(log(n))` complexity.

All that remains now is to find a way to compute the index of the pivot in `O(log(n))` complexity. Finding the index of the pivot is in itself a variation of the binary search algorithm. We keep dividing the input array in half in search for a "breaking point" (i.e. a point where the value at the lower index is **larger** than the value and the higher index). This is `O(log(n))`.

In conclusion, we have managed to break the original problem into 3 sub-problems each with an `O(log(n))` complexity yielding an overall complexity of `O(log(n))`.