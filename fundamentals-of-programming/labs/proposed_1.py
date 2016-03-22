# http://python-future.org/compatible_idioms.html
from __future__ import print_function
from builtins import input

s = 0
for i in range(1, int(input("Give a number: ")) + 1):
    s += i

print("The sum is " + str(s))
