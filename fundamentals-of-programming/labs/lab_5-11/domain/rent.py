#!/usr/bin/python


# TODO USE IDS INSTEAD of instance
class Rent(object):
    def __init__(self, client_id, movie_id, start_date, end_date):
        self.__client_id = client_id
        self.__movie_id = movie_id
        self.__start_date = start_date
        self.__end_date = end_date
        self.__id = self.get_id()
        self.returned = None

    def __str__(self):
        return "%s\t\t\t" * 5 % (
            str(self.client_id), str(self.movie_id), str(self.start_date), str(self.end_date), str(self.returned))

    def get_client_id(self):
        return self.__client_id

    def get_movie_id(self):
        return self.__movie_id

    def get_start_date(self):
        return self.__start_date

    def get_end_date(self):
        return self.__end_date

    def set_client_id(self, value):
        self.__client_id = value

    def set_movie_id(self, value):
        self.__movie_id = value

    def set_start_date(self, value):
        self.__start_date = value

    def set_end_date(self, value):
        self.__end_date = value

    def del_client_id(self):
        del self.__client_id

    def del_movie_id(self):
        del self.__movie_id

    def del_start_date(self):
        del self.__start_date

    def del_end_date(self):
        del self.__end_date

    def get_id(self):
        return str(self.client_id) + "_" + str(self.movie_id) + "_" + str(self.start_date) + "_" + str(
            self.end_date) + "|"

    id = property(fget=get_id)
    client_id = property(get_client_id, set_client_id, del_client_id, "client_id's docstring")
    movie_id = property(get_movie_id, set_movie_id, del_movie_id, "movie_id's docstring")
    start_date = property(get_start_date, set_start_date, del_start_date, "start_date's docstring")
    end_date = property(get_end_date, set_end_date, del_end_date, "end_date's docstring")
