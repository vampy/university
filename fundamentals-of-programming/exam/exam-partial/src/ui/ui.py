'''
Created on Dec 2, 2013

@author: daniel
'''


class PharmacyUI(object):
    def __init__(self, pController):
        self.__pController = pController
        self.__commands = {
            "0": self.__quit,
            "1": self.__search,
            "2": self.__buy,
            "3": self.__total
        }

    def getMenu(self):
        return """\n\n
        1. Search product
        2. Buy product
        3. Total amount due
            
        0. Quit
        """

    def run(self):
        """
        Run the program
        Input:
        Output:
        
        """
        while True:
            print
            self.getMenu()
            command = raw_input(">>> ").strip()
            while command not in self.__commands:
                print("Invalid commmand")
                command = raw_input(">>> ").strip()

            self.__commands[command]()

    def __buy(self):
        """
        Handle buy command
        Input:
        Output:
        
        """

        # get the name and quantity from the user
        print
        "Buy Product"
        name = raw_input("Product name: ").strip()
        qty = raw_input("Product quantity: ").strip()
        try:
            qty = int(qty)
        except ValueError:
            print
            "Quantity is not a number please try again"
            return

        # find the product instance by name
        products = self.__pController.filterByName(name)
        # if empty do nothing
        if not products:
            print
            "There are no products with that name"
            return

        # if found use the first product with that name
        self.__pController.addProductToCart(products[0], qty)
        print
        "Product added"

    def __total(self):
        """
        Handle total command
        Input:
        Output:
        
        """
        print
        "Total: %d" % self.__pController.getCartTotal()

    def __search(self):
        """
        Handle search command
        Input:
        Output:
        
        """
        name = raw_input("Product name: ").strip()
        products = self.__pController.filterByName(name)

        # check empty product list
        if not products:
            print
            "There are no products with that name"
            return

        # if here all good display found products
        print("Found products: ")
        for product in products:
            print
            product

    def __quit(self):
        print
        "\n\nBye Bye."
        exit()
