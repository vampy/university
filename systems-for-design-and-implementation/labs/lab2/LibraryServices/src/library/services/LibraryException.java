package library.services;


public class LibraryException extends Exception
{
    public LibraryException()
    {
    }

    public LibraryException(String message)
    {
        super(message);
    }

    public LibraryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
