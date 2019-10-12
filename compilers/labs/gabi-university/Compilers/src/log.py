

class Log:
    RED = '\033[91m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    END = '\033[0m'

    @classmethod
    def error(cls, message, *args, **kwargs):
        print(cls.RED + message + cls.END, *args, **kwargs)

    @classmethod
    def success(cls, message, *args, **kwargs):
        print(cls.GREEN + message + cls.END, *args, **kwargs)

    @classmethod
    def warning(cls, message, **kwargs):
        print(cls.YELLOW + message + cls.END, **kwargs)

    @classmethod
    def info(cls, message, **kwargs):
        print(message, **kwargs)

