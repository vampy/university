#!/usr/bin/python
"""
@author: Daniel Butum, Group 911
"""


class Session(object):
    # session message
    _message = []

    @staticmethod
    def get_message():
        """
        Get the list of all session messages
        Return:
            string of messages
        """
        return_str = ""
        for message in Session._message:
            return_str += message + "\n"

        # clean session for next iteration
        Session._message = []

        return return_str

    @staticmethod
    def set_message(message):
        """
        Sets a session specific message into a list
        """
        Session._message.append(message)