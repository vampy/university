'''
Created on Dec 2, 2013

@author: daniel
'''


class PharmacyController(object):
    def __init__(self, repository):
        self.__pRepo = repository
        # cart is used to keep track of all the products
        self.__cart = []
        self.__cartTotal = 0

    def addProductToCart(self, product, qty):
        """
        Add a product to cart
        Input:
            product - instance of type product
            qty - int - representing the quantity
        Output:
        
        """
        # we will store them as list of tuples [(product_instance, qty)]
        self.__cart.append((product, qty))

        self.__cartTotal += int(product.getPrice()) * int(qty)

    def filterByName(self, name):
        """
        Search a product by name and return the closest matching
        Input:
            name - string - the name of the product
        Output:
            list - a list of products instances
        """
        return_list = []
        name = name.lower()
        for product in self.__pRepo.getAll():
            if name in product.getName().lower():
                return_list.append(product)

        return return_list

    def getProductsInCart(self):
        """
        Get the products
        Input:
        Output:
            a list of tuples in the format [(product, qty)]
        """
        return self.__cart

    def getCartTotal(self):
        """
        Get the car total
        Input:
        Output:
            int - representing the total
        """
        return self.__cartTotal

    def newCart(self):
        """
        Init a new cart
        """
        self.__cart = []
