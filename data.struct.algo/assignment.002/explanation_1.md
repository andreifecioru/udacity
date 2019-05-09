To keep the complexity in check, we maintain 2 data structures in our LRU implementation:
  - a dictionary which keeps an association between key/value pairs. This ensures that we can get the value corresponding to any key in the cache in O(1) time. 

    NOTE: the "value" part of the k/v pair is actually a reference to a node element in the doubly-linked list (see below).
  
  - a doubly linked list which helps us keep track of the recency of usage of various keys in the dictionary. We're using a linked-list here because we want to be able to perform insertions and removal of nodes in O(1) time.

The overall algorithm goes as follows:
  - for GET operations:
    - if the key is not in the dictionary we return -1. Checking that a key is present is an O(1) operation
    - if the key present in the dictionary:
      - we retrieve the node reference from the dictionary and from that the value we are looking for (this is O(1))
      - we promote the referenced node to the front of the list (since this is a doubly linked-list the insert/removals are O(1) operations). In this manner, the most recently accessed entries are bubbled to the front of the list and the least recently accessed entries are pushed to the tail of the list.

  - for the SET operations:
    - if the key is in the dictionary, we simply replace the value of the node referenced by the dictionary key with the new value (O(1) complexity)
    - if the key is not in the dictionary:
      - if we've not yet reached capacity, we can simply add a new key to the dictionary (i.e. O(1)) and add the newly inserted value to the front of the list (again O(1))
      - if we reached max cache capacity, we make room by removing the least recently used entry (which we know it is at the tail of the list) both from the dictionary and the list (that is O(1)) and then we perform the new entry insertion as above (O(1)).

So, in all cases all operations take constant execution time (i.e. O(1) time complexity). The tradeoff here is space complexity: we must consume memory to keep both the dictionary and the linked list.