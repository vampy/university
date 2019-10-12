import os


class Util:
    # Representation in our productions will be None
    EMPTY_STRING = ['eps', 'epsilon', 'ε']

    @staticmethod
    def is_empty_string(string):
        string_lower = string.lower()
        return any([string_lower == e for e in Util.EMPTY_STRING])

    @staticmethod
    def convert_to_type(value, value_type, default_value=None):
        """
        Try to convert 'value' to type
        :param value: The value to convert
        :param value_type: The type to convert to eg: int, float, bool
        :param default_value: The default returned value if the conversion fails
        :return:
        """
        if value is None:
            return default_value

        try:
            return value_type(value)
        except ValueError:
            return default_value

    @staticmethod
    def convert_to_int(value, default_value=None):
        """
        Try to convert a value to an int
        :param value: The value to convert
        :param default_value: The default returned value if the conversion fails
        :return: The converted value
        """
        return Util.convert_to_type(value, int, default_value)

    @staticmethod
    def convert_to_float(value, default_value=None):
        """
        Try to convert a value to an float
        :param value: The value to convert
        :param default_value: The default returned value if the conversion fails
        :return: The converted value
        """
        return Util.convert_to_type(value, float, default_value)

    @staticmethod
    def get_lines_filename(filename):
        if not os.path.exists(filename):
            raise FileExistsError('The file "{0}" does not exist'.format(filename))

        with open(filename, 'r') as f:
            return f.readlines()

    @staticmethod
    def get_data_filename(filename):
        if not os.path.exists(filename):
            raise FileExistsError('The file "{0}" does not exist'.format(filename))

        with open(filename, 'r') as f:
            return f.read()

