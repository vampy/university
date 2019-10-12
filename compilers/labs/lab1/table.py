

# Simple mock table implemented on a list
class SymbolTable:
    def __init__(self):
        self._table = []
        self._current_pos = 0

    def find(self, string):
        # search of value
        for i, val in enumerate(self._table):
            if string == val:
                return i  # pos

        return -1

    def has(self, string):
        return self.find(string) != -1

    def position(self, value):
        # only strings
        value = str(value)

        pos = self.find(value)
        if pos != -1:
            return pos

        # Insert value
        self._table.append(value)
        pos = self._current_pos
        self._current_pos += 1
        return pos


class HashTable:
    def __init__(self, length=64):
        self._buckets_len = length
        self._buckets = [[] for _ in range(self._buckets_len)]
        self._nr_entries = 0

    def get_from_bucket(self, bucket_index, key):
        key_index = self._find_in_bucket(bucket_index, key)
        if key_index == -1:
            return None

        return self._buckets[bucket_index][key_index]

    def get(self, key):
        bucket_index = self._hash_index(key)
        key_index = self._find_in_bucket(bucket_index, key)
        if key_index == -1:  # Not found
            return None

        return self._buckets[bucket_index][key_index]

    def put(self, key, value):
        bucket_index = self._hash_index(key)
        key_index = self._find_in_bucket(bucket_index, key)

        if key_index == -1:  # create key
            self._buckets[bucket_index].append((key, value))
        else:  # update key
            self._buckets[bucket_index][key_index] = (key, value)

    def position(self, key):
        # only strings
        key = str(key)

        # already in table
        bucket_index = self._hash_index(key)
        key_index = self._find_in_bucket(bucket_index, key)
        if key_index != -1:
            return bucket_index

        # insert in table
        self._buckets[bucket_index].append((key, None))
        return bucket_index

    def has(self, key):
        return self._find_in_bucket(self._hash_index(key), key) != -1

    def is_empty(self):
        return self._nr_entries == 0

    def _find_in_bucket(self, bucket_index, search_key):
        for i, (key, value) in enumerate(self._buckets[bucket_index]):
            if search_key == key:
                return i

        return -1

    def _hash_index(self, key):
        return self._hash(key) % self._buckets_len

    def _hash(self, key):
        # return hash(key)
        sum_ascii = 0
        for i in key:
            sum_ascii += ord(i)

        return sum_ascii % self._buckets_len

    def __str__(self):

        output = []
        for i in range(self._buckets_len):
            if not self._buckets[i]:
                continue

            output.append('{0}: {1}'.format(i, self._buckets[i]))

        return "\n".join(output)
