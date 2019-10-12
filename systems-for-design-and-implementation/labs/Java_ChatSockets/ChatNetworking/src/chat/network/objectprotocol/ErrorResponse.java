package chat.network.objectprotocol;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 4:30:15 PM
 */
public class ErrorResponse implements Response
{
    private String message;

    public ErrorResponse(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
