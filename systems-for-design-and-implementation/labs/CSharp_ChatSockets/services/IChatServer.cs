using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using chat.model;
namespace chat.services
{
    public interface IChatServer
    {
        void login(User user, IChatObserver client);
        void sendMessage(Message message);
        void logout(User user, IChatObserver client);
        User[] getLoggedFriends(User user);

    }
}
