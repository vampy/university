#!/usr/bin/python


class Grade:
    def __init__(self, name, grade):
        self.name = name
        self.grade = grade

    def __str__(self, *args, **kwargs):
        return "%s=%d" % (self.name, self.grade)

