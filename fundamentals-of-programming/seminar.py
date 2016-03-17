"""
SEMINAR
"""

"""
insertion sort
Best Case: T(n) (e) O(n)
Worst Case: n(n-1)/n (e) O(n^2)
"""


def sort_insertion(li):
    len_list = len(li)

    for i in range(1, len_list):
        current_value = li[i]
        hole_pos = i

        # shift list
        while hole_pos > 0 and li[hole_pos - 1] > current_value:
            li[hole_pos] = li[hole_pos - 1]

            hole_pos -= 1

        # insert in right position
        li[hole_pos] = current_value

    return li


"""
pseudocode:

alg oper(n , i):
    if n > 1:
        i = 2 * i
        m = [n/2]
        
        oper(m, i - 2)
        oper(m, i - 1)
        oper(m, i + 2)
        oper(m, i + 1)
    else:
        print;
end

T(n) = (constant) = 1, n = 1
T(n) = 4 * T(n/2) + 2

T(n) = 4*T(n/2) + 2
T(n/2) = 4 * T(n/4) + 2
T(n/4) = 4 * T(n/8) + 2

T(n) = 4*(4*T(n/4) + 2) = 2^4*T(n/2^2) + 2^3 + 2^1
n = 2^k

T(4) = 4^k*T(n/2^k) + 2*(sum 4, i=0 to k) = 2* (4-1)/(4+1)

T(n) = 2/3(n^2 - 1) + ... (e) O(n^2)

"""

"""
Binary search

T(n) = 1 + .... + 1 + T(2^(k-2)) = k
T(n/2) = 1 + T(n/4)

n = 2^k

T(n) (e) O(log(2)  n)
"""


def binary_search_recusive(li, search_value, left, right):
    if left > right:
        return False

    middle = (left + right) / 2

    if search_value == li[middle]:
        return True

    # element in lower bound
    if li[middle] > search_value:
        return binary_search_recursive(li, search_value, left, middle - 1)

    # element in upper bound
    if li[middle] < search_value:
        return binary_search_recursive(li, search_value, middle + 1, right)


"""
Sorting in python
"""
s = [("bea", "A"), ("adrian", "B"), ("jan", "B")]
sorted(s, key=lamda
t: t[1], reverse = False)


# We have a list of persons
def cmp_age(a, b):
    if a.age < b.age:
        return -1
    elif a.age > b.age:
        return 1
    else:
        return 0


l = []
sorted(l, cmp=cmp_age)


# or use operator overloading def __cmp__() <- this is the python way

# to use set() on objects we use def __hash__()
# eg
def __hash__(self):
    return self.__id


def __eq__(self, other):  # overload '='
    pass


# if a == b => a.__hash__ == b.__hash__

def __repr__(self):
    return self.__str__()


# iterator over an object
class MyRepo(object):
    def __init__(self, li):
        self._li = li

    def __iter__(self):
        self.poz = 0

        return self

    def __next__(self):
        if self.poz > len(self._li):
            self.poz += 1

        return self._li[self.poz]


repo = MyRepo([])
for client in repod:
    print(client)

"""
Magic methods for lists
__getItem__
__setItem__
__delItem__
works on a list
a[0] or a[0] = 2 or del a[0]
"""

"""
SEMINAR
"""

"""
- greedy method
    - follows local optimum
    - complete in a reasonable time(good T(n))
    - might not get optimum solution

* Making change problem
67 bani = 50 + 10 + 5 + 1 + 1
[1, 7, 10]
15 = 10 + 5 x 1
15 = 2 x 7 + 1
"""
coins = [1, 5, 10, 50]


def make_change(n, coins):
    # n - the change we want back
    change = []
    while n > 0:
        p = len(coins) - 1
        # we do not want bigger coins than our change
        while coins[p] > n and p > 0:
            p -= 1

        n -= coins[p]
        change.append(coins[p])

    return change


"""
T(n) (e) O(n)
"""


def test_make_change():
    for i in range(1, 100):
        assert sum(make_change(i, coins)) == i


"""
* Traveling salesman
    - travel though all the cities
, 
* Scheduling problem
    - 1 CPU -> 3 code
    e.g: 3, 5, 6, 10, 11, 14, 15, 18, 20
    A | 20->10->3
    B | 18->11->5
    C | 15->14->6 | 35
"""

"""
- Divide et Impera(Divide and Conquer)
    - divide
    - conquer
    - combine

* smallest nr in list
"""


