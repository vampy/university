package chat.services;

import chat.model.Message;
import chat.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 1:32:07 PM
 */
public interface IChatServer
{
    public void login(User user, IChatClient client) throws ChatException;

    public void sendMessage(Message message) throws ChatException;

    public void logout(User user, IChatClient client) throws ChatException;

    public User[] getLoggedFriends(User user) throws ChatException;
}
