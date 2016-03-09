#!/usr/bin/python
from repository.repository import RepositoryException
from domain.session import Session


class Controller(object):
    def __init__(self, repository_obj):
        """
        Init the controller with an repository
        Input:
            repository_obj - an repository object
        Output:
        Raises:
        """
        self._repository = repository_obj

    def get_all(self):
        """
        Get all elements from the repository
        Time complexity:
            O(1)
        Input:
        Output:
            a list of all objects from the repository
        Raises:
        """
        return self._repository.select_all()

    def get_by_id(self, instance_id):
        """
        Get an element from the repository by id
        Time complexity:
            O(n)
        Input:
            instance_id - an object
        Output:
            an object - on success
            None - on nonexistent
        Raises:
        """
        return self._repository.select_by_id(instance_id)

    def remove(self, instance_id):
        """
        Remove an element from the repository by id
        Time complexity:
            O(1)
        Input:
            instance - an object
        Output:
            True - on success
            False - on failure (with session message set)
        Raises:
        """
        try:
            self._repository.delete_by_id(instance_id)
            return True
        except RepositoryException as e:
            Session.set_message(e.message)
            return False

    def __str__(self):
        """
        Return the str of the repository
        """
        return str(self._repository)
