#!/usr/bin/python
import cPickle
from repository import Repository, RepositoryException


class FileRepositoryException(RepositoryException):
    pass


class FileRepository(Repository):
    def __init__(self, filename=None):
        if not filename:
            raise FileRepositoryException("Please set a filename")

        self._filename = filename
        Repository.__init__(self)
        self._load()

    def insert(self, instance):
        """
        Insert the object into the file
        """
        Repository.insert(self, instance)
        self._save()

    def update(self, instance):
        """
        Update the object from the file
        """
        Repository.update(self, instance)
        self._save()

    def delete_by_id(self, instance_id):
        """
        Delete an object by id
        """
        Repository.delete_by_id(self, instance_id)
        self._save()

    def _load(self):
        """
        Load the previous state from a file
        """
        try:
            with open(self._filename, "r") as fp:
                self._repository = cPickle.load(fp)
                # print self._repository
        except cPickle.UnpickleableError:
            print("Unpickle Error on filename '%s'. continue" % self._filename)
        except IOError:
            print("IO Error on filename '%s'. continue" % self._filename)
        except Exception as e:
            print("Unhandled exception on filename '%s'. message=%s" % (self._filename, e.message))

    def _save(self):
        """
        Saves the current state to a file
        """
        with open(self._filename, "w") as fp:
            cPickle.dump(self._repository, fp)
