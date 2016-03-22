# http://python-future.org/compatible_idioms.html
from __future__ import print_function
from builtins import input

from fractions import gcd


def gcd_1(a, b):
    while a != b:
        if a > b:
            a -= b
        else:
            b -= a

    return a


def gcd_2(a, b):
    if a == 0:
        return b
    return gcd_2(b % a, a)


line = input("Give 2 numbers separated by ',': ").split(", ")
a, b = int(line[0]), int(line[1])
print("GCD(%d, %d) = %d" % (a, b, gcd(a, b)))
