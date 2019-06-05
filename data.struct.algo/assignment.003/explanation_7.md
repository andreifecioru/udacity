Another basic trie implementation similar with what I have implemented for problem-5. The backing data structure for each node is a dictionary: the key is the path element and the value is the corresponding node. The same complexity considerations apply: to find a handler for an input path is an `O(n)` operation (where `n` is the number of path elements in the input path).