#!/usr/bin/python
"""
Created on Oct 18, 2013

@author: daniel
"""
import re

from apartment import Apartment
from ui import UI


class Bloc():
    # the regex patterns used
    _PATTERN_INSERT = "^insert\s+([-+]?[0-9]*\.?[0-9]+)\s*,\s*([a-zA-Z]+)\s+at\s+(\d+)$"
    _PATTERN_REPLACE = "^replace\s+([-+]?[0-9]*\.?[0-9]+)\s*,\s*([a-zA-Z]+)\s+at\s+(\d+)$"

    _PATTERN_REMOVE_BY_ID = "^remove\s+(\d+)$"
    _PATTERN_REMOVE_BY_INTERVAL = "^remove\s+from\s+(\d+)\s+to\s+(\d+)$"
    _PATTERN_REMOVE_BY_TYPE = "^remove\s+([a-zA-Z]+)$"

    _PATTERN_LIST_BY_TYPE = "^list\s+([a-zA-Z]+)$"
    _PATTERN_LIST_GREATER = "^list\s+greater\s+than\s+([-+]?[0-9]*\.?[0-9]+)$"
    _PATTERN_LIST_LESS = "^list\s+less\s+than\s+([-+]?[0-9]*\.?[0-9]+)\s+before\s+(\d+)$"
    _PATTERN_LIST_TOTAL_APARTMENT = "^list\s+(?:total|sold)\s+(\d+)$"

    _PATTERN_STAT_TOTAL_TYPE = "^sum\s+([a-zA-Z]+)$"
    _PATTERN_STAT_MAX_APARTMENT = "^max\s+(\d+)$"

    _PATTERN_SORT = "^sort\s+(?:desc|asc)$"
    _PATTERN_SORT_BY_TYPE = "^sort\s+(?:desc|asc)\s+(\w+)$"

    _PATTERN_FILTER_BY_TYPE = "^filter\s+(\w+)$"
    _PATTERN_FILTER_GREATER_THAN_TOTAL = "^filter\s+([-+]?[0-9]*\.?[0-9]+)$"

    @staticmethod
    def from_dictionary(dictionary):
        """
        Creates a new Apartment Object from a dictionary
        Input:
            dictionary - a dictionary compatible with Bloc
        Return:
            A new Bloc object
        """
        new_dict = {}
        for apartment_id in dictionary:
            new_dict[apartment_id] = Apartment.from_dictionary(dictionary[apartment_id])

        return Bloc(new_dict)

    @staticmethod
    def to_dictionary(bloc_obj):
        """
        Creates a dictionary from an Bloc object
        Input:
            bloc_obj - a object instance of Bloc
        Return:
            a new dictionary representing the object
        Raises:
            Exception on wrong object
        """
        if isinstance(bloc_obj, Bloc):
            bloc_dict = bloc_obj.get()
            for apartment_id in bloc_dict:
                bloc_dict[apartment_id] = Apartment.to_dictionary(bloc_dict[apartment_id])

            return bloc_dict

        raise Exception("bloc_obj is not of type Apartment")

    def __init__(self, bloc_dict={}):
        """
        The constructor for the bloc object
        Input:
            bloc_dict - a bloc dictionary used for the container
        Output: -
        """
        self._bloc_dict = bloc_dict
        self._command = ""
        self._parsed_command = {}

    def _reset_command(self):
        """
        Resets all the command related proprietes
        """
        self._command = ""
        self._parsed_command = {}

    def _regex_search(self, regex_pattern):
        """
        Generic method used for regex search
        Input:
            regex_pattern - the regex pattern used
        Return:
            a new regex object
        Raises:
            Exception on invalid command
        """
        if not self._command:
            raise Exception("command is empty cannot apply regex")
        return re.search(regex_pattern, self._command, re.MULTILINE)

    def _validate_parsed_command(self, list_of_keys):
        """
        Checks if _parsed_command has the keys from the parameter
        Input:
            list_of_keys - the list of keys
        Raises:
            Exception on invalid key
        """
        # if not isinstance(list_of_keys, list):
        #    raise Exception("Incorrect list_of_keys. should be a list.")

        for key in list_of_keys:
            if key not in self._parsed_command:
                raise Exception("Incorrect parsed_command(key '%s' not in list)" % key)

    def get(self):
        """
        Get the bloc of apartments
        Return:
            dictionary copy representing the bloc
        """
        return self._bloc_dict.copy()

    def set_command(self, command):
        """
        Set the command given by the user
        """
        self._reset_command()
        self._command = command

    def is_apartment(self, apartment_id):
        """
        Finds out if a specific apartment is already in the list
        Return:
            True if is in list or  False othewise
        """

        return str(apartment_id) in self._bloc_dict

    def insert_apartment(self):
        """
            Insert a new apartment into the block
        """
        self._validate_parsed_command(["id", "type", "amount"])
        apartment_id = self._parsed_command["id"]
        expense_type = self._parsed_command["type"]
        amount = self._parsed_command["amount"]

        # apartment does not exist in list create a new empty one
        if not self.is_apartment(apartment_id):
            self._bloc_dict[apartment_id] = Apartment()

        self._bloc_dict[apartment_id].expenses[expense_type] = float(amount)

    def insert_apartment_parse(self, command):
        """
        Parses and validates the insert command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        return self._insert_or_replace_apartment_parse(command, "insert")

    def replace_apartment_parse(self, command):
        """
        Parses and validates the replace command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        return self._insert_or_replace_apartment_parse(command, "replace")

    def _insert_or_replace_apartment_parse(self, command, command_type):
        """
        Protected parse method for insert and replace
        Input:
            command - the user command
            command_type - the command_type, can be insert or replace
        Return:
            True on valid and False otherwise
        Raises:
            Exception on invalid command_type
        """
        self.set_command(command)

        # get the correct pattern
        if command_type is "insert":
            pattern = self._regex_search(self._PATTERN_INSERT)
            if not pattern:
                UI.set_message(UI.get_error("insert <amount>, <expense_type> at <apartment_id>"))
                return False
        elif command_type is "replace":
            pattern = self._regex_search(self._PATTERN_REPLACE)
            if not pattern:
                UI.set_message(UI.get_error("replace <amount>, <expense_type> at <apartment_id>"))
                return False
        else:
            raise Exception("command type is incorrect")

        # get the data
        amount = pattern.group(1)
        expense_type = pattern.group(2)
        apartment_id = pattern.group(3)

        # wrong type
        if not Apartment.is_expense_type(expense_type):
            UI.set_message(UI.get_error_types())
            return False

        if command_type is "insert":
            # apartment does  exist in the list
            if self.is_apartment(apartment_id):
                UI.set_message("Apartment " + apartment_id + " does exist in the list. "
                                                             "Use this command to replace: replace " + amount + ", " +
                               expense_type + " at " + apartment_id)
                return False
        elif command_type is "replace":
            # apartment does not exist in the list
            if not self.is_apartment(apartment_id):
                UI.set_message("Apartment " + apartment_id + " does not exist in the list. "
                                                             "Use this command to add: insert " + amount + ", " +
                               expense_type + " at " + apartment_id)
                return False

        # all good
        self._parsed_command = {"type": expense_type, "amount": amount, "id": apartment_id}
        return True

    def remove_apartment(self):
        """
            Remove the expenses from an apartment or from a list of apartments.
        """

        if "id" in self._parsed_command:  # remove only from one apartments
            apartment_id = self._parsed_command["id"]
            self._bloc_dict[apartment_id] = Apartment()
            UI.set_message("Removed all expenses from apartment " + apartment_id)

        elif "type" in self._parsed_command:  # remove all types
            expense_type = self._parsed_command["type"]
            for apartment_id in self._bloc_dict.keys():
                self._bloc_dict[apartment_id].expenses[expense_type] = 0

            UI.set_message("Removed " + self._parsed_command["type"] + " from all apartments ")

        elif ("id_min" in self._parsed_command) and ("id_max" in self._parsed_command):  # remove from range
            removed_from = []  # keep track of all removed
            for apartment_id in range(int(self._parsed_command["id_min"]), int(self._parsed_command["id_max"]) + 1):
                apartment_id = str(apartment_id)
                if self.is_apartment(apartment_id):
                    removed_from.append(apartment_id)
                    self._bloc_dict[apartment_id] = Apartment()

            UI.set_message("Removed all expenses from apartments " + removed_from.__str__())
        else:
            raise Exception("Incorrect parsed_command")

    def remove_apartment_parse(self, command):
        """
        Parses and validates the remove command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        self.set_command(command)
        # first case by apartment id
        pattern_remove_by_id = self._regex_search(self._PATTERN_REMOVE_BY_ID)
        if pattern_remove_by_id:
            apartment_id = pattern_remove_by_id.group(1)
            if not self.is_apartment(apartment_id):
                UI.set_message(
                    "Apartment " + apartment_id + " does not exist in this list so all the expenses are by default 0")
                return False

            self._parsed_command = {"id": apartment_id}
            return True

        # second case remove from interval
        pattern_remove_by_interval = self._regex_search(self._PATTERN_REMOVE_BY_INTERVAL)
        if pattern_remove_by_interval:
            id_min = pattern_remove_by_interval.group(1)
            id_max = pattern_remove_by_interval.group(2)

            self._parsed_command = {"id_min": id_min, "id_max": id_max}
            return True

        # third case by type
        pattern_remove_by_type = self._regex_search(self._PATTERN_REMOVE_BY_TYPE)
        if pattern_remove_by_type:
            expense_type = pattern_remove_by_type.group(1)
            if not Apartment.is_expense_type(expense_type):
                UI.set_message(UI.get_error_types())
                return False

            self._parsed_command = {"type": expense_type}
            return True

        UI.set_message(UI.get_error("remove <apartment_id> \n\tremove from 5 to 10 \n\tremove type"))
        return False

    def list_all(self):
        """
        Displays all the apartments in the bloc
        """
        UI.set_message(UI.get_bloc_table(self._bloc_dict))

    def list_by_type(self):
        """
        Displays only the apartments having a specific expense type
        """
        self._validate_parsed_command(["expense_type"])

        expense_type = self._parsed_command["expense_type"]
        filtered_bloc_dict = {}
        for apartment_id in self._bloc_dict.keys():
            if self._bloc_dict[apartment_id].expenses[expense_type] != 0:
                filtered_bloc_dict[apartment_id] = self._bloc_dict[apartment_id]

        if filtered_bloc_dict:
            UI.set_message(UI.get_bloc_table(filtered_bloc_dict))
        else:
            UI.set_message("There are no apartments with " + expense_type)

    def list_by_type_parse(self, command):
        """
        Parses and validates the list command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        self.set_command(command)

        # list <type>
        pattern_type = self._regex_search(self._PATTERN_LIST_BY_TYPE)
        if pattern_type:
            expense_type = pattern_type.group(1)
            if not Apartment.is_expense_type(expense_type):
                UI.set_message(UI.get_error_types())
                return False

            self._parsed_command = {"expense_type": expense_type}
            return True

        UI.set_message(UI.get_error("list <expense_type>"))
        return False

    def list_greater_than(self):
        """
        Displays only the apartments with an overall expense greater than the given amount
        """
        self._validate_parsed_command(["greater"])

        greater_than = float(self._parsed_command["greater"])
        filtered_bloc_dict = {}
        for apartment_id in self._bloc_dict.keys():
            if self._bloc_dict[apartment_id].get_total_expenses() > greater_than:
                filtered_bloc_dict[apartment_id] = self._bloc_dict[apartment_id]

        # check if empty
        if filtered_bloc_dict:
            UI.set_message(UI.get_bloc_table(filtered_bloc_dict))
        else:
            UI.set_message("There are no apartments with overall expenses greater than " + str(greater_than))

    def list_greater_than_parse(self, command):
        """
        Parses and validates the 'list greater than' command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        # list greater than <amount>
        self.set_command(command)
        pattern_greater = self._regex_search(self._PATTERN_LIST_GREATER)
        if pattern_greater:
            greater = pattern_greater.group(1)

            self._parsed_command = {"greater": greater}
            return True

        UI.set_message(UI.get_error("list greater than <amount>"))
        return False

    def list_less_than(self):
        """
        Displays only the apartments with an overall expense less than the given amount
        """
        self._validate_parsed_command(["less"])

        less_than = float(self._parsed_command["less"])
        upper_id_limit = self._parsed_command["upper_id_limit"]
        filtered_bloc_dict = {}
        for apartment_id in range(1, int(upper_id_limit) + 1):
            apartment_id = str(apartment_id)
            if self.is_apartment(apartment_id):
                if self._bloc_dict[apartment_id].get_total_expenses() < less_than:
                    filtered_bloc_dict[apartment_id] = self._bloc_dict[apartment_id]

        # check if empty
        if filtered_bloc_dict:
            UI.set_message(UI.get_bloc_table(filtered_bloc_dict))
        else:
            UI.set_message("There are no apartments with overall expenses less than " + str(less_than))

    def list_less_than_parse(self, command):
        """
        Parses and validates the 'list less than' command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        # less than <amount> before <id>
        self.set_command(command)
        pattern_less = self._regex_search(self._PATTERN_LIST_LESS)
        if pattern_less:
            less = pattern_less.group(1)
            upper_id_limit = pattern_less.group(2)

            self._parsed_command = {"less": less, "upper_id_limit": upper_id_limit}
            return True

        UI.set_message(UI.get_error("list less than <amount> before <apartment_id>"))
        return False

    def list_total_apartment(self):
        """
        Displays the total expenses for an apartment
        """
        self._validate_parsed_command(["id"])

        apartment_id = self._parsed_command["id"]
        UI.set_message("Total expenses = " + str(self._bloc_dict[apartment_id].get_total_expenses()))

    def list_total_apartment_parse(self, command):
        """
        Parses and validates the 'list total' command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        self.set_command(command)

        # sold|total <apartment_number>
        pattern_total = self._regex_search(self._PATTERN_LIST_TOTAL_APARTMENT)
        if pattern_total:
            apartment_id = pattern_total.group(1)
            if not self.is_apartment(apartment_id):
                UI.set_message("Apartment " + apartment_id + " does not exist in the list")
                return False

            self._parsed_command = {"id": apartment_id}
            return True

        UI.set_message(UI.get_error("list total <apartment_id>"))
        return False

    def stat_total_type(self):
        """
        Displays the total for an expense type
        """
        self._validate_parsed_command(["expense_type"])

        sum_type = self._parsed_command["expense_type"]
        total = sum([self._bloc_dict[i].expenses[sum_type] for i in self._bloc_dict])
        # for apartment_id in self._bloc_dict:
        #    total += self._bloc_dict[apartment_id].expenses[sum_type]

        UI.set_message("Total expenses for " + sum_type + " = " + str(total))

    def stat_total_type_parse(self, command):
        """
        Parses and validates the 'sum type' command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        self.set_command(command)

        # sum <type>
        pattern_sum = self._regex_search(self._PATTERN_STAT_TOTAL_TYPE)
        if pattern_sum:
            expense_type = pattern_sum.group(1)
            if not Apartment.is_expense_type(expense_type):
                UI.set_message(UI.get_error_types())
                return False

            self._parsed_command = {"expense_type": expense_type}
            return True

        UI.set_message(UI.get_error("sum <type>"))
        return False

    def stat_max_apartment(self):
        """
        Displays the biggest expense in an apartment
        """
        self._validate_parsed_command(["id"])

        apartment_id = self._parsed_command["id"]
        biggest_types = self._bloc_dict[apartment_id].get_max_expenses_type()

        if biggest_types:
            UI.set_message("Biggest expense is " + biggest_types.__str__() + " = " + str(
                self._bloc_dict[apartment_id].expenses[biggest_types[0]]))
        else:
            UI.set_message("Apartment has all expenses = 0")

    def stat_max_apartment_parse(self, command):
        """
        Parses and validates the 'max id' command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        self.set_command(command)

        # max <id>
        pattern_max = self._regex_search(self._PATTERN_STAT_MAX_APARTMENT)
        if pattern_max:
            apartment_id = pattern_max.group(1)
            if not self.is_apartment(apartment_id):
                UI.set_message("Apartment " + apartment_id + " does not exist in the list")
                return False

            self._parsed_command = {"id": apartment_id}
            return True

        UI.set_message(UI.get_error("max <apartment_number>"))
        return False

    def sort(self):
        """
        Displays the list of apartments sorted
        """
        self._validate_parsed_command(["type", "reverse"])

        reverse_sort = self._parsed_command["reverse"]
        if reverse_sort is True:
            header_message = " sorted descending"
        else:
            header_message = " sorted ascending"

        # simple sort by total expense
        if self._parsed_command["type"] is None:
            # we create a list of tuples consisting of (id, apartment_obj, total_expense)
            list_of_tuples = [
                (apartment_id, self._bloc_dict[apartment_id], self._bloc_dict[apartment_id].get_total_expenses()) for
                apartment_id in self._bloc_dict]

            # set message
            header_message += " by total expenses"

        else:  # sort by type
            expense_type = self._parsed_command["type"]
            # we create a list of tuples consisting of (id, apartment_obj, expense_type_amount)
            list_of_tuples = [
                (apartment_id, self._bloc_dict[apartment_id], self._bloc_dict[apartment_id].expenses[expense_type]) for
                apartment_id in self._bloc_dict]

            # set message
            header_message += " by expense '" + expense_type + "'"

        # print "raw_list: ", list_of_tuples
        # use the last key of the tuple for the value
        sorted_list_of_tuples = sorted(list_of_tuples, key=lambda item: item[2], reverse=reverse_sort)
        # print "sorted_list: ", sorted_sort_it

        UI.set_message(UI.get_bloc_table(sorted_list_of_tuples, header_message))

    def sort_parse(self, command, reverse=False):
        """
        Parses and validates the sort command
        Input:
            command - the user command
            reverse - the order of the sort
        Return:
            True on valid and False otherwise
        """
        self.set_command(command)

        # simple sort no type use total expense
        pattern_sort = self._regex_search(self._PATTERN_SORT)
        if pattern_sort:
            self._parsed_command = {"type": None, "reverse": reverse}
            return True

        # use an expense type for the sort
        pattern_sort_by_type = self._regex_search(self._PATTERN_SORT_BY_TYPE)
        if pattern_sort_by_type:
            expense_type = pattern_sort_by_type.group(1)
            if not Apartment.is_expense_type(expense_type):
                UI.set_message(UI.get_error_types())
                return False

            self._parsed_command = {"type": expense_type, "reverse": reverse}
            return True

        UI.set_message(UI.get_error_sort())
        return False

    def filter(self):
        """
        Filter the list of apartments on specific criteria
        """
        self._validate_parsed_command(["greater", "type"])

        # filter by type
        if self._parsed_command["type"]:
            expense_type = self._parsed_command["type"]
            filtered_bloc_dict = {}
            for apartment_id in self._bloc_dict.keys():
                if self._bloc_dict[apartment_id].expenses[expense_type]:
                    filtered_bloc_dict[apartment_id] = self._bloc_dict[apartment_id]

            self._bloc_dict = filtered_bloc_dict

        # filter by total greater than
        if self._parsed_command["greater"]:
            amount_greater = float(self._parsed_command["greater"])
            filtered_bloc_dict = {}
            for apartment_id in self._bloc_dict.keys():
                if self._bloc_dict[apartment_id].get_total_expenses() > amount_greater:
                    filtered_bloc_dict[apartment_id] = self._bloc_dict[apartment_id]

            self._bloc_dict = filtered_bloc_dict

        # print filtered_bloc_dict
        if not filtered_bloc_dict:
            UI.set_message("No apartment fits the criteria")
        else:
            UI.set_message("Apartments filtered successfully")

    def filter_parse(self, command):
        """
        Parses and validates the filter command
        Input:
            command - the user command
        Return:
            True on valid and False otherwise
        """
        self.set_command(command)

        # filter by total amount greater than
        pattern_total_greater = self._regex_search(self._PATTERN_FILTER_GREATER_THAN_TOTAL)
        if pattern_total_greater:
            greater = pattern_total_greater.group(1)
            self._parsed_command = {"greater": greater, "type": None}
            return True

        # filter by only apartments having that type
        pattern_by_type = self._regex_search(self._PATTERN_FILTER_BY_TYPE)
        if pattern_by_type:
            expense_type = pattern_by_type.group(1)
            if not Apartment.is_expense_type(expense_type):
                UI.set_message(UI.get_error_types())
                return False

            self._parsed_command = {"greater": None, "type": expense_type}
            return True

        UI.set_message(UI.get_error_filter())
        return False