def detimpera_min(li):
    if len(li) == 1:
        return l[0]

    middle = len(li) / 2
    return min(deimpera_min(l[:m]), detimpera_min(l[m:]))
    # or
    return min(l[0], detimpera_min(l[1:])

    """
    * gcd of a list of numbers
    """


def gcd(a, b):
    while a != b:
        if a < b:
            a -= b
        else:
            b -= a
    return a


def dei_gcd(li):
    if len(li) == 1:
        return li[0]

    middle = len(li) / 2
    return gcd(dei_gcd(li[:m]), dei_gcd(li[m:])


def test_dei_gcd():
    m = {}
    m[2] = [2, 6]
    m[1] = [5, 6]
    for k in m, keys():
        assert dei_gcd(m[k]) == k


"""
* Max SubArray
[-2, -5, 6, -2, -3, 1, 5, -6]
         | the sum is 7|

"""


def max_subarray_1(li):
    max = l[0]
    for i in range(0, len(li)):
        for j in range(i, len(li)):
            s = 0
            for k in range(i, j + 1):
                s += li[k]
                if s > max:
                    max = s

    return max


"""
T(n) (e) O(n^3)
"""


def max_subarray_2(li)
    max = l[0]
    for i in range(0, len(li)):
        s = 0
        for j in range(i, len(li)):
            s += li[j]  # the sum between i and j(aka the subarray)
            if s > max:
                max = s

    return max


"""
T(n) = O(n^2)
"""


def max_subarray_3(array, low, high):
    if low == high:
        return array[low]

    middle = (low + high) / 2

    return max(max_subarray_3(array, low, middle),
               max_subarray_3(array, middle, high),
               center_sum(array, low, high))


def center_sum(array, low, high):
    sum = 0
    middle = (low + high) / 2
    left_sum = -1000  #
    i = middle
    while i > l:
        sum += l[i]
        i -= 1

        if sum > left_sum:
            left_sum = sum

    # the same idea for right sum

    return left_sum + right_sum


"""
T(n) = 2*T(n/2) + n 
T(n/2) = 2*T(n/4) + n/2

T(n) = 2*[2*T(n/4) + n/2] + n
T(n) = 2^2 * T(n/2^2) + 2n
n = 2^k'
= 2^k + k
T(n) (e) O(nlog(n))
"""

"""
* n (e) N, r (e) N,  p (e) R given
a = ?
a^r (e) [n - p, n + p]

example:

2^1/7 , p = 0,0001

which is a ?
a^7 = 2

1^7 = 1
2^7 = 128

a (e) [1, 2]

1.5^2 > 2

a (e) [1,15]
"""


def recursive_root(n, a, low, high):
    if high - low < a:
        return n

    middle = (low + high) / 2

    if a ** r < n ** r:
        return recursive_root(n, a, low, middle)
    else:
        return recursive_root(n, a, middle, high)


recursive_root(2, 2, 0, 2)

"""
Backtracking method
    - find all solutions
    - search space
    - slow

Recipe:
    1. represent problem as a vector X = [X0, ..., Xn]
    2. Find candidate(partial solution)
    3. Find solution

Permutation problem.
n = 4 => 4!

x = [1, 2, 3, 4] (can be represented as a tree)
"""


def solution(x):
    return len(x) == N  # N the number of permutations


def candidate(x):
    return len(x) == len(set(x))


def backtrack_recursive(x):
    x.append(0)
    for i in range(0, DIM):
        x[len(x) - 1] = i
        if consistent(x):  # or candidate(x)
            if solution(x):
                found(x)
            else:
                backtrack_recursive(x[:])


def backtrack_iterative():
    x = [-1]
    while len(x) > 0:
        c = False
        while c == False and x[len(x) - 1] < DIM - 1:
            x[len(x) - 1] += 1
            c = consistent(x)  # or candidate(x)
    if c:
        if solution(x):
            found(x)
    else:
        x.append(-1)
    else:
    x = x[:-1]


"""
Queen problem
indexes are columns and values represent the row

N x N
eg: x =[2, 4, 1, 3]
column 0 row 2
column 1 row 4
column 3 row 1
column 4 row 3
"""


def solution(x):  # same as previous
    pass


def consistent(x):  # or candidate
    # the columns are handled by the indexes

    # handle line
    if len(x) != len(set(x)):
        return False

    # handle the diagonal
    # check the last queen inserted
    for c in range(0, len(x) - 1):
        if abs(x[c] - x[len(x) - 1]) == len(x) - c - 1:
            return False

    return True


"""
Latin squares
N = 3
| A | B | C |
| B | C | A |
| C | A | B |
represnt the matix as a vector
A = 1
B = 2
C = 3
X = [1, 2, 3,   2, 3, 1,   3, 1, 2]
    1 line      2 line     3 line
x[0:3] - 1 line
x[3:6] - 2 line
"""


def solution(x):
    return len(x) == N ** 2


def consistent(x):
    # handle line
    for i in range(0, N ** 2, N):
        if len(set(x[i:i + N])) != len(set(x[i:i + N])):
            return False

    # handle columns
    # check all elements
    for c in range(0, N):
        # we build the column
        col = []
        while j < len(x):
            col.append(x[j])
            j += N

        # check if ok
        if len(col) != len(set(col)):
            return False

    return True


"""
Closing parantheses
N = 4
(()) -> X = [1, 1, -1, -1]
()() -> X = [1, -1, 1, -1]
( - 1
) - -1
"""


def solution(x):
    return len(x) == N and sum(x) == 0


def consistent(x):
    # do not have more closed vs open parantheses
    if sum(x) < 0:
        return False

    # v1 calculate all open parantheses and compare with N/2
    op = 0
    for i in x:
        if i == 1:
            op += 1

    return op <= N / 2

    # v2 the sum should not be bigger than the rest of remaing space
    return sum(x) <= N - len(x)


"""
Dynamic programming
    - split problem
    - solve subproblem once
    - memorization

Fibonnacii
m = {}
m[0] = 0
m[1] - 1
"""


# linear complexity O(n)
def fibonaci_memory(n):
    if n not in m.keys():
        m[n] = fibonaci_memory(n - 1) + fibonaci_memory(n - 2)

    return m[n]


"""
Checkerboard problem
| 2 | 3 | 4 | 1 | 3 |
| 9 | 8 | 1 | 1 | 2 |
| 5 | 8 | 9 | 1 | 3 |
| 1 | 2 | 1 | 1 | 2 |
| 1 | 2 | x | 4 | 3 |

we can move up and on diagonals

we build the cost matrix
| 15 | 10 | 90 | 4 | 15 |
|  |  |  |  |  |
| 6 |  |  |  |  |
| 1 | 3 |  |  |  |
| 1 | 2 | x | 4 | 3 |
"""

"""
x = [-2, -5, 6, -2, -3, 1, 5, -6]
             |j     7      |
greedy - O(n^2)
divide and conquer - O(nlog(n))
dynamic programming - O(n)
"""
M = []


# Mj = max{Mj-1 + Xj, Xj}
# Mj-1 + Xj > Xj
# Mj-1 > 0
# -5 + 6 > 6

def max_sub(li):
    len_li = len(li)
    if not len_li:
        return 0

    sum_li = 0
    M = []
    M.append(l[0])  # the first limit
    T = []
    T.append(0)  # remember the indexes
    # sum_li = l[0]
    # start = 0
    # end = 0
    for i in range(1, len_li):
        if M[i - 1] > 1:  # start over
            M.append(M[i - 1] + l[i])
            T.append(T[i - 1])
        else:  # continue to append
            M.append(l[i])
            T.append(i)
        if M[i] > sum_li:
            sum_li = M[i]
            start = T[i]
            end = i


"""
At exam possible:
Product of prime number of list
"""


# Return True if prime
def is_prime(n):
    if n < 2:
        return False

    for i in range(2, n / 2):
        if n % i == 0:
            return False  # not prime found divisor

    return True


def prime_li(li):
    len_li = len(li)
    if len_li == 1:
        if is_prime(l[0]):
            return l[0]
        else:
            return 1  # the neutral element

    return prime_li(l[:len_li / 2]) * prime_li(l[len_li / 2:])


"""
Specify
test
n (e) N
Gollbach conjecture
8 = 3 + 5
10 = 3 + 7
   = 5 + 5
   
"""
"""
Veerify if n can be written as sum of primes
Input:
    n (e) N, n is even
Output:
    a list of [n1, n2] with n1, n2 primes
Raises:
    ValueError if n is odd or n is not a naturoa number
"""


def gb(n):
    if not isinstance(n, int) or n < 0 or n % 2 == 0:
        raise ValueError()

    for i in range(2, n):
        if is_prime(i) and is_prime(n - i):
            return [i, n - i]

    raise BadConjecture()


def test_gb():
    try:
        gb(1)
        assert False
    except ValueError:
        assert True

    assert gb(10) == [3, 7]
    assert gb(20) == [3, 17]
