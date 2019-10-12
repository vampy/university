# Statement
Considering a small programming language (that we shall call mini-language), you have to write a scanner (lexical analyzer). 
The assignment can be divided in two parts:

1. Mini-language Specification
The mini-language should be a restricted form of a known programming language, and should contain the following:
- 2 simple data types and a user-defined type
- statements:
	- assignment
	- input/output
	- conditional
	- loop
- some conditions will be imposed on the way the identifiers and constants can be formed.

2. Scanner implementation
	The scanner input will be a text file contained the source program, and will produce as output the following:
	- PIF - Program Internal Form
	- ST  - Symbol Table
In addition, the program should be able to determine the lexical errors, specifying the location, and, if possible, the type of the error.

The scanner assignment will be differentiated based on:
	1. Identifiers:
		a. length at most 8 characters
		b. arbitrary length, no more than 250 characters
	2. Symbol Table:
		a. unique for identifiers and constants
		b. separate tables for identifiers, respectively 
		   constants
	3. Symbol Table Organization:
		a. lexicographically sorted table
		b. lexicographically binary tree
		c. hashing table
		
This code implements: 1.a, 2.b, 3.c

# Usage
Run program by `python3 main.py <filename>`
Where `<filename>` is mandatory, it must be a valid .ry source code file.
See `code.ry` for an example.

# Implementation
1. Reads the whole code file.
2. Splits the file into lines by the new line character `\n`.
3. Split each line into tokens by whitespace.
4. Classify each split string. 
    - verify if it is a valid `Token` but not an identifier or constant
    - verify if identifier
    - verify if numeric constant
    - verify if char/string constant
    - try to split token into three parts by tokens that can not have space 
    near them, `left`, `center`, `right` and apply the classify token procedure recursively on each of them


# What each class does
The `Program` class holds the state of the program: PIF, symbol tables.
The `PIFTuple` tuple holds the token and position found in the symbol table.
The `HashTable` class is the hash implementation for the symbol table.
The `Token` enum maps each token name to an int.
The `TokenStrings` class helps for the conversion and identification of each string to a Token.

# Output
Symbol tables are represented as a hash table.

The program will output:
- the symbol table for the identifiers
- the symbol table for the constants
- the program internal form

