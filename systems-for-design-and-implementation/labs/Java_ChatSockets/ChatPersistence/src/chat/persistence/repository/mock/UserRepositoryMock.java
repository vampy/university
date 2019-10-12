package chat.persistence.repository.mock;

import chat.model.User;
import chat.persistence.repository.UserRepository;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 1:13:54 PM
 */
public class UserRepositoryMock implements UserRepository
{
    private Map<String, User> allUsers;

    public UserRepositoryMock()
    {
        allUsers = new TreeMap<String, User>();
        populateUsers();
    }

    public boolean verifyUser(User user)
    {
        User userR = allUsers.get(user.getId());
        if (userR == null)
            return false;
        return userR.getPasswd().equals(user.getPasswd());
    }

    public User[] getFriends(User user)
    {
        User userR = allUsers.get(user.getId());
        if (userR != null)
            return userR.getFriendsArray();
        return new User[0];
    }

    private void populateUsers()
    {
        User ana = new User("ana", "ana", "Popescu Ana");
        User mihai = new User("mihai", "mihai", "Ionescu Mihai");
        User ion = new User("ion", "ion", "Vasilescu Ion");
        User maria = new User("maria", "maria", "Marinescu Maria");
        User test = new User("test", "test", "Test user");

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

        allUsers.put(ana.getId(), ana);
        allUsers.put(mihai.getId(), mihai);
        allUsers.put(ion.getId(), ion);
        allUsers.put(maria.getId(), maria);
        allUsers.put(test.getId(), test);


    }

}
