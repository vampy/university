#!/usr/bin/python
from domain.client import Client
from filerepository import FileRepository
from repository import RepositoryException


class FileClientRepository(FileRepository):
    def _validate_instance(self, client):
        if not isinstance(client, Client):
            raise RepositoryException("Not Client")

    def _get_table_header(self):
        return "ID\t\t\tName\t\t\tCNP"
