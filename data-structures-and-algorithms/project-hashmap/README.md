# Problem
Problem number: Set 2. Nr. 17. Map

Find all the anagrams of a given word. An anagram of a word is the result of rearranging the letters of a word to produce a new word, using all the original letters, and having the same letter frequencies.

The general idea if of to solve the problem is to find all the anagrams in the from a large pool of words. The pseudocode for this solution would be:

Create empty hashmap `M<string, set<string>>`

For each word in file:
- Create a key that is the word's letters sorted alphabetically (forced to lowercase)
- Add to the key the word we are processing

To check the anagrams of a word:
- Create a key that is the word's letters sorted alphabetically (forced to lowercase)
- Look up the key in the dictionary
- Iterate over the set of words (now we have all the anagrams, if any)

## Example
We have two words: `mary` and `army`.

The key of `mary` would be `amry` and the key of `army` would be `amry`.
So map would have the:
```
key => list of words
“amry” => [mary, army]
```

# Map ADT
## Type: `Map<TKey, TValue>`
## Methods
```
Function get(key)
    Description: get a value by key   
    Data: TKey – key  
    Prec: key should be of type TKey
    Post: TValue or raise exception otherwise
    Complexity: O(1) average, O(n) worst case

Subalg put(key, value):
    Description: Create a new key with the value, or replace the current key with the new value if that key already exists.
    Data: TKey - key
          Tvalue – value
    Prec: key should be of type TKey and value of type Tvalue
    Complexity: O(1) average, O(n) worst case

Subalg remove(key)
    Description: Remove a map entry based on the key.
                 If the key does not exist, do nothing.
    Data: TKey – key
    Prec: key should be of type Tkey
    Complexity: O(1) average, O(n) worst case

Function containsKey(key)
    Description: Checks if the key is in the map
    Data: Tkey – key
    Prec: key should be of type Tkey
    Post: true if the key exists, false otherwise
    Complexity: O(1) average, O(n) worst case

Function getLength()
    Description: Get the number of entries in the map
    Post: the number of entries as an unsigned integer
    Complexity: O(1) average, O(n) worst case

Subalg clear()
    Description: Clear all the entries in the map
```

## Pseudocode for the hashtable representation
```
Function get(key)
    hash_index := hashIndex(key)
    bucket_index := findInBucket(hash_index, key)
    if bucket_index = -1 then
        return
    endif

    get := buckets[hash_index][bucket_index]
endget

Sublag put(key, value)
    hash_index := hashIndex(key)
    bucket_index := findInBucket(hash_index, key)
    if bucket_index = -1 then
        nrEntries := nrEntries + 1
        pushBack(buckets[hash_index], new Entry(key, value))
    endif

    buckets[hash_index][bucket_index].value = value
endput

Subalg remove(key)
    hash_index := hashIndex(key)
    bucket_index := findInBucket(hash_index, key)    
    if bucket_index != -1 then
        nrEntries := nrEntries – 1
        erase(bucket[bucket_index])
    endif
endremove

Function containsKey(key)
     containsKey := (findInBucket(hashIndex(key), key) != -1)
endcontainsKey
```

# Other ADT's used
# Type: `DynamicArray<TElement>`
## Methods
```
Subalg add(element)
    Description: Adds an element in the vector, to the end
    Data: TElement - element

Subalg insert(position, element)
    Description: Insert an alement at a given position
    Data: unsigned int – position
          TElement – element

Function get(position)
    Description: Get element from vector from a given position
    Data: unsigned int – position
    Post: TElement or raise exception if position is invalid

Subalg set(position, element)
    Descrption: Set the given position with the value of element
    Data: unsigned int – position
          TElement – element

Subalg remove(position)
    Description: Remove element at a given position
    Data: unsigned int – position

Function getLength()
    Description: get the length of the vector
    Post: unsigned int - the number the elements

Function getCapacity()
    Description: Get the total allocated capacity for the vector
    Post: unsigned int - the allocated capacity
```

# Benchmark
Time and memory comparison by using representation (dynamic vector) or representation 2 (hash table collision through chaining).

Dictionaries used:
- `words.txt` - the [unix words files](https://en.wikipedia.org/wiki/Words_(Unix)).
- `english-words.95` - somewhere on sourceforge? (TODO find)

## Dynamic Vector
| Data set size (nr words)   | Time          | Memory  |
| -------------------------- | ------------- | ------- |
| ~ 1000                     | < 1 second    | ~ 1 MB  |
| 99171 (words.txt)          | ~ 4 minutes   | ~ 20 MB |
| 2457044 (english-words.95) | ~ 20 minutes  | ~ 45 MB |

## Hash table
| Data set size (nr words)   | Time          | Memory  |
| -------------------------- | ------------- | ------- |
| ~ 1000                     | < 1 second    | ~ 1 MB  |
| 99171 (words.txt)          | ~ 1.5 seconds | ~ 25 MB |
| 2457044 (english-words.95) | ~ 1.6 seconds | ~ 55 MB |

# What did I learn from this project?
As clearly shown in the benchmarks above the best suited data representation is a hash table. And the tables below show the hash table has perfect timing when considering the average column.

The map ADT is clearly the most suited for this type of problem, because it allow fast data access.

## Dynamic Vector
|        | Average | Worst case |
| ------ |:-------:|:----------:|
| Search | O(n)    | O(n)       |
| Insert | O(n)    | O(n)       |
| Delete | O(n)    | O(n)       |

## Hash table
|        | Average | Worst case |
| ------ |:-------:|:----------:|
| Search | O(1)    | O(n)       |
| Insert | O(1)    | O(n)       |
| Delete | O(1)    | O(n)       |
