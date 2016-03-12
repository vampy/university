#!/usr/bin/python
"""
Created on Oct 18, 2013

@author: daniel
"""

EXPENSE_TYPES = ['gas', 'heating', 'illuminating', 'others', 'water']


class Apartment():
    # this list is sorted in ascending order
    def __init__(self, water=0, heating=0, illuminating=0, gas=0, others=0):
        self.expenses = {
            "gas": gas,
            "heating": heating,
            "illuminating": illuminating,
            "others": others,
            "water": water
        }

    def get_total_expenses(self):
        """
        Get the total expenses
        Return:
            float representing the total
        """
        return sum(self.expenses.values())

    def get_max_expenses_type(self):
        """
        Gets the biggest expense in this apartment
        Return:
            a list of the biggest expenses types
        """
        keys = self.expenses.keys()
        biggest_types = [keys[0]]
        keys.pop(0)  # remove first element
        for key in keys:
            if self.expenses[key] > self.expenses[biggest_types[0]]:  # greater
                biggest_types = [key]
            elif self.expenses[key] == self.expenses[biggest_types[0]]:  # equal
                biggest_types.append(key)

        if self.expenses[biggest_types[0]] <= 0:
            return []

        return biggest_types

    @staticmethod
    def from_dictionary(dictionary):
        """
        Creates a new Apartment Object from a dictionary
        Return:
            A new Apartment object
        Raises:
            Exception on incompatibility
        """
        if "expenses" in dictionary and sorted(dictionary["expenses"]) == sorted(EXPENSE_TYPES):
            apartment_temp = Apartment()
            apartment_temp.expenses = dictionary["expenses"]
            return apartment_temp

        raise Exception("dictionary is not compatible with Apartment")

    @staticmethod
    def to_dictionary(apartment_obj):
        """
        Creates a dictionary from an Apartment
        Return:
            dictionary
        Raises:
            Exception on wrong object
        """
        if isinstance(apartment_obj, Apartment):
            return {"expenses": apartment_obj.expenses}

        raise Exception("apartment_obj is not of type Apartment")

    @staticmethod
    def is_expense_type(expense_type):
        """
        @return: True if expense_type is valid and False otherwise
        """
        return str(expense_type) in EXPENSE_TYPES
