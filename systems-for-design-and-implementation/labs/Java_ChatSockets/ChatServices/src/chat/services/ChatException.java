package chat.services;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 1:32:58 PM
 */
public class ChatException extends Exception
{
    public ChatException()
    {
    }

    public ChatException(String message)
    {
        super(message);
    }

    public ChatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
