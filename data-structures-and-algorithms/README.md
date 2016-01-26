# Problem
Problem nr: Set 2. Nr. 17. Map

Find all the anagrams of a given word. An anagram of a word is the result of rearranging the letters of a word to produce a new word, using all the original letters, and having the same letter frequencies.

The general idea if of to solve the problem is to find all the anagrams in the from a large pool of words. The pseudocode for this solution would be:

Create empty hashmap `M<string, set<string>>`

For each word in file:
- Create a key thas is the word's letters sorted alphabetically (forced to lowercase)
- Add to the key the word we are processing

To check the anagrams of a word:
- Create a key that is the word's letters sorted alphabetically (forced to lowercase)
- Look up the key in the dictionary
- Iterate over the set of words (now we have all the anagrams, if any)

# Map ADT
Type: `Map<TKey, TValue>`

Methods:
```
Function get(key)
    Description: get a value by key   
    Data: TKey – key  
    Prec: key should be of type TKey
    Post: TValue or raise exception otherwise
    Complexity: O(1) average, O(n) worst case

Subalg put(key, value):
    Description: Create a new key with the value, or replace the current key with the new value  if that key already exists.
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
