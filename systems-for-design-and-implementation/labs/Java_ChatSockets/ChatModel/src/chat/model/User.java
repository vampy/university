package chat.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 12:59:04 PM
 */
public class User implements Comparable<User>, Serializable
{
    private String Id, passwd, name;
    private Set<User> friends;


    public User()
    {
        this("");
    }

    public User(String id)
    {
        this(id, "", "");
    }

    public User(String id, String passwd)
    {
        this(id, passwd, "");
    }

    public User(String id, String passwd, String name)
    {
        Id = id;
        this.passwd = passwd;
        this.name = name;
        friends = new TreeSet<User>();
    }

    public String getPasswd()
    {
        return passwd;
    }

    public String getId()
    {
        return Id;
    }

    public String getName()
    {
        return name;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFriends(Set<User> friends)
    {
        this.friends = friends;
    }

    public Set<User> getFriends()
    {
        return friends;
    }

    public User[] getFriendsArray()
    {
        return friends.toArray(new User[friends.size()]);
    }

    public void addFriend(User u)
    {
        friends.add(u);

    }

    public void removeFriend(User u)
    {
        friends.add(u);
    }


    public int compareTo(User o)
    {
        return Id.compareTo(o.Id);
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof User)
        {
            User u = (User) obj;
            return Id.equals(u.Id);
        }
        return false;
    }

    @Override
    public String toString()
    {
     /*  StringBuilder friendsString=new StringBuilder();
        for (User user : friends){
            friendsString.append(user.getName());
            friendsString.append(", ");
        }*/
        return "User{" +
            "Id='" + Id + '\'' +
            ", name='" + name + '\'' +
            // ", friends='"+friendsString+'\''+
            '}';
    }

    @Override
    public int hashCode()
    {
        return Id != null ? Id.hashCode() : 0;
    }
}
