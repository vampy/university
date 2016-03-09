#!/usr/bin/python


class Movie(object):
    def __init__(self, movie_id, movie_title="", movie_type="", movie_description=""):
        self.__id = movie_id
        self.__title = movie_title
        self.__description = movie_description
        self.__type = movie_type

    def __str__(self):
        return "%s\t\t\t"*4 % (self.__id, self.__title, self.__type,  self.__description[0:20])

    def get_id(self):
        return self.__id

    def get_title(self):
        return self.__title

    def get_description(self):
        return self.__description

    def get_type(self):
        return self.__type

    def set_id(self, value):
        self.__id = value

    def set_title(self, value):
        self.__title = value

    def set_description(self, value):
        self.__description = value

    def set_type(self, value):
        self.__type = value

    def del_id(self):
        del self.__id

    def del_title(self):
        del self.__title

    def del_description(self):
        del self.__description

    def del_type(self):
        del self.__type

    id = property(get_id, set_id, del_id, "id's docstring")
    title = property(get_title, set_title, del_title, "title's docstring")
    description = property(get_description, set_description, del_description, "description's docstring")
    type = property(get_type, set_type, del_type, "type's docstring")