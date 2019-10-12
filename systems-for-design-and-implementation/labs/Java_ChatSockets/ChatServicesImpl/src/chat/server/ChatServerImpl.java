package chat.server;

import chat.model.Message;
import chat.model.User;
import chat.persistence.repository.UserRepository;
import chat.services.ChatException;
import chat.services.IChatClient;
import chat.services.IChatServer;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 1:39:47 PM
 */
public class ChatServerImpl implements IChatServer
{
    private UserRepository           userRepository;
    private Map<String, IChatClient> loggedClients;

    public ChatServerImpl(UserRepository repo)
    {
        userRepository = repo;
        loggedClients = new ConcurrentHashMap<>();
    }

    public synchronized void login(User user, IChatClient client) throws ChatException
    {
        boolean loginOk = userRepository.verifyUser(user);
        if (loginOk)
        {
            if (loggedClients.get(user.getId()) != null)
                throw new ChatException("User already logged in.");
            loggedClients.put(user.getId(), client);
            notifyFriendsLoggedIn(user);
        }
        else
            throw new ChatException("Authentication failed.");
    }

    private boolean isLogged(User u)
    {
        return loggedClients.get(u.getId()) != null;
    }

    private void notifyFriendsLoggedIn(User user) throws ChatException
    {
        User[] friends = userRepository.getFriends(user);
        System.out.println("Logged " + friends);

        ExecutorService executor = Executors.newFixedThreadPool(friends.length);
        for (User us : friends)
        {
            IChatClient chatClient = loggedClients.get(us.getId());
            if (chatClient != null)
                executor.execute(() -> {
                    try
                    {
                        System.out.println("Notifying [" + us.getId() + "] friend [" + user.getId() + "] logged in.");
                        chatClient.friendLoggedIn(user);
                    }
                    catch (ChatException e)
                    {
                        System.out.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();
    }

    private void notifyFriendsLoggedOut(User user) throws ChatException
    {
        User[] friends = userRepository.getFriends(user);
        ExecutorService executor = Executors.newFixedThreadPool(friends.length);

        for (User us : friends)
        {
            IChatClient chatClient = loggedClients.get(us.getId());
            if (chatClient != null)
                executor.execute(() -> {
                    try
                    {
                        System.out.println("Notifying [" + us.getId() + "] friend [" + user.getId() + "] logged out.");
                        chatClient.friendLoggedOut(user);
                    }
                    catch (ChatException e)
                    {
                        System.out.println("Error notifying friend " + e);
                    }
                });

        }
        executor.shutdown();
    }

    public synchronized void sendMessage(Message message) throws ChatException
    {
        String id_receiver = message.getReceiver().getId();
        IChatClient receiverClient = loggedClients.get(id_receiver);
        if (receiverClient != null)       //the receiver is logged in

            receiverClient.messageReceived(message);
        else
            throw new ChatException("User " + id_receiver + " not logged in.");
    }

    public synchronized void logout(User user, IChatClient client) throws ChatException
    {
        IChatClient localClient = loggedClients.remove(user.getId());
        if (localClient == null)
            throw new ChatException("User " + user.getId() + " is not logged in.");
        notifyFriendsLoggedOut(user);
    }

    public synchronized User[] getLoggedFriends(User user) throws ChatException
    {
        User[] friends = userRepository.getFriends(user);
        Set<User> result = new TreeSet<User>();
        System.out.println("Logged friends for: " + user.getId());
        for (User friend : friends)
        {
            if (loggedClients.containsKey(friend.getId()))
            {    //the friend is logged in
                result.add(new User(friend.getId()));
                System.out.println("+" + friend.getId());
            }
        }
        System.out.println("Size " + result.size());
        return result.toArray(new User[result.size()]);
    }
}
