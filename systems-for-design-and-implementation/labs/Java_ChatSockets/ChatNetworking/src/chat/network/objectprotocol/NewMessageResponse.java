package chat.network.objectprotocol;

import chat.network.dto.MessageDTO;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 4:35:38 PM
 */
public class NewMessageResponse implements UpdateResponse
{
    private MessageDTO message;

    public NewMessageResponse(MessageDTO message)
    {
        this.message = message;
    }

    public MessageDTO getMessage()
    {
        return message;
    }
}
