#!/usr/bin/python
"""
Module by Butum Daniel - Group 911
"""


def selection(iterable, cmp_function):
    """
    First find the smallest element in the array and exchange it with the element in the first position,
    then find the second smallest element and exchange it with the element in the second position, a
    nd continue in this way until the entire array is sorted.
    Example:
        # Sort ascending
        li = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
        def cmp_function(a, b):
            if a == b:
                return 0
            elif a < b:
                return -1
            else:
                return 1
        li = selection(li, cmp_function)
        # li is now [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

        # for descending order use the following cmp_function
        def cmp_function(a, b):
            if a == b:
                return 0
            elif a < b:
                return 1
            else:
                return -1

    Input:
        iterable - list object
        cmp_function - the function used for comparing
    Output:
        list object sorted
    """
    len_iterable = len(iterable)
    for i in range(0, len_iterable - 1):
        min_index = i

        for j in range(i, len_iterable):
            # element in position j is smaller than element in position min_index
            if cmp_function(iterable[j], iterable[min_index]) == -1:
                min_index = j

        # swap
        if min_index != i:
            iterable[i], iterable[min_index] = iterable[min_index], iterable[i]

    return iterable


def shake(iterable, cmp_function):
    """
    The cocktail shaker sort is an improvement on the Bubble Sort. The improvement
    is basically that values "bubble" both directions through the array, because
    on each iteration the cocktail shaker sort bubble sorts once forwards and once backwards.
    Example:
        # Sort ascending
        li = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
        def cmp_function(a, b):
            if a == b:
                return 0
            elif a < b:
                return -1
            else:
                return 1
        li = shake(li, cmp_function)
        # li is now [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

        # for descending order use the following cmp_function
        def cmp_function(a, b):
            if a == b:
                return 0
            elif a < b:
                return 1
            else:
                return -1

    Input:
        iterable - list object
        cmp_function - the function used for comparing
    Output:
        list object sorted
    """
    range_index = range(len(iterable) - 1)
    swapped = True
    while swapped:
        swapped = False
        # go ascending
        for i in range_index:
            if cmp_function(iterable[i], iterable[i + 1]) == 1:
                # swap
                iterable[i], iterable[i + 1] = iterable[i + 1], iterable[i]
                swapped = True

        # we can exit the outer loop here if no swaps occurred.
        if not swapped:
            break

        swapped = False
        # go descending
        for i in reversed(range_index):
            if cmp_function(iterable[i], iterable[i + 1]) == 1:
                # swap
                iterable[i], iterable[i + 1] = iterable[i + 1], iterable[i]
                swapped = True

    return iterable
    # while True:
    #     for index in (range_index, reversed(range_index)):
    #         swapped = False
    #         for i in index:
    #             if cmp_function(iterable[i], iterable[i+1]) == 1:
    #                 # swap
    #                 iterable[i], iterable[i+1] = iterable[i+1], iterable[i]
    #                 swapped = True
    #
    #         if not swapped:
    #             return
