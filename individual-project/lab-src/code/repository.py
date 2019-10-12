#!/usr/bin/python

from student import Student
from grade import Grade


class Repository:
    def __init__(self, filename_students, filename_grades):
        self.filename_students = filename_students
        self.filename_grades = filename_grades
        self.students = {}  # map from int to Student instance

    def load_from_files(self):
        """
        Load all files from the input files
        :return:
        """
        # read from file
        with open(self.filename_students, "r") as f:
            lines_students = [line.strip() for line in f]

        with open(self.filename_grades, "r") as f:
            lines_grades = [line.strip() for line in f]

        # add all students
        for i, line in enumerate(lines_students):
            line_list = line.split("|")

            # validate length
            if len(line_list) != 4:
                print("Line: %d in file %s is invalid. IGNORING" % (i + 1, self.filename_students))
                continue

            sid = int(line_list[0])
            name, address, birthday = line_list[1:]

            # validate id
            if not sid:
                print("Line: %d in file %s is invalid. Id is not an int. IGNORING" % (i + 1, self.filename_students))
                continue
            if sid in self.students:
                print("Line: %d in file %s is invalid. Id already exists IGNORING" % (i + 1, self.filename_students))
                continue

            # add to repository
            self.students[sid] = Student(sid, name, address, birthday)

        # add all grades
        last_id = None
        last_line = None
        for i, line in enumerate(lines_grades):
            #print(line, last_id, last_line)
            # found id
            if last_id is None or last_line is None or not last_line:
                last_id = int(line)

                # validate id
                if not last_id:
                    raise Exception("Line: %d in file %s is invalid. Id is not an int" % (i + 1, self.filename_grades))
                if last_id not in self.students:
                    raise Exception("Line: %d in file %s is invalid. Id does not exist" % (i + 1, self.filename_grades))
            elif not line:  # empty line
                pass
            else:  # found grade info
                name, grade = line.split("=")
                grade = int(grade)

                # validate grade
                if not grade:
                    print("Line: %d in file %s is invalid. grade is not an int. IGNORING" % (i + 1, self.filename_grades))
                    continue

                # add grade
                self.students[last_id].grades.append(Grade(name, grade))

            last_line = line.strip()

    def group_by_average(self, descending=True):
        """
        Group students
        :param descending:
        :return:
        """
        # sort
        students = list(self.students.values())
        students.sort(key=lambda x: x.get_average(), reverse=descending)

        # write to file
        with open("students_desc.txt", "w") as f:
            f.write("Id\t\tName\t\t\t\t\tAverage\n")
            for student in students:
                f.write("%d\t\t%s\t\t\t%.2f\n" % (student.id, student.name, student.get_average()))

    def filter_non_passing(self):
        """
        Filter all non passing students
        :return:
        """
        students = {}  # a map from sid => [list of non passing grades]

        # filter items
        for sid, student in self.students.items():
            for g in student.grades:

                # non passing grade
                if g.grade < 5.0:

                    # add to non passing
                    if sid not in students:
                        students[sid] = [g]
                    else:
                        students[sid].append(g)

        # write to file
        with open("students_nonpassing.txt", "w") as f:
            f.write("Id\t\tName\t\t\t\t\tFailed Subjects\n")
            for sid, grades in students.items():
                student = self.students[sid]
                f.write("%d\t\t%s\t\t\t%s\n" % (student.id, student.name, Student.str_grades(grades)))

    def group_by_best_subject(self):
        """
        Group by best subjects along best group
        :return:
        """
        subjects = {}  # map from subject name => (best Student, grade)

        # organize data
        for student in self.students.values():
            for g in student.grades:
                if g.name not in subjects:  # add first student to subject
                    subjects[g.name] = {"grade": g.grade, "students": [student]}
                else:  # compare
                    temp_students = subjects[g.name]["students"]
                    temp_grade = subjects[g.name]["grade"]

                    if g.grade > temp_grade:  # found new best
                        subjects[g.name] = {"grade": g.grade, "students": [student]}
                    elif g.grade == temp_grade:  # found equal student
                        temp_students.append(student)
                        subjects[g.name]["students"] = temp_students


        # write to file
        with open("students_best.txt", "w") as f:
            f.write("Subject Name\t\tStudent(s) Name\t\tGrade\n")
            for subject_name, t in subjects.items():
                students = [student.name for student in t["students"]]
                grade = t["grade"]
                f.write("%s\t\t%s\t\t%.2f\n" % (subject_name, ", ".join(students), grade))

    def group_by_age(self, descending=True):
        """
        Group students by age
        :param descending:
        """
        # sort
        students = list(self.students.values())
        students.sort(key=lambda x: x.get_age(), reverse=descending)

        # write to file
        with open("students_age.txt", "w") as f:
            f.write("Id\t\tName\t\t\t\t\tAge\n")
            for student in students:
                f.write("%d\t\t%s\t\t\t%d\n" % (student.id, student.name, student.get_age()))

    def filter_passed_all(self):
        """
        Filter all students who passed all their exams
        """
        students = []  # the list of all students that passed

        # filter items
        for sid, student in self.students.items():
            add = True
            for g in student.grades:
                # non passing grade
                if g.grade < 5.0:
                    add = False
                    break

            # passed all their exams
            if add is True:
                students.append(student)

        # sort
        students.sort(key=lambda x: x.get_average())

        # write to file
        with open("students_all_exams.txt", "w") as f:
            f.write("Id\t\tName\t\t\t\t\tAverage\n")
            for student in students:
                f.write("%d\t\t%s\t\t\t%.2f\n" % (student.id, student.name, student.get_average()))

    def __str__(self, *args, **kwargs):
        return_str = "Repository: \n"
        for student in self.students.values():
            return_str += str(student)

        return return_str
