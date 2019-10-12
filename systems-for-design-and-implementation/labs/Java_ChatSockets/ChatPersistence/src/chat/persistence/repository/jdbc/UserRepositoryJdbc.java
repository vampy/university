package chat.persistence.repository.jdbc;

import chat.model.User;
import chat.persistence.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Apr 15, 2009
 * Time: 2:24:34 PM
 */
public class UserRepositoryJdbc implements UserRepository
{
    public boolean verifyUser(User user)
    {
        System.out.println("Jdbc verify user");
        Connection con = getConnection();
        try
        {
            PreparedStatement preStmt = con.prepareStatement("select name from users where userId=? and password=?");
            preStmt.setString(1, user.getId());
            preStmt.setString(2, user.getPasswd());
            ResultSet result = preStmt.executeQuery();
            boolean resOk = result.next();
            System.out.println("verify user " + resOk);
            return resOk;
        }
        catch (SQLException e)
        {
            System.out.println("Error DB " + e);
        }
        return false;
    }

    public User[] getFriends(User user)
    {
        System.out.println("JDBC get friends");
        Connection con = getConnection();
        ArrayList<User> result = new ArrayList<User>();
        try
        {
            PreparedStatement preStmt = con.prepareStatement("select friends.friendId  from friends where friends.userId=?");
            preStmt.setString(1, user.getId());
            ResultSet rs = preStmt.executeQuery();
            while (rs.next())
            {
                User u = new User(rs.getString(1));
                result.add(u);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error DB " + e);
        }
        return result.toArray(new User[result.size()]);
    }

    private static Connection instance = null;

    private Connection getNewConnection()
    {
        String driver = System.getProperty("chat.jdbc.driver");
        String url = System.getProperty("chat.jdbc.url");
        String user = System.getProperty("chat.jdbc.user");
        String pass = System.getProperty("chat.jdbc.pass");
        Connection con = null;
        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, pass);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error loading driver " + e);
        }
        catch (SQLException e)
        {
            System.out.println("Error getting connection " + e);
        }
        return con;
    }

    public Connection getConnection()
    {
        try
        {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        }
        catch (SQLException e)
        {
            System.out.println("Error DB " + e);
        }
        return instance;
    }
}
