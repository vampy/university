#!/usr/bin/python
"""
Module by Butum Daniel - Group 911
"""


def filter(iterable, filter_function):
    """
    Filter an iterable object(e.g a list) by a user supplied function
    Example:
        numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        def filter_function(x):
            return x % 2 == 0         # all even numbers
        numbers = filter(numbers, filter_function)
        # numbers is now [2, 4, 6, 8, 10]

    Input:
        iterable - the object to apply the filter on
        filter_function - the function to apply to every element in the iterable object.
                          It should return True if the current element in the iterable object
                          is correct
    Return:
        list - representing the elements that correspond to the filter
    """
    return_iterable = []
    for li in iterable:
        if filter_function(li):
            return_iterable.append(li)

    return return_iterable
