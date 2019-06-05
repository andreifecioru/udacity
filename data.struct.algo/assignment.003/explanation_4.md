The core idea for this algorithm is to loop once through all the elements of the array and let the smaller elements "sink" to the beginning of the array and the large elements "float" to the end of the array. For this purpose we need to keep track of three indexes:
  - the upper index of the "small elements" section of the array (initially 0)
  - the lower bound of the "large elements" section of the array (initially the last index of the array)
  - the index of the current element that is being processed (i.e. the cursor)

When we encounter a "small" value we swap it with the element at the upper bound of the "small elements" section and **increase** the corresponding index.
When we encounter a "large" value we swap it with the element at the lower bound of the "large elements" section and **decrease** the corresponding index.
When we encounter a "mid" value we do nothing and increase the cursor.

Since we do it all in a single pass, we get an `O(n)` overall complexity.