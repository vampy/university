using System;
using System.Collections.Generic;
using chat.model;

namespace chat.persistence
{
    namespace repository.mock
    {
    public class UserRepositoryMock: IUserRepository{
    private readonly IDictionary<String, User> allUsers;

    public UserRepositoryMock() {
        allUsers=new Dictionary<String, User>();
        populateUsers();
    }
    public bool verifyUser(User user) {
        User userR;
        bool  ok=allUsers.TryGetValue(user.Id,out userR);
        if (!ok)
            return false;
        return userR.Password==user.Password;
    }

    public User[] getFriends(User user) {
        User userR;
        if (allUsers.TryGetValue(user.Id, out userR))
            return userR.Friends;
        return new User[0];
    }

    private void populateUsers(){
        User ana=new User("ana", "ana", "Popescu Ana");
        User mihai=new User("mihai", "mihai", "Ionescu Mihai");
        User ion=new User("ion", "ion", "Vasilescu Ion");
        User maria=new User("maria", "maria", "Marinescu Maria");
        User test=new User("test", "test", "Test user");

        ana.addFriend(mihai);
        ana.addFriend(test);

        mihai.addFriend(ana);
        mihai.addFriend(test);
        mihai.addFriend(ion);

        ion.addFriend(maria);
        ion.addFriend(test);
        ion.addFriend(mihai);

        maria.addFriend(ion);
        maria.addFriend(test);

        test.addFriend(ana);
        test.addFriend(mihai);
        test.addFriend(ion);
        test.addFriend(maria);

        allUsers[ana.Id]=ana;
        allUsers[mihai.Id]= mihai;
        allUsers[ion.Id]= ion;
        allUsers[maria.Id]= maria;
        allUsers[test.Id]= test;


    }



        }
    }
}
