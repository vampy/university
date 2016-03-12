#!/usr/bin/python

def solution_found(x):
    print
    "*******Solution******"
    # str = ""
    # for i in x:
    #
    #     if i == -1:
    #         str += ")"
    #     else:
    #         str += "("
    # print str
    print
    x


# def solution(x):
#     return len(x) == DIM
#
# def consistent(x):
#     return len(x) == len(set(x))


# def solution(x):
#     return len(x) == DIM and sum(x) == 0
#
# def consistent(x):
#     # do not have more closed vs open parantheses
#     if sum(x) < 0:
#         return False
#
#     # v1 calculate all open parantheses and compare with N/2
#     op = 0
#     for i in x:
#         if i == 1:
#             op += 1
#
#     return op <= DIM/2


# def consistent(x):
#     # do not have more closed vs open parantheses
#     if sum(x) < 0:
#         return False
#
#     # v1 calculate all open parantheses and compare with N/2
#     # op = 0
#     # for i in x:
#     #     if i == 1:
#     #         op += 1
#     #
#     # return op <= DIM/2
#
#     # v2 the sum should not be bigger than the rest of remaing space
#     return sum(x) <= DIM - len(x)

# S = 12
# N = [1, 5, 10]
# def solution(x):
#     return sum(x) == S
#
# def consistent(X):
#     return sum(X) <= S

# def backtrack_recursive(x):
#     x.append(1)
#     for i in [1, -1]:
#         x[-1] = i
#         if consistent(x):
#             if solution(x):
#                 solution_found(x)
#             else:
#                 backtrack_recursive(x[:])
#         # el = -1

# def backtrack_recursive(x):
#     x.append(N[0])
#     for i in range(0, len(N)):
#         x[-1] = N[i]
#         if consistent(x):
#             if solution(x):
#                 solution_found(x)
#             else:
#                 backtrack_recursive(x[:])
#         # el = -1

# def backtrack_iterative():
#     x = [-1]
#     while len(x) > 0:
#         choosed = False
#         while not choosed and x[-1] < DIM - 1:
#             x[-1] += 1
#             choosed = consistent(x)  # or candidate(x)
#
#         if choosed:
#             if solution(x):
#                 solution_found(x)
#             else:
#                 x.append(-1)
#         else:  # cut the last element
#             x = x[:-1]

"""
8)
Generate all subsequences of length 2n+1, formed only by 0, -1 or 1, such that a1 = 0, ...,
a2n+1 = 0 and |a(i+1) - ai| = 1 or 2, for any 1 <= i <= 2n
"""
N = 1
DIM = 2 * N + 1


def solution_found(x):
    pr = ""
    for i in x:
        pr += str(i) + ", "
    print
    pr[:-2]


def solution(x):
    return len(x) == DIM


def consistent(x):
    len_x = len(x)
    if len_x <= 1:  # we have to few elements
        return x[0] == 0  # first element must be a zero

    # check last element
    if len_x == DIM and x[-1] != 0:
        return False

    # check the domain
    if x[len_x - 1] not in [-1, 0, 1]:
        return False

    # check last inserted
    value = abs(x[len_x - 1] - x[len_x - 2])
    return value == 1 or value == 2


def backtrack_recursive(x):
    x.append(0)
    for i in [0, -1, 1]:
        x[-1] = i
        if consistent(x):
            if solution(x):
                solution_found(x)
            else:
                backtrack_recursive(x[:])


def backtrack_iterative():
    x = [-2]
    while len(x) > 0:
        choosed = False
        while not choosed and x[-1] <= 0:
            x[-1] += 1
            choosed = consistent(x)  # or candidate(x)

        if choosed:
            if solution(x):
                solution_found(x)
            else:
                x.append(-2)
        else:  # cut the last element
            x = x[:-1]


print
"N = " + str(N)
print
"Recursive"
backtrack_recursive([])
print
"\n\n"
print
"Iterative"
backtrack_iterative()
