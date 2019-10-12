package chat.network.objectprotocol;

import chat.network.dto.MessageDTO;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 4:26:05 PM
 */
public class SendMessageRequest implements Request
{
    private MessageDTO message;

    public SendMessageRequest(MessageDTO message)
    {
        this.message = message;
    }

    public MessageDTO getMessage()
    {
        return message;
    }
}

