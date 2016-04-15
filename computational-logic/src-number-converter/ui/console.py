"""
@author: Daniel Butum, Group 911
"""

from domain.number import Number, NumberException
from utils.number import convert_to_int


class Console:

    def __init__(self):
        pass

    def run(self):
        """
        Run the console gui
        """
        # print initial help menu
        print(self._get_help_menu())

        while True:
            option = self._get_command()

            try:
                if option == "q":  # quit the loop
                    break

                elif option == "h":  # display help
                    print(self._get_help_menu())

                elif option == "1":  # add two numbers
                    print("Addition")
                    base = self._get_int_command("Base of the numbers: ")

                    number_a = Number(self._get_command("Number a: "), base)
                    number_b = Number(self._get_command("Number b: "), base)

                    print("Result: {0} (base {1})".format(number_a + number_b, base))

                elif option == "2":
                    print("Subtraction")
                    base = self._get_int_command("Base of the numbers: ")

                    number_a = Number(self._get_command("Number a(must be larger than b): "), base)
                    number_b = Number(self._get_command("Number b: "), base)

                    print("Result: {0} (base {1})".format(number_a - number_b, base))

                elif option == "3":
                    print("Multiplication")
                    base = self._get_int_command("Base of the number: ")

                    number = Number(self._get_command("Number: "), base)
                    scalar = self._get_int_command("Scalar: ")

                    print("Result: {0} (base {1})".format(number * scalar, base))

                elif option == "4":
                    print("Division")
                    base = self._get_int_command("Base of the number: ")

                    number = Number(self._get_command("Number: "), base)
                    scalar = self._get_int_command("Scalar: ")
                    quotient, remainder = divmod(number, scalar)

                    print("Quotient = {0} (base {1}), \n Remainder = {2}".format(quotient, base, remainder))

                elif option == "5" or option == "6" or option == "7":
                    if option == "5":
                        print("Conversion using substitution")
                    if option == "6":
                        print("Conversion using multiplication and division method")
                    if option == "7":
                        print("Using rapid conversions")

                    source_base = self._get_int_command("Base of the number: ")
                    number = Number(self._get_command("Number: "), source_base)
                    destination_base = self._get_int_command("Destination base: ")

                    if option == "5":
                        number.convert_substitution(destination_base)
                    if option == "6":
                        number.convert_division(destination_base)
                    if option == "7":
                        number.convert_rapid(destination_base)

                    print("Result: {0} (base {1})".format(number, destination_base))

                else:
                    print("Option does not exist. Please try again")

            except NumberException as ex:
                print(ex)

    @staticmethod
    def _get_help_menu():
        """
        Returns the menu
        """
        return """Basic operations and conversions by Daniel Butum

    1. Add two numbers
    2. Subtract two numbers
    3. Multiplication by one digit
    4. Division by one digit

    Base conversion using:
        5. substitution method
        6. successive divisions method
        7. rapid conversions

    h. Display this help menu
    q. Quit
"""

    @staticmethod
    def _input(message):
        """
        Improved input
        Return:
            string - user command
        """
        try:
            return input(message).strip()
        except KeyboardInterrupt:  # handle CTRL + C interrupt
            return exit('CTRL + D')
        except EOFError:  # handle CTRL + D
            return exit('CTRL + D')

    @staticmethod
    def _get_command(message=">>> "):
        """
        Gets the command inputted by the user
        """
        return Console._input(message).lower()

    @staticmethod
    def _get_int_command(message, not_empty=True):
        """
        Gets the command inputted by the user if is an int
        """
        command = Console._input(message)
        while convert_to_int(command) is None:
            # if empty command and option is set => return
            if not not_empty and command == "":
                return command

            print("Please retry again with an integer")
            command = Console._input(message)

        return int(command)

    @staticmethod
    def display_help():
        """
        Display the message to the user
        """
        print(Console._get_help_menu())
