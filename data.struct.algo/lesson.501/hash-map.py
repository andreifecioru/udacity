#!/usr/bin/env python

from collections import namedtuple

KV = namedtuple('KV', ['key', 'value'])

class BucketEntry:
    def __init__(self, kv):
        self.data = kv
        self.next = None



class HashMap:
    REHASH_THRESHOLD = 0.7

    def __init__(self, initial_size=10):
        self.bucket_array = [None for _ in range(0, initial_size)]
        # choose 31 or 37 for string keys
        self.p = 37
        self.num_elements = 0

    def put(self, key, value):
        self._rehash()
        
        hash_code = self._hash_code(key)
        bucket = self.bucket_array[hash_code]
        existing = HashMap._find_in_bucket(bucket, key)

        new_data = KV(key, value)

        if existing:
            existing.data = new_data
        else:
            new_entry = BucketEntry(new_data)
            self.num_elements += 1
            if not bucket:
                self.bucket_array[hash_code] = new_entry
            else:
                entry = bucket
                while entry.next:
                    entry = entry.next
                entry.next = new_entry

    def get(self, key):
        hash_code = self._hash_code(key)
        bucket = self.bucket_array[hash_code]
        target = HashMap._find_in_bucket(bucket, key)
        return target.data.value if target else None

    def delete(self, key):
        hash_code = self._hash_code(key)
        bucket = self.bucket_array[hash_code]

        if not bucket:
            return

        if bucket.data.key == key:
            self.bucket_array[hash_code] = bucket.next
            self.num_elements -= 1
            return

        entry = bucket.next
        prev = bucket
        while entry:
            if entry.data.key == key:
                prev.next = entry.next
                self.num_elements -= 1
                return
            entry = entry.next

    def size(self):
        return self.num_elements

    def _hash_code(self, key):
        # popular hash function for string keys
        code = sum([(lambda idx, letter: ord(letter) * self.p ** idx)(idx, letter) 
                    for idx, letter in enumerate(key)])
        # hash compression phase
        return code % len(self.bucket_array)

    def _rehash(self):
        load_factor = self.num_elements / len(self.bucket_array)
        if load_factor >= HashMap.REHASH_THRESHOLD:
            print('Re-hashing...')
            new_capacity = 2 * len(self.bucket_array)
            old_bucket_array = self.bucket_array
            self.bucket_array = [None for _ in range(0, new_capacity)]
            
            for entry in old_bucket_array:
                while entry:
                    self.put(**entry.data._asdict())
                    entry = entry.next

    @staticmethod
    def _find_in_bucket(bucket, key):
        if not bucket:
            return None

        entry = bucket
        while entry:
            if entry.data.key == key:
                return entry
            entry = entry.next

        return None

    def __repr__(self):
        ret_val = f'HashMap({self.size()})\n'
        for bucket in self.bucket_array:
            if not bucket:
                ret_val += '[]\n'
            else:
                ret_val += "["
                entry = bucket
                while entry:
                    ret_val += str(entry.data) + ', '
                    entry = entry.next
                ret_val = ret_val[:-2] + ']\n'

        return ret_val

if __name__ == '__main__':
    hashMap = HashMap()
    hashMap.put('one', 1)
    hashMap.put('two', 2)
    hashMap.put('three', 3)
    hashMap.put('four', 4)
    hashMap.put('five', 5)
    hashMap.put('six', 6)
    hashMap.put('seven', 7)
    hashMap.put('eight', 8)

    hashMap.delete('three')

    print(hashMap)

    print(hashMap.get('six'))