using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using chat.services;
using chat.model;

namespace chat.client
{
    public class ChatServerMock:IChatServer
    {
        public void login(User user, IChatObserver client)
        {
        }
        public void sendMessage(Message message)
        {
        }
        public void logout(User user, IChatObserver client)
        {
        }
        public User[] getLoggedFriends(User user)
        {
            return new User[0];
        }
    }
}
