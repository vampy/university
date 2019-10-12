using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using chat.model;
using chat.persistence;
using chat.persistence.repository;
using chat.services;
using System;
namespace chat.server
{
    public class ChatServerImpl: IChatServer
    {
        private IUserRepository userRepository;
        private readonly IDictionary <String, IChatObserver> loggedClients;


        public ChatServerImpl()
        {
            userRepository = Persistence.getInstance().createUserRepository();
            loggedClients = new Dictionary<String, IChatObserver>();
        }
    public ChatServerImpl(IUserRepository repo) {
        userRepository = repo;
        loggedClients=new Dictionary<String, IChatObserver>();
    }

    public  void login(User user, IChatObserver client)  {
        bool loginOk=userRepository.verifyUser(user);
        if (loginOk){
            if(loggedClients.ContainsKey(user.Id))
                throw new ChatException("User already logged in.");
            loggedClients[user.Id]= client;
            notifyFriendsLoggedIn(user);
        }else
            throw new ChatException("Authentication failed.");
    }

    private void notifyFriendsLoggedIn(User user){
        User[] friends=userRepository.getFriends(user);
        Console.WriteLine("notify logged friends "+friends.Count());
        foreach(User us in friends){
            if (loggedClients.ContainsKey(us.Id))
            {
                IChatObserver chatClient = loggedClients[us.Id];
					Task.Run(() => chatClient.friendLoggedIn(user));
            }
        }
    }

    private void notifyFriendsLoggedOut(User user) {
        User[] friends=userRepository.getFriends(user);
        foreach(User us in friends){
            if (loggedClients.ContainsKey(us.Id))
            {
                IChatObserver chatClient = loggedClients[us.Id];
					Task.Run(() =>chatClient.friendLoggedOut(user));
            }
        }
    }

    public  void sendMessage(Message message)  {
        String id_receiver=message.Receiver.Id;
        
        if (loggedClients.ContainsKey(id_receiver))  {     //the receiver is logged in
            IChatObserver receiverClient=loggedClients[id_receiver];
				Task.Run(() => receiverClient.messageReceived(message));
        }
        else
            throw new ChatException("User "+id_receiver+" not logged in.");
    }

    public  void logout(User user, IChatObserver client) {
        IChatObserver localClient=loggedClients[user.Id];
        if (localClient==null)
            throw new ChatException("User "+user.Id+" is not logged in.");
        loggedClients.Remove(user.Id);
        notifyFriendsLoggedOut(user);
    }

    public  User[] getLoggedFriends(User user)  {
        User[] friends=userRepository.getFriends(user);
        IList<User> result=new List<User>();
        Console.WriteLine("Logged friends for: "+user.Id);
        foreach (User friend in friends){
            if (loggedClients.ContainsKey(friend.Id)){    //the friend is logged in
                result.Add(new User(friend.Id));
                Console.WriteLine("+"+friend.Id);
            }
        }
        Console.WriteLine("Size "+result.Count);
        return result.ToArray();
    }

       
    }
}
