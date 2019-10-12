package com.aop.controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    private Connection connection;
    private static DBConnection instance;

    private DBConnection()
    {
        initConnection();
    }

    private void initConnection()
    {
        String driver   = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "<yourpassword>";
        String database = "school_aop";
        String url      = "jdbc:mysql://localhost:3306/" + database;

        try
        {
            Class.forName(driver);
            System.out.println(String.format("Driver %s loaded", driver));

            connection = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error loading driver " + e);
        }
        catch (SQLException e)
        {
            System.out.println("Error getting connection " + e);
        }
    }

    public static DBConnection get()
    {
        if (instance == null)
        {
            instance = new DBConnection();
        }

        return instance;
    }

    public Connection getConnection()
    {
        try
        {
            if (connection != null && connection.isClosed())
            {
                initConnection();
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error DB " + e);
        }

        return connection;
    }
}
