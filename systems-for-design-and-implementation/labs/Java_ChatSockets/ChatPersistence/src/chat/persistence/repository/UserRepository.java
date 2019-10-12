package chat.persistence.repository;

import chat.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 1:10:57 PM
 */
public interface UserRepository
{
    public boolean verifyUser(User user);

    public User[] getFriends(User user);

}
