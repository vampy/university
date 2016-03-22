#!/usr/bin/python
"""
Created on Oct 18, 2013

@author: daniel
"""
# http://python-future.org/compatible_idioms.html
from __future__ import print_function
from builtins import input

try:
    import readline
except ImportError as e:
    print(("\n" * 3) + "--- READLINE IS DISABLED TERMINAL APPLICATION WILL NOT WORK PROPERLY ---")
    print("\tTHE EXCEPTION: " + e.message + ("\n" * 3))


class UI:
    # session message
    session_message = []

    def __init__(self):
        pass

    @staticmethod
    def get_help_menu():
        """
        Returns the help menu(problem 5)
        """
        return """Help Menu:
    1. Add a new transaction into the list:
        insert <amount>, <expense_type> at <apartment_id> -- inserts at apartment <apartment_id> an expense of <amount> RON having the given <expense_type>

    2. Modify expenses from the list:
        remove <apartment_id> -- removes all the expenses at apartment <apartment_id>
        remove from <low_id> to <high_id> -- removes all the expenses from apartment 5 to apartment 10
        remove <expense_type> -- removes all the expenses having the indicated <expense_type>, from all the apartments
        replace <amount>, <expense_type> at <apartment_id> -- replaces the amount for the expense having the specified <expense_type> at apartment <apartment_id> with <amount> RON

    3. Write the expenses having different properties.
        list - displays all proprieties
        list greater than <amount> -- writes all the apartments with an overall expense greater than <amount> RON
        list less than <amount> before <upper_id> -- writes all the apartments with an overall expense less than <amount> for apartments from 1 to <upper_id>
        list type -- writes all the expenses having the specified type
        list total|sold <apartment_id> -- computes the sold (total amount) for apartment <apartment_id>

    4. Obtain different characteristics of expenses
        sum <expense_type> -- writes the total amount for the expenses having the specified type
        max <apartment_id> -- writes the maximum expense per type for apartment 25
        sort asc -- sorts the total expenses/apartment in an ascending order
        sort asc type -- sorts the total expenses per type in a ascending order
        sort desc -- sorts the total expenses/apartment in an descending order
        sort desc type -- sorts the total expenses per type in a descending order

    5. Filter
        filter <expense_type> -- retains only the expenses having the specified <expense_type>
        filter <amount> -- retains only the overall expenses greater than <amount> RON

    6. Undo the last operation
        undo -- the last operation that has modified the list of apartments is cancelled.

    Other:
        To exit: type "exit" or "quit" or by the keyboard CTRL + C
        To print this help menu: type "help"
        """

    @staticmethod
    def get_bloc_table(bloc_dict_or_list, header="", footer=""):
        """
        Builds a nice formatted table from the bloc_dict_or_list provided
        Input:
            bloc_dict - dictionary or a list of tuples of the bloc
        Return:
            string containing the table
        """

        # TODO better check for errors
        if isinstance(bloc_dict_or_list, dict):
            list_of_tuples = list(bloc_dict_or_list.items())
        else:
            list_of_tuples = bloc_dict_or_list

        return_str = "Apartments" + header + ":\n\tID\tExpenses \n\n"
        for tu in list_of_tuples:
            apartment_id = tu[0]
            apartment_obj = tu[1]
            return_str += "\t" + apartment_id + "\t"

            for expense_key in sorted(apartment_obj.expenses.keys()):
                return_str += expense_key + ": " + str(apartment_obj.expenses[expense_key]) + ", "

            return_str += "\t\n"
        return_str += footer

        # return_str += "OLD: Apartments: \n\tID\tExpenses\n\n"
        # for apartment_id in bloc_dict_or_list.iterkeys():
        #
        #    return_str += "\t" + apartment_id + "\t"
        #
        #    for expense_key in sorted(bloc_dict_or_list[apartment_id].expenses.keys()):
        #        return_str += expense_key + ": " + str(bloc_dict_or_list[apartment_id].expenses[expense_key]) + ", "
        #
        #    return_str += "\t\n"

        return return_str

    @staticmethod
    def get_error(correct_syntax):
        """
        Build a generic error message for syntax
        """
        return "Incorrect syntax, it should be like: \n\t" + correct_syntax

    @staticmethod
    def get_error_sort():
        return UI.get_error("sort asc|desc <expense_type> (where <expense_type> is optional)")

    @staticmethod
    def get_error_filter():
        return UI.get_error("filter <expense_type>\n\tfilter <amount>")

    @staticmethod
    def get_error_types():
        """
        Gets the generic error for wrong expense type
        """
        from apartment import EXPENSE_TYPES
        return "Expense type is not correct it should be a value from this list: " + EXPENSE_TYPES.__str__()

    @staticmethod
    def get_command():
        """
        Gets the command inputed by the user
        """
        try:
            return input(">>> ").strip().lower()
        except KeyboardInterrupt:  # handle CTRL + C interrupt
            return "exit"
        except EOFError:  # handle CTRL + D
            return "exit"

    @staticmethod
    def get_message():
        """
        Get the list of all session messages
        Return:
            string of messages
        """
        return_str = ""
        for message in UI.session_message:
            return_str += message + "\n"

        # clean session
        UI.session_message = []

        return return_str

    @staticmethod
    def set_message(message):
        """
        Sets a session specific message into a list
        """
        UI.session_message.append(message)
