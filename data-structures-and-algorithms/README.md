# Problem
Find all the anagrams of a given word. An anagram of a word is the result of rearranging the letters of a word to produce a new word, using all the original letters, and having the same letter frequencies.

The general idea if of to solve the problem is to find all the anagrams in the from a large pool of words. The pseudocode for this solution would be:

Create empty hashmap `M<string, set<string>>`

Foreach word in file:
- Create a key thas is the word's letters sorted alphabetically (forced to lowercase)
- Add to the key the word we are processing

To check the anagrams of a word:
- Create a key that is the word's letters sorted alphabetically (forced to lowercase)
- Look up the key in the dictionary
- Iterate over the set of words (now we have all the anagrams, if any)
