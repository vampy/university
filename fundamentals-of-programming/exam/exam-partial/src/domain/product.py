'''
Created on Dec 2, 2013

@author: daniel
'''


class Product(object):
    def __init__(self, product_id, product_name, product_price):
        self.__id = product_id
        self.__name = product_name
        self.__price = product_price

    def getID(self):
        return self.__id

    def getName(self):
        return self.__name

    def getPrice(self):
        return self.__price

    def setId(self, value):
        self.__id = value

    def setName(self, value):
        self.__name = value

    def setPrice(self, value):
        self.__price = value

    def __str__(self):
        return "\tName: %s, Price: %s" % (self.getName(), str(self.getPrice()))
