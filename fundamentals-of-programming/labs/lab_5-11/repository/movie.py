#!/usr/bin/python
from domain.movie import Movie
from repository import Repository, RepositoryException


class MovieRepository(Repository):
    def _validate_instance(self, movie):
        if not isinstance(movie, Movie):
            raise RepositoryException("Not movie")

    def _get_table_header(self):
        return "ID\t\t\tTitle\t\t\tType\t\t\tDescription"
