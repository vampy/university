#!/usr/bin/python

try:
    import readline
except ImportError as e:
    print(("\n" * 3) + "--- READLINE IS DISABLED TERMINAL APPLICATION WILL NOT WORK PROPERLY ---")
    print("\tTHE EXCEPTION: " + e.message + ("\n" * 3))
from datetime import datetime
from utils.number import convert_to_int
from domain.session import Session


class Console(object):
    # first run
    _first_run = True

    OPTION_CLIENTS_ADD = "1"
    OPTION_CLIENTS_REMOVE = "2"
    OPTION_CLIENTS_UPDATE = "3"
    OPTION_CLIENTS_LIST = "4"

    OPTION_MOVIES_ADD = "5"
    OPTION_MOVIES_REMOVE = "6"
    OPTION_MOVIES_UPDATE = "7"
    OPTION_MOVIES_LIST = "8"

    OPTION_SEARCH_CLIENTS = "9"
    OPTION_SEARCH_MOVIES = "10"

    OPTION_RENT_MOVIE = "11"
    OPTION_RETURN_MOVIE = "12"
    OPTION_LIST_ALL_RENTS = "13"

    OPTION_STAT_1 = "14"
    OPTION_STAT_2 = "15"

    OPTION_HELP = "h"
    OPTION_QUIT = "q"

    def __init__(self, client_controller, movie_controller, rent_controller):
        self.client_controller = client_controller
        self.movie_controller = movie_controller
        self.rent_controller = rent_controller

    def run(self):
        """
        Run the console gui
        """
        # TODO CHECK IF CLIENT HAS RENTED A MOVIE IF SO DO NOT ALLOW EDITING THE ID
        while True:
            option = self.get_option()

            if option == self.OPTION_QUIT:
                break

            if option == self.OPTION_HELP:
                self.display_help()

            elif option == self.OPTION_CLIENTS_ADD:  # add movie
                print("Add Client")
                client_id = self.get_client_id()
                client_name = self.get_client_name()
                client_cnp = self.get_client_cnp()
                if self.client_controller.store(client_id=client_id,
                                                client_name=client_name,
                                                client_cnp=client_cnp):
                    Session.set_message("Client added")

            elif option == self.OPTION_CLIENTS_UPDATE:  # update client
                print("Update Client")
                client_id = self.get_client_id()
                client_name = self.get_client_name()
                client_cnp = self.get_client_cnp()
                if self.client_controller.update(client_id=client_id,
                                                 client_name=client_name,
                                                 client_cnp=client_cnp):
                    Session.set_message("Client %s updated" % client_id)

            elif option == self.OPTION_CLIENTS_REMOVE:  # remove client
                print("Remove Client")
                client_id = self.get_client_id()
                if self.client_controller.remove(client_id):
                    Session.set_message("Client ID %s removed" % client_id)

            elif option == self.OPTION_CLIENTS_LIST:  # list clients
                print("List Clients")
                print(self.client_controller)

            elif option == self.OPTION_MOVIES_ADD:  # add movie
                print("Add Movie")
                movie_id = self.get_movie_id()
                movie_title = self.get_movie_title()
                movie_type = self.get_movie_type()
                movie_description = self.get_movie_description()
                if self.movie_controller.store(movie_id=movie_id,
                                               movie_title=movie_title,
                                               movie_type=movie_type,
                                               movie_description=movie_description):
                    Session.set_message("Movie added")

            elif option == self.OPTION_MOVIES_UPDATE:  # update movie
                print("Update Movie")
                movie_id = self.get_movie_id()
                movie_title = self.get_movie_title()
                movie_type = self.get_movie_type()
                movie_description = self.get_movie_description()
                if self.movie_controller.update(movie_id=movie_id,
                                                movie_title=movie_title,
                                                movie_type=movie_type,
                                                movie_description=movie_description):
                    Session.set_message("Movie %s updated" % movie_id)

            elif option == self.OPTION_MOVIES_REMOVE:  # remove movie
                print("Remove movie")
                movie_id = self.get_movie_id()
                if self.movie_controller.remove(movie_id):
                    Session.set_message("Movie ID %s removed" % movie_id)

            elif option == self.OPTION_MOVIES_LIST:  # list movies
                print("List Movies")
                print(self.movie_controller)

            elif option == self.OPTION_SEARCH_CLIENTS:  # search clients
                print("Please input by what criteria the search will be or blank to not search by it")
                client_id = self.get_client_id(not_empty=False)
                client_name = self.get_client_name()
                client_cnp = self.get_client_cnp()
                found_clients = self.client_controller.search(client_id=client_id,
                                                              client_name=client_name,
                                                              client_cnp=client_cnp)
                if found_clients:
                    print("Found clients")
                    for client in found_clients:
                        print(client)
                else:
                    print("No clients match the search criteria")

            elif option == self.OPTION_SEARCH_MOVIES:  # search movies
                print("Please input by what criteria the search will be or blank to not search by it")
                movie_id = self.get_movie_id(not_empty=False)
                movie_title = self.get_movie_title()
                movie_type = self.get_movie_type()
                movie_description = self.get_movie_description()
                found_movies = self.movie_controller.search(movie_id=movie_id,
                                                            movie_title=movie_title,
                                                            movie_type=movie_type,
                                                            movie_description=movie_description)
                if found_movies:
                    print("Found movies")
                    for movie in found_movies:
                        print(movie)
                else:
                    print("No clients match the search criteria")

            elif option == self.OPTION_RENT_MOVIE:
                print("Rent Movie")
                client_id = self.get_client_id()
                movie_id = self.get_movie_id()
                start_date = self.get_rent_start_date()
                end_date = self.get_rent_end_date()
                if self.rent_controller.rent_movie(client_id=client_id,
                                                   movie_id=movie_id,
                                                   start_date=start_date,
                                                   end_date=end_date):
                    Session.set_message("Movie rented")

            elif option == self.OPTION_RETURN_MOVIE:
                print("Return Movie")
                client_id = self.get_client_id()
                movie_id = self.get_movie_id()
                if self.rent_controller.return_movie(client_id=client_id, movie_id=movie_id):
                    Session.set_message("Movie returned")

            elif option == self.OPTION_LIST_ALL_RENTS:
                print("List rents")
                print(self.rent_controller)

            elif option == self.OPTION_STAT_1:
                print("Most rented movies")
                self.rent_controller.get_most_rented_movies()
                # item[0] the movie object and item[1] the number of apereances
                for item in self.rent_controller.get_most_rented_movies():
                    print "Rented ", item[1], " times -->(ID, Title, Type, Description):", item[0]

            elif option == self.OPTION_STAT_2:
                print("Clients with rented movies ordered by number of rents")
                # item[0] the client object and item[1] the number of rents
                for item in self.rent_controller.get_clients_with_most_rents():
                    print "Number of rents : ", item[1], " by client -->(ID, Name, CNP):", item[0]

            self.display_message()

    @staticmethod
    def _get_options_ordered():
        """
        Get all the options of the console
        Time complexity:
            O(1)
        Input:
        Output:
            a list of options
        Raises:
        """
        return [Console.OPTION_CLIENTS_ADD, Console.OPTION_CLIENTS_REMOVE,
                Console.OPTION_CLIENTS_UPDATE, Console.OPTION_CLIENTS_LIST,

                Console.OPTION_MOVIES_ADD, Console.OPTION_MOVIES_REMOVE,
                Console.OPTION_MOVIES_UPDATE, Console.OPTION_MOVIES_LIST,

                Console.OPTION_SEARCH_CLIENTS, Console.OPTION_SEARCH_MOVIES,

                Console.OPTION_RENT_MOVIE, Console.OPTION_RETURN_MOVIE, Console.OPTION_LIST_ALL_RENTS,

                Console.OPTION_STAT_1, Console.OPTION_STAT_2,

                Console.OPTION_HELP, Console.OPTION_QUIT]

    @staticmethod
    def _get_help_menu():
        """
        Returns the menu
        Time complexity:
            O(1)
        """
        return """Menu:
    %s. Add client
    %s. Remove client
    %s. Update client
    %s. List clients
    
    %s. Add movie
    %s. Remove movie
    %s. Update movie
    %s. List movies

    %s. Search clients
    %s. Search movies

    %s. Rent Movie
    %s. Return Movie
    %s. List all rents

    %s. Most rented movies
    %s. Clients with rented movies ordered by number of rents

    %s. Display this help menu
    %s. Quit
""" % tuple(Console._get_options_ordered())


    @staticmethod
    def _raw_input(message):
        """
        Improved raw_input
        Time complexity:
            O(1)
        Return:
            string - user command
        """
        try:
            return raw_input(message).strip()
        except KeyboardInterrupt:  # handle CTRL + C interrupt
            return Console.OPTION_QUIT
        except EOFError:  # handle CTRL + D
            return Console.OPTION_QUIT

    @staticmethod
    def _get_command(message=">>> "):
        """
        Gets the command inputed by the user
        Time complexity:
            O(1)
        """

        command = Console._raw_input(message).lower()
        return command

    @staticmethod
    def _get_int_command(message, not_empty=True):
        """
        Gets the command inputed by the user if is an int
        Time complexity:
            O(1)
        """
        command = Console._raw_input(message)
        while convert_to_int(command) is None:
            # if empty command and option is set => return
            if not not_empty and command == "":
                return command

            print("Please retry again with an integer")
            command = Console._raw_input(message)
        # ID should be an int
        return int(command)

    @staticmethod
    def _get_datetime_command(message="Enter the date (d/m/y)"):
        """
        Gets the command inputed by the user iff is a datetime object
        Time complexity:
            O(1)
        Output:
            datetime object
        """
        while True:
            command = Console._raw_input(message).lower()
            try:
                start_date = datetime.strptime(command, "%d/%m/%Y")
                return start_date
            except ValueError as e:
                print("Date formath should be day/month/year as 23/05/1985")

    @staticmethod
    def get_option():
        """
        Get the option/event that happened
        Time complexity:
            O(1)
        Return:
            int - representing the event
        """
        # display first help
        if Console._first_run:
            Console.display_help()
            Console._first_run = False

        command_main = Console._get_command()
        while command_main not in Console._get_options_ordered():
            print("Please choose an option from the list. Command " + str(command_main) + " is not valid")
            command_main = Console._get_command()

        return command_main

    @staticmethod
    def get_client_id(message="Client id: ", not_empty=True):
        """
        Get the client id
        Time complexity:
            O(1)
        """
        return Console._get_int_command(message, not_empty=not_empty)

    @staticmethod
    def get_client_name(message="Client Name: "):
        """
        Get the client name
        Time complexity:
            O(1)
        """
        return Console._raw_input(message)

    @staticmethod
    def get_client_cnp(message="Client CNP: "):
        """
        Get the client cnp
        Time complexity:
            O(1)
        """
        return Console._raw_input(message)

    @staticmethod
    def get_movie_id(message="Movie id: ", not_empty=True):
        """
        Get the movie id
        Time complexity:
            O(1)
        """
        return Console._get_int_command(message, not_empty=not_empty)

    @staticmethod
    def get_movie_title(message="Movie title: "):
        """
        Get the movie title
        Time complexity:
            O(1)
        """
        return Console._raw_input(message)

    @staticmethod
    def get_movie_type(message="Movie type: "):
        """
        Get the movie type
        Time complexity:
            O(1)
        """
        return Console._raw_input(message)

    @staticmethod
    def get_movie_description(message="Movie description: "):
        """
        Get the movie description
        Time complexity:
            O(1)
        """
        return Console._raw_input(message)

    @staticmethod
    def get_rent_start_date(message="Enter the start date (d/m/y): "):
        """
        Get the rent start date
        Time complexity:
            O(1)
        """
        return Console._get_datetime_command(message)

    @staticmethod
    def get_rent_end_date(message="Enter the end date (d/m/y): "):
        """
        Get the rent end date
        Time complexity:
            O(1)
        """
        return Console._get_datetime_command(message)

    @staticmethod
    def display_help():
        """
        Display the message to the user
        Time complexity:
            O(1)
        """
        print(Console._get_help_menu())

    @staticmethod
    def display_message():
        """
        Display the message to the user
        Time complexity:
            O(1)
        """
        print(Session.get_message())