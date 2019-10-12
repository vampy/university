package chat.client.gui;

import chat.model.Message;
import chat.model.User;
import chat.services.ChatException;
import chat.services.IChatClient;
import chat.services.IChatServer;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 2:54:28 PM
 */
public class ChatClientCtrl implements IChatClient
{

    private FriendsListModel  friendsModel;
    private MessagesListModel messagesModel;
    private IChatServer       server;
    private User              user;

    public ChatClientCtrl(IChatServer server)
    {
        this.server = server;
        friendsModel = new FriendsListModel();
        messagesModel = new MessagesListModel();
    }

    public ListModel getFriendsModel()
    {
        return friendsModel;
    }

    public ListModel getMessagesModel()
    {
        return messagesModel;
    }

    public void messageReceived(Message message) throws ChatException
    {
        messagesModel.newMessage(message.getSender().getId(), message.getText());
    }

    public void friendLoggedIn(User friend) throws ChatException
    {
        friendsModel.friendLoggedIn(friend.getId());
    }

    public void friendLoggedOut(User friend) throws ChatException
    {
        friendsModel.friendLoggedOut(friend.getId());
    }

    public void logout()
    {
        try
        {
            server.logout(user, this);
        }
        catch (ChatException e)
        {
            System.out.println("Logout error " + e);
        }
    }

    public void login(String id, String pass) throws ChatException
    {
        User userL = new User(id, pass, "");
        server.login(userL, this);
        user = userL;
        User[] loggedInFriends = server.getLoggedFriends(user);
        System.out.println("Logged friends " + loggedInFriends.length);
        for (User us : loggedInFriends)
        {
            friendsModel.friendLoggedIn(us.getId());
        }

    }

    public void sendMessage(int indexFriend, String txtMsg) throws ChatException
    {
        messagesModel.newMessage(user.getId(), txtMsg);
        User sender = new User(user.getId());
        User receiver = new User((String) friendsModel.getElementAt(indexFriend));
        Message message = new Message(sender, txtMsg, receiver);
        server.sendMessage(message);
    }
}
