#!/usr/bin/env python
'''
Created on Oct 13, 2013
Problem number 3

@author: Daniel Butum, Group 911
'''

import datetime


def get_read_data():
    """
    Read the data from the user and validates it.

    @return: a tuple representing the year and the day.
    """
    # return 1994, 200

    year = raw_input("Input the year: ")
    day = raw_input("Input the day of the year: ")
    return year, day


# run it as a script 
if __name__ == "__main__":
    year, day = get_read_data()

    # use string formating

    # handle years in the shorthand notation from [00, 99],
    year_format = "%y" if int(year) < 100 else "%Y"

    d = datetime.datetime.strptime(year + " " + day, year_format + " %j")

    # use timedelta
    # d = datetime.datetime(int(year), 1, 1) + datetime.timedelta(int(day) - 1)

    print "The time in format Year/Month/Day: " + d.strftime("%Y/%m/%d")
