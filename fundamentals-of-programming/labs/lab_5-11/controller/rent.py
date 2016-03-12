#!/usr/bin/python
from datetime import datetime

from controller import Controller
from domain.rent import Rent
from domain.session import Session
from repository.repository import RepositoryException


class RentController(Controller):
    def __init__(self, rent_repository, client_controller, movie_controller):
        Controller.__init__(self, rent_repository)
        self._client_controller = client_controller
        self._movie_controller = movie_controller

    def rent_movie(self, client_id, movie_id, end_date, start_date=datetime.now()):
        """
        Rent a movie
        Input:
            ...
        Output:
            True - success
            False - failure
        Raises:
        """
        # TODO check that dates are in correct order
        if not self._client_controller.get_by_id(client_id):
            Session.set_message("Client does not exist")
            return False

        if not self._movie_controller.get_by_id(movie_id):
            Session.set_message("Movie does not exist")
            return False

        if not isinstance(end_date, datetime):
            Session.set_message("Start date is not correct")
            return False

        if not isinstance(start_date, datetime):
            Session.set_message("End date is not correct")
            return False

        # check if movie is already rented by the same client
        if self.search_not_returned(client_id, movie_id):
            Session.set_message("That movie is already rented by that same client. Please return it first")
            return False

        rent = Rent(client_id=client_id, movie_id=movie_id, start_date=start_date, end_date=end_date)
        try:
            self._repository.insert(rent)
        except RepositoryException as e:
            Session.set_message(e.message)
            return False

        return True

    def return_movie(self, client_id, movie_id):
        """
        Return a movie
        Time complexity:
            O(1)
        Input:
            ...
        Output:
            True - success
            False - failure
        Raises:
        """
        client = self._client_controller.get_by_id(client_id)
        if not client:
            Session.set_message("Client does not exist")
            return False

        movie = self._movie_controller.get_by_id(movie_id)
        if not movie:
            Session.set_message("Movie does not exist")
            return False

        rent = self.search_not_returned(client_id, movie_id)
        if not rent:
            Session.set_message("Rent does not exist for returning")
            return False

        rent.returned = datetime.now()
        return True

    def get_clients_with_most_rents(self):
        """
        Statistics most active clients
        Time complexity:
            O(n)
        Input:
            instance - an object
        Output:
            a list of tuples
        Raises:
        """
        temp_dict = {}
        for rent in self.get_all():
            if rent.client_id in temp_dict:
                temp_dict[rent.client_id] += 1
            else:
                temp_dict[rent.client_id] = 1

        # item[1] will contain the number of rents
        li_sorted = sorted(list(temp_dict.items()), key=lambda tu: tu[1], reverse=True)
        return [(self._client_controller.get_by_id(i[0]), i[1]) for i in li_sorted]
        # print sorted(list(temp_dict), key=lambda item: item[1])

    def get_most_rented_movies(self):
        """
        Statistics most rented movies
        Time complexity:
            O(n)
        Input:
            instance - an object
        Output:
            a list of tuples
        Raises:
        """
        temp_dict = {}
        # find out how frequent each movie was
        for rent in self.get_all():
            if rent.movie_id in temp_dict:
                temp_dict[rent.movie_id] += 1
            else:
                temp_dict[rent.movie_id] = 1

        li_sorted = sorted(list(temp_dict.items()), key=lambda tu: tu[1], reverse=True)
        return [(self._movie_controller.get_by_id(i[0]), i[1]) for i in li_sorted]

    def search(self, client_id="", movie_id=""):
        """
        Search the rent repository
        Time complexity:
            Best: O(1)
            Worst and average: O(n)
        Input:
        Output:
            the rent - on success
            None - on failure
        Raises:
        """
        for rent in self.get_all():

            if client_id == rent.client_id and movie_id == rent.movie_id:
                return rent

        return None

    def search_not_returned(self, client_id="", movie_id=""):
        """
        Search the rent repository
        Input:
        Output:
            the rent - on success
            None - on failure
        Raises:
        """
        for rent in self.get_all():
            # this is a previous rent
            if rent.returned:
                continue

            if client_id == rent.client_id and movie_id == rent.movie_id:
                return rent

        return None

    def search_returned(self, client_id="", movie_id=""):
        """
        Search the rent repository
        Input:
        Output:
            the rent - on success
            None - on failure
        Raises:
        """
        for rent in self.get_all():
            # this is a previous rent
            if rent.returned and client_id == rent.client_id and movie_id == rent.movie_id:
                return rent

        return None
