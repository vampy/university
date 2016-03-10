'''
Created on Dec 2, 2013

@author: daniel
'''
from domain.product import Product

class RepositoryException(Exception):
    pass

class PharmacyRepo():
    def __init__(self, fileName="products.dat"):
        self.__repository = {}
        self.__load(fileName)

    def insert(self, instance):
        """
        Add a product to the repository
        Input:
        Output:
        Raises:
            RepositoryException on id already matched
        
        """
        self.__validate_instance(instance)
        if instance.getID() in self.__repository:
            raise RepositoryException("Id does already exist")
        
        self.__repository[instance.getID()] = instance

    def getAll(self):
        """
        Get all products
        Input:
        Output:
            list of instances
        
        """
        return self.__repository.values()

    def __validate_instance(self, instance):
        """
        Check if instance is correct
        Input:
            instance - object
        Output:
        Raises:
            RepositoryException on invalid instance
        
        """
        if not isinstance(instance, Product):
            raise RepositoryException("Not of type product")


    def __load(self, fileName):
        """
        Load the repository from a file
        Input:
            fileName - string - the name of the file
        Output:
        
        """
        try:
            with open(fileName, "r") as fp:
                for line in fp:
                    # the format is in <id>;<name;<price>
                    temp = line.strip().split(";")
                    
                    # create instance
                    product = Product(product_id=int(temp[0]), product_name=temp[1], product_price=int(temp[2]))
                    
                    # insert it
                    self.insert(product)
                    
        except IOError:
            with open(fileName, "w") as fp:
                pass
        except ValueError:
            print "Repository is bad. ABBORTING"
            exit()