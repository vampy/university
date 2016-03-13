#!/usr/bin/python
"""
Created on Oct 18, 2013

@author: daniel
"""
import copy
import json

from bloc import Bloc
from ui import UI


class App():
    def __init__(self):
        """
        The construction for the app. Used for reading the bloc from the file
        """
        # read apartments from file
        try:
            fp = open("apartments.json", "r")
            temp_dict = json.loads(fp.read())
            fp.close()
        except ValueError:  # handle empty json
            temp_dict = {}

        # load the apartments into the dict
        # for key in temp_dict:
        #    temp_dict[key] = Apartment.from_dictionary(temp_dict[key])

        # create the bloc object
        self.bloc = Bloc.from_dictionary(temp_dict)

        # undo variables
        self._undo_last_operation = None
        self._undo_bloc = None

    def run(self):
        """
        Main loop of the application
        """
        print(UI.get_help_menu())

        while True:
            # get the command from the user
            command = UI.get_command()

            # find out the command type and handle each command correctly
            if "help" in command:
                UI.set_message(UI.get_help_menu())

            elif ("quit" in command) or ("exit" in command):
                self.exit()

            elif "insert" in command:
                if self.bloc.insert_apartment_parse(command):
                    self._undo_start("insert")
                    self.bloc.insert_apartment()

            elif "replace" in command:
                if self.bloc.replace_apartment_parse(command):
                    self._undo_start("replace")
                    self.bloc.insert_apartment()

            elif "remove" in command:
                if self.bloc.remove_apartment_parse(command):
                    self._undo_start("remove")
                    self.bloc.remove_apartment()

            elif "list" in command:
                if "list" == command:
                    self.bloc.list_all()
                elif "greater" in command:
                    if self.bloc.list_greater_than_parse(command):
                        self.bloc.list_greater_than()

                elif "less" in command:
                    if self.bloc.list_less_than_parse(command):
                        self.bloc.list_less_than()

                elif ("sold" in command) or ("total" in command):
                    if self.bloc.list_total_apartment_parse(command):
                        self.bloc.list_total_apartment()

                else:  # check list by type
                    if self.bloc.list_by_type_parse(command):
                        self.bloc.list_by_type()

            elif "sum" in command:
                if self.bloc.stat_total_type_parse(command):
                    self.bloc.stat_total_type()

            elif "max" in command:
                if self.bloc.stat_max_apartment_parse(command):
                    self.bloc.stat_max_apartment()

            elif "filter" in command:
                if self.bloc.filter_parse(command):
                    self._undo_start("filter")
                    self.bloc.filter()

            elif "sort" in command:
                if "asc" in command:
                    if self.bloc.sort_parse(command, reverse=False):
                        self.bloc.sort()

                elif "desc" in command:
                    if self.bloc.sort_parse(command, reverse=True):
                        self.bloc.sort()

                else:
                    UI.set_message(UI.get_error_sort())

            elif "undo" in command:
                if self._undo_last_operation:
                    UI.set_message("Undo operation '" + self._undo_last_operation + "' finished")
                    self._undo_end()
                else:
                    UI.set_message("Nothing to undo")

            elif "save" == command:
                self.save()
                UI.set_message("Current bloc state saved to file.")

            else:
                UI.set_message("Command unknown. type help for a list of commands")

            # print all messages
            print(UI.get_message())
            # print(UI.get_bloc_table(self.bloc.get()))

    def _undo_start(self, operation):
        """
        Start the undo process
        Input:
            operation - the last command
        """
        self._undo_last_operation = operation
        self._undo_bloc = copy.deepcopy(self.bloc)

    def _undo_end(self):
        """
        Start the undo process
        Input:
            operation - the last command
        """
        self._undo_last_operation = None
        self.bloc = self._undo_bloc
        self._undo_bloc = None

    def save(self):
        """
        Saves the current bloc into the apartments.json file
        """
        # bloc_dict = self.bloc.get()
        # for apartment_id in bloc_dict.keys():
        #    bloc_dict[apartment_id] = Apartment.to_dictionary(bloc_dict[apartment_id])
        #
        fp = open("apartments.json", "w")
        fp.write(json.dumps(Bloc.to_dictionary(self.bloc)))
        fp.close()

    def exit(self):
        """
        Quits the app, called on 'quit' command
        """
        print("\n")
        self.save()
        exit()
