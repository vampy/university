#!/usr/bin/python
from datetime import datetime, date, timedelta

class Student:
    def __init__(self, sid, name, address, birthday):
        self.id = sid
        self.name = name
        self.address = address
        self.birthday = birthday
        self.datetime_birthday = datetime.strptime(birthday, "%d-%m-%Y")
        self.grades = []

    def get_age(self):
        return (date.today() - self.datetime_birthday.date()) // timedelta(days=365.2425)

    def get_average(self):
        return sum([i.grade for i in self.grades]) / len(self.grades)

    @staticmethod
    def str_grades(grades):
        string = ""
        len_grades = len(grades)
        for i, grade in enumerate(grades):
            string += str(grade)

            if i != len_grades - 1:
                string += ", "

        return string

    def __str__(self, *args, **kwargs):
        return_str = "\n\t".join(
            ["Student = {",
             "id = %d" % self.id,
             "name = %s" % self.name,
             "address = %s" % self.address,
             "birthday = %s" % self.birthday
             ])

        return return_str + "\n\tgrades = " + self.str_grades(self.grades) + "\n}\n"
