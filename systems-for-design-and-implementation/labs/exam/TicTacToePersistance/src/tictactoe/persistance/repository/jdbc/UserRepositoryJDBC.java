package tictactoe.persistance.repository.jdbc;

import tictactoe.model.Game;
import tictactoe.model.User;
import tictactoe.model.UserException;
import tictactoe.persistance.repository.IUserRepository;
import tictactoe.persistance.repository.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserRepositoryJDBC implements IUserRepository
{
    private Set<Game> getGamesPlayed(int user_id)
    {
        Connection connection = DBConnection.get().getConnection();
        Set<Game> games = new HashSet<Game>();

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, user_1_id, user_2_id, result" +
                " FROM games WHERE user_1_id = ? OR user_2_id = ?");
            stmt.setInt(1, user_id);
            stmt.setInt(2, user_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                games.add(new Game(rs.getInt("id"), rs.getInt("user_1_id"), rs.getInt("user_2_id"), rs.getInt("result")));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return games;
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
                user = new User(id, rs.getString("username"), rs.getString("password"));
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
