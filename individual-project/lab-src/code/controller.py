#!/usr/bin/python

from repository import Repository


class Controller:
    def __init__(self, repository):
        if not isinstance(repository, Repository):
            raise Exception("repository if not of type Repository")

        self.repository = repository

    def load_from_files(self):
        self.repository.load_from_files()

    def group_by_average(self):
        self.repository.group_by_average()

    def filter_non_passing(self):
        self.repository.filter_non_passing()

    def group_by_best_subject(self):
        self.repository.group_by_best_subject()

    def group_by_age(self):
        self.repository.group_by_age()

    def filter_passed_all(self):
        self.repository.filter_passed_all()