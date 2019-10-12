package chat.services;

import chat.model.Message;
import chat.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 1:35:30 PM
 */
public interface IChatClient
{
    public void messageReceived(Message message) throws ChatException;

    public void friendLoggedIn(User friend) throws ChatException;

    public void friendLoggedOut(User friend) throws ChatException;
}
