package library.persistance.repository.jdbc;

import library.model.User;
import library.model.UserException;
import library.persistance.repository.IUserRepository;
import library.persistance.repository.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserRepositoryJDBC implements IUserRepository
{
    private Set<Integer> getBorrowedBooks(int user_id)
    {
        Connection connection = DBConnection.get().getConnection();
        Set<Integer> books = new HashSet<Integer>();

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT book_id FROM loans WHERE user_id = ?");
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                books.add(rs.getInt("book_id"));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }

    @Override
    public boolean verifyUser(User user)
    {
        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        catch (SQLException e)
        {
            throw new UserException("Error " + e);
        }
    }

    @Override
    public User getUserByUsername(String username)
    {
        Connection connection = DBConnection.get().getConnection();
        User user = null;

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("id");
                boolean is_librarian = rs.getInt("is_librarian") == 1;
                user = new User(id, rs.getString("username"), rs.getString("password"), is_librarian, getBorrowedBooks(id));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return user;
    }
}
