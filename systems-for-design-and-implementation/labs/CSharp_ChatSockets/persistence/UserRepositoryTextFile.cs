using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using chat.model;

namespace chat.persistence
{
    namespace repository.file
    {
        class UserRepositoryTextFile:IUserRepository
        {
            private const String userFile = "users.txt";
            private const String friendsFile = "friends.txt";
            private readonly IDictionary<String, User> allUsers;
            public UserRepositoryTextFile()
            {
                allUsers = new Dictionary<String, User>();
                readUsers();
            }

            public bool verifyUser(User user)
            {
                User userR = allUsers[user.Id];
                if (userR == null)
                    return false;
                return userR.Password == user.Password;
            }

            public User[] getFriends(User user)
            {
                User userR = allUsers[user.Id];
                if (userR != null)
                    return userR.Friends;
                return new User[0];
            }

            private void readUsers()
            {
                //read users first
                using(TextReader inFile=File.OpenText(userFile))
                {
                    String line;
                    while((line=inFile.ReadLine())!=null)
                    {
                        String[] elems = line.Split(new char[] {':'});
                        if (elems.Length<3)
                        {
                            Console.WriteLine("[UserFileRepository ]Invalid line "+line);
                            continue;
                        }
                        User user=new User(elems[0],elems[1],elems[2]);
                        allUsers[user.Id] = user;
                    }
                }
                //read friends
                using(TextReader inFile=File.OpenText(friendsFile))
                {
                    String line;
                    while ((line = inFile.ReadLine()) != null)
                    {
                        String[] elems = line.Split(new char[] { ':' });
                        if (elems.Length < 1)
                        {
                            Console.WriteLine("[FriendFileRepository ]Invalid line " + line);
                            continue;
                        }
                        User user = allUsers[elems[0]];
                        for (int i = 1; i < elems.Length; i++)
                        {
                            User friend = allUsers[elems[i]];
                            user.addFriend(friend);
                        }
                      
                    }
                    
                }
            }
        }
    
    }
    
}
