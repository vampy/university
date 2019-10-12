using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using chat.model;
namespace chat.persistence
{
    public interface IUserRepository
    {
        bool verifyUser(User user);
        User[] getFriends(User user);

    }
}
