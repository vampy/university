#!/usr/bin/python
from domain.rent import Rent
from repository import Repository, RepositoryException
from filerepository import FileRepository, FileRepositoryException


class RentRepository(Repository):
    def _validate_instance(self, rent):
        if not isinstance(rent, Rent):
            raise RepositoryException("Not a rent")

    def _get_table_header(self):
        return "Client ID\t\t\tMovie ID\t\t\tStart date\t\t\tEnd date\t\t\tReturned Date"