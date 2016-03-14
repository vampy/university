#!/usr/bin/python
import utils.sort


class RepositoryException(Exception):
    pass


class Repository(object):
    # template for messages
    template_message_id_exists = "ID '%s' does already exist"
    template_message_id_not_exists = "Id '%s' does not exist"

    def __init__(self):
        self._repository = {}

    def insert(self, instance):
        """
        A new instance object to the repository
        Time complexity:
            O(1)
        Input:
            instance - an object
        Output:
            True on success
        Raises:
            RepositoryException
        """
        self._validate_instance(instance)
        if instance.id in self._repository:
            raise RepositoryException(Repository.template_message_id_exists % instance.id)

        self._repository[instance.id] = instance
        return True

    def update(self, instance):
        """
        Update a instance
        Time complexity:
            O(1)
        Input:
            instance - an object
        Output:
            True on success
        Raises:
            RepositoryException
        """
        self._validate_instance(instance)
        if instance.id not in self._repository:
            raise RepositoryException(Repository.template_message_id_not_exists % instance.id)

        self._repository[instance.id] = instance
        return True

    def delete_by_id(self, instance_id):
        """
        A new instance object to the repository
        Time complexity:
            O(1)
        Input:
            instance - an object
        Output:
            True - on success
        Raises:
            RepositoryException
        """
        if instance_id not in self._repository:
            raise RepositoryException(Repository.template_message_id_not_exists % instance_id)

        del self._repository[instance_id]
        return True

    def select_by_id(self, instance_id):
        """
        Get an instance by the id
        Time complexity:
            Best: O(1)
            Average: O(n)
            Worst: O(n)
        Input:
            instance - an object
        Output:
            the instance or None
        Raises:
        """
        found = utils.sort.search(self.select_all(), instance_id, "get_id")
        if found:
            return found[0]

        # for el in self.select_all():
        #     if el.id == instance_id:
        #         return el

        return None

    def select_all(self):
        """
        Get all instances
        Time complexity:
            O(1)
        Input:
        Output:
            list of all instances
        Raises:
        """
        return self._repository.values()

    # def _get_index_for_id(self, instance_id):
    #    """
    #    Get the index for the instance id
    #    """
    #    count = 0
    #    for el in self._repository:
    #        if el.id == instance_id:
    #            return count
    #        count += 1
    #
    #    return -1

    def _validate_instance(self, instance):
        raise RepositoryException("NOT IMPLEMENTED")

    def _get_table_header(self):
        raise RepositoryException("NOT IMPLEMENTED")

    def __str__(self):
        if not self._repository:
            return "Repository is empty"

        return_str = self._get_table_header() + "\n\n"
        for el in self.select_all():
            return_str += str(el) + "\n"
        return return_str
