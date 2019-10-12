package project.model;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import project.controller.DB;

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
        try
        {
            return DB.get().getJdbcTemplate()
                .queryForObject("SELECT * FROM users WHERE username=?", new UserMapper(), username);
        }
        catch (EmptyResultDataAccessException e)
        {
            System.out.println("Username does not exist");
            return null;
        }
    }

    @Override
    public String toString()
    {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            '}';
    }

    private static class UserMapper implements RowMapper<User>
    {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return new User(rs.getInt("id"), rs.getString("username"));
        }
    }
}
