class NumberException(Exception):
    pass


class Number:
    # all hex numbers
    _number_map = "0123456789ABCDEF"

    def __init__(self, str_number="0", base=10):
        self._number_list = ["0"]
        self._number_base = 10
        self._number_str = "0"

        self.set_number(str_number, base)

    def get_number(self) -> str:
        """:returns The number in string format"""
        return self._number_str

    def get_base(self) -> int:
        """:returns The base of the number"""
        return self._number_base

    def get_number_list(self) -> list:
        """:returns The internal number list"""
        return self._number_list[:]

    def set_number(self, str_number, base):
        """
        Set the number and base
        :param str_number
        :param base
        """
        # set the number base
        self._number_base = base

        # the original string passed
        self._number_str = str_number

        # validate and trow exception on error
        self._validate_number_base()
        self._validate_number_str()

        # the list of numbers reversed
        self._number_list = self._number_str_to_number_list(self._number_str, self._number_base)

    @staticmethod
    def _number_str_to_number_list(number_str, number_base) -> list:
        """
        Convert a str to proper list of numbers that are reversed
        e.g: "F9" (base 16) will output [9, 15]
        :returns list
        """
        # reverse the number
        temp_reversed = Number._reverse(number_str)

        # convert to appropriate digits
        return [int(i, number_base) for i in temp_reversed]

    @staticmethod
    def _number_list_to_number_str(number_list):
        """
        Convert a list of number in any base to a string
        """
        if not number_list:  # handle when list is empty
            return "0"

        # reverse to the original state
        temp_reversed = Number._reverse(number_list)

        return "".join([Number._number_map[i] for i in temp_reversed])

    def _validate_number_str(self):
        """
        Check if _number_str is valid
        Raises:
            NumberException - on invalid number
        """
        if not isinstance(self._number_str, str):
            raise NumberException("Must be a string")

        # use python builtin to convert char to number
        try:
            int(self._number_str, self._number_base)
        except ValueError:
            raise NumberException("Number %s is not of base %d" % (self._number_str, self._number_base))

    def _validate_number_base(self):
        """"
        Check if _number_base is valid
        """
        if not isinstance(self._number_base, int):
            raise NumberException("Base is not an int")

        if self._number_base < 2 or self._number_base > 16:
            raise NumberException("Please choose a base between 2 and 16")

    @staticmethod
    def _validate_operation(base1, base2):
        """
        Check if we can do on an operation on these numbers
        :raises NumberException - on invalid number
        """
        if base1 != base2:
            raise NumberException("Can not do operation on 2 different bases. Please convert to a common base")

    @staticmethod
    def _reverse(object_abstract: list):
        """
        Reverse an object that is iterable
        :returns the list reversed
        """
        return object_abstract[::-1]

    @staticmethod
    def _normalize_lists(list1, list2):
        """
        Make the two lists the same length my appending zeroes to the end
        """
        len_list1 = len(list1)
        len_list2 = len(list2)
        len_diff = abs(len_list2 - len_list1)

        if len_diff:
            if len_list1 < len_list2:  # extend the first list
                list1.extend([0] * len_diff)
            else:  # extend second list
                list2.extend([0] * len_diff)

    @staticmethod
    def _normalize_result(result):
        """
        Remove all non significant zeroes from result
        eg: if result = [4, 3, 2, 1, 0, 0, 0] which
            represents the number 0001234
            after normalization
            result = [4, 3, 2, 1] which
            represents the number 1234
        """
        i = len(result) - 1
        while i >= 0 and result[i] == 0:
            i -= 1
            result.pop()

    def __str__(self):
        """
        Str context
        """
        return self.get_number()

    def __add__(self, other):
        """
        Add to another number
        """
        # print "add number list = ", self._number_list, other._number_list

        base = self.get_base()
        self._validate_operation(base, other.get_base())

        number_a = self.get_number_list()
        number_b = other.get_number_list()
        # will hold the result
        number_c = []

        # print number_a, number_b
        # normalize lists
        self._normalize_lists(number_a, number_b)

        len_number_a = len(number_a)

        transport = 0  # transport number, must be integer
        i = 0

        while i < len_number_a:
            temp = number_a[i] + number_b[i] + transport

            # add to the result list
            number_c.append(temp % base)

            # the transport is quotient
            transport = temp // base

            i += 1

        # overflow append transport
        if transport:
            number_c.append(transport)

        return Number(self._number_list_to_number_str(number_c), base)

    def __sub__(self, other):
        """
        Subtract from another number of variable length
        """
        # assume this one if bigger than other
        base = self.get_base()
        self._validate_operation(base, other.get_base())

        number_a = self.get_number_list()
        number_b = other.get_number_list()
        # will hold the result
        number_c = []

        # normalize lists
        self._normalize_lists(number_a, number_b)

        len_number_a = len(number_a)

        transport = 0  # transport number
        i = 0
        while i < len_number_a:

            # check if we need to borrow
            if number_a[i] + transport < number_b[i]:
                temp = (number_a[i] + transport + base) - number_b[i]

                # set transport for next iteration
                transport = -1
            else:  # number_a[i] is bigger than number_b[i] NO need for borrow
                temp = (number_a[i] + transport) - number_b[i]
                transport = 0

            # temp = (number_a[i] + transport) - number_b[i]
            # print "temp", temp
            # # if the result is negative that means that the digit in number_a is smaller than number_b
            # if temp < 0:
            #    # set transport for next iteration
            #    transport = -1
            #
            #    # add the appropriate borrow
            #    temp += base
            # else:
            #    transport = 0

            # add to the result list
            number_c.append(temp)

            i += 1

        self._normalize_result(number_c)

        return Number(self._number_list_to_number_str(number_c), base)

    def __mul__(self, other):
        """
        Multiply with another number of 1 digit aka a scalar
        """
        base = self.get_base()
        number_a = self.get_number_list()

        if isinstance(other, Number):
            # works only with one digit
            self._validate_operation(base, other.get_base())
            number_b = other._number_list[:]
            number_b = number_b[0]
        elif isinstance(other, int):
            number_b = other
        else:
            raise NumberException("Wrong input")

        # will hold the result
        number_c = []
        len_number_a = len(number_a)
        transport = 0  # transport number, must be an integer
        i = 0

        while i < len_number_a:
            temp = number_a[i] * number_b + transport

            # add to the result list
            number_c.append(temp % base)

            # the transport is the quotient
            transport = temp // base
            i += 1

        # if overflow append transport
        while transport:
            number_c.append(transport % base)
            transport //= base

        self._normalize_result(number_c)

        return Number(self._number_list_to_number_str(number_c), base)

    def __divmod__(self, other) -> tuple:
        """
        Divide and return quotient and remainder
        """
        base = self.get_base()
        number_a = self.get_number_list()

        if isinstance(other, Number):
            # works only with one digit
            self._validate_operation(base, other.get_base())
            number_b = other._number_list[:]
            number_b = number_b[0]
        elif isinstance(other, int):
            number_b = other
        else:
            raise NumberException("Wrong input")

        # will hold the result
        number_c = []

        len_number_a = len(number_a)

        remainder = 0  # transport number
        i = len_number_a - 1
        while i >= 0:
            temp = remainder * base + number_a[i]

            number_c.append(temp // number_b)

            remainder = temp % number_b

            i -= 1

        # reverse it because we inserted in reverse order
        number_c = self._reverse(number_c)

        return Number(self._number_list_to_number_str(number_c), base), remainder

    def __floordiv__(self, other):
        return divmod(self, other)[0]

    def __mod__(self, other):
        return divmod(self, other)[1]

    def is_zero(self):
        """
        Check if number is zero
        :returns True or False
        """
        if len(self._number_list) == 1 and self._number_list[0] == 0:
            return True

        for i in self._number_list:
            if i != 0:
                return False

        return True

    def convert_substitution(self, destination_base: int):
        """
        Convert number to another base using substitution method
        :param destination_base
        :returns A new Number object
        """
        if destination_base < 2 or destination_base > 16:
            raise NumberException("Base inputted is invalid")

        number = self.get_number_list()
        source_base = self.get_base()

        # because of our implementation we perform the calculation in the destination base
        result = Number("0", destination_base)
        power = Number("1", destination_base)
        for digit in number:
            # take every digit and multiply it by the corresponding power
            result += power * digit

            # increase the power for the next iteration
            # e.g for base 2, the power will be: 1, 2, 4, 8, etc
            power *= source_base

        self.set_number(result.get_number(), destination_base)

        return self

    def convert_division(self, destination_base: int):
        """
        Convert number to another base using division method
        NOTE: only works with decimal numbers, and our internal number representation has decimal numbers,
        so we can use the overwritten // and % operators.

        :param destination_base
        :returns A new Number object
        """
        if destination_base < 2 or destination_base > 16:
            raise NumberException("Base inputted is invalid")

        number = Number(self.get_number(), self.get_base())

        result = []
        while not number.is_zero():
            quotient, remainder = divmod(number, destination_base)

            # The remainder is part of the result
            result.append(remainder)

            # Use next quotient for the division
            number = quotient

        self.set_number(self._number_list_to_number_str(result), destination_base)

        return self

    @staticmethod
    def _validate_rapid_conversion(source_base, destination_base):
        """
        Checks if source_base if a power of destination_base or destination_base is a power of source_base
        :raises NumberException on invalid bases
        """

        # checks if b is a power of a
        def _check(a, b):
            i = 1
            while i < b:
                i *= a

            return i == b

        if _check(source_base, destination_base) or _check(destination_base, source_base):
            return  # do nothing

        raise NumberException("Can not use rapid conversion")

    def convert_rapid(self, destination_base):
        """
        Convert number to another base using rapid conversions
        NOTE: For this method to work, one of the bases must be a power of another one
        e.g: 2 and 16 (2^4 = 16), 3 and 9 (3^2 = 9)

        :param destination_base string
        :returns self
        """
        if destination_base < 2 or destination_base > 16:
            raise NumberException("Base inputted is invalid")

        source_base = self.get_base()
        self._validate_rapid_conversion(source_base, destination_base)

        number = self.get_number_list()
        result = []

        def get_len_group(base_a, base_b):
            # calculate the length of the group of replaced digits in base_b

            # swap bases, base_a must be smaller than base_b
            if base_a > base_b:
                base_a, base_b = base_b, base_a

            k = base_a
            length = 1
            while k < base_b:
                k *= base_a
                length += 1

            return length

        # the length of the group of digits
        len_group = get_len_group(source_base, destination_base)
        len_number = len(number)

        # convert to smaller base
        if source_base > destination_base:
            for i in range(len_number):
                # the number of digits to convert to
                for j in range(len_group):
                    result.append(number[i] % destination_base)
                    number[i] //= destination_base

        else:  # convert to larger base
            i = 0

            # compute the number
            while i < len_number:
                power = 1
                temp = 0
                j = 0
                while j < len_group and i < len_number:
                    temp += power * number[i]
                    power *= source_base
                    i += 1
                    j += 1

                result.append(temp)

        self.set_number(self._number_list_to_number_str(result), destination_base)
        return self
