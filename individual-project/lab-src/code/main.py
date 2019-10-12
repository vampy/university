#!/usr/bin/python

from repository import Repository
from controller import Controller

if __name__ == "__main__":
    print("Running program")

    repository = Repository("students.dat", "students_grades.dat")
    controller = Controller(repository)
    controller.load_from_files()

    # for student in repository.students.values():
    #     print(student.name, "%.2f" % student.get_average())
    #
    # print(repository)

    controller.group_by_average()
    controller.filter_non_passing()
    controller.group_by_best_subject()
    controller.group_by_age()
    controller.filter_passed_all()
    print("All files were generated successfully")
