'''
Created on Dec 2, 2013

@author: daniel
'''

from domain.product import Product
from repository.pharmacy import PharmacyRepo, RepositoryException


def test_product():
    product = Product(1, "Daniel", 100)
    assert product.getID() == 1
    assert product.getName() == "Daniel"
    assert product.getPrice() == 100

    product.setId(2)
    product.setName("Alex")
    product.setPrice(10)
    assert product.getID() == 2
    assert product.getName() == "Alex"
    assert product.getPrice() == 10


def test_repository():
    repository = PharmacyRepo("tests.dat")
    product = Product(1, "Daniel", 10)

    try:
        repository.insert(product)
        assert True
    except RepositoryException:
        assert False

    try:
        repository.insert(product)
        assert False
    except RepositoryException:
        assert True

    try:
        repository.insert("test")
        assert False
    except RepositoryException:
        assert True

    try:
        repository.insert(132)
        assert False
    except RepositoryException:
        assert True


if __name__ == "__main__":
    test_product()
    test_repository()
    print
    "All test passed"
