#!/usr/bin/python


class Client(object):
    def __init__(self, client_id, client_name="", client_cnp=""):
        self.__id = client_id
        self.__name = client_name
        self.__cnp = client_cnp
    
    def __str__(self):
        # "Name %s" % name
        return "%s\t\t\t"*3 % (self.__id, self.__name, self.__cnp)

    def __cmp__(self, other):
        if self.get_name() < other.get_name():
            return -1
        elif self.get_name() > other.get_name():
            return 1

        return 0

    def get_id(self):
        return self.__id

    def get_name(self):
        return self.__name

    def get_cnp(self):
        return self.__cnp

    def set_id(self, value):
        self.__id = value

    def set_name(self, value):
        self.__name = value

    def set_cnp(self, value):
        self.__cnp = value

    def del_id(self):
        del self.__id

    def del_name(self):
        del self.__name

    def del_cnp(self):
        del self.__cnp

    id = property(get_id, set_id, del_id, "id's docstring")
    name = property(get_name, set_name, del_name, "name's docstring")
    cnp = property(get_cnp, set_cnp, del_cnp, "cnp's docstring")