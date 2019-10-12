package com.aop.model;

import com.aop.controller.DBConnection;
import com.aop.log.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User
{
    private int    id;
    private String username;

    public User(int id, String username)
    {
        this.id = id;
        this.username = username;
    }

    public int getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public static User login(String username)
    {
        User user;
        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username=?");
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
            {
                return null;
            }
            user = new User(rs.getInt("id"), rs.getString("username"));
        }
        catch (SQLException e)
        {
            throw new UserException("Error " + e);
        }

        return user;
    }

    @Override
    public String toString()
    {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            '}';
    }
}
