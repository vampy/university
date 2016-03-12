#!/usr/bin/python
"""
Module by Butum Daniel - Group 911
"""


def linear(iterable, search_value, key):
    """
    Perform a linear search on an iterable object
    Input:
        iterable - the search list
        search_value - the value to find
        key - the key to compare on
    Output:
        The list of items matching the search on
    """
    return_list = []

    for i in range(len(iterable)):
        method = getattr(iterable[i], key)
        type_method = str(type(method))
        if type_method == "<type 'method-wrapper'>" or type_method == "<type 'instancemethod'>":  # is a method
            attribute = method()
        else:  # is a simple attribute
            attribute = method

        if attribute == search_value:
            return_list.append(iterable[i])

    return return_list


def binary(iterable, item_function):
    pass
