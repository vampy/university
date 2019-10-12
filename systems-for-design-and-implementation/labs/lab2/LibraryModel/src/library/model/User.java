package library.model;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable
{
    private int id = -1;
    private String username;
    private String password;
    private boolean is_librarian = false;

    // ID of all books that this user borrowed
    private Set<Integer> borrowed;

    public User(int id)
    {
        this.id = id;
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password)
    {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password, Set<Integer> borrowed)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.borrowed = borrowed;
    }

    public User(int id, String username, String password, boolean isLibrarian, Set<Integer> borrowed)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.is_librarian = isLibrarian;
        this.borrowed = borrowed;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Set<Integer> getBorrowed()
    {
        return borrowed;
    }

    public void setBorrowed(Set<Integer> borrowed)
    {
        this.borrowed = borrowed;
    }

    public boolean isLibrarian()
    {
        return is_librarian;
    }

    public void setLibrarian(boolean librarian)
    {
        is_librarian = librarian;
    }

    public void addBorrowed(int book_id)
    {
        assert id != 0;
        borrowed.add(book_id);
    }

    public void removeBorrowed(int book_id)
    {
        assert id != 0;
        borrowed.remove(book_id);
    }


    @Override
    public boolean equals(Object obj)
    {
        assert id != 0;
        if (obj instanceof User)
        {
            User user = (User) obj;
            return id == user.getId();
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", is_librarian=" + is_librarian +
            ", borrowed=" + borrowed +
            '}';
    }
}
