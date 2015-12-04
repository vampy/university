class Session:
    # session message
    _message = []

    @staticmethod
    def get_message() -> list(str):
        """
        Get the list of all session messages
        """
        return_str = ""
        for message in Session._message:
            return_str += message + "\n"

        # clean session for next iteration
        Session._message = []

        return return_str

    @staticmethod
    def set_message(message: str):
        """
        Sets a session specific message into a list

        :param message
        """
        Session._message.append(message)
