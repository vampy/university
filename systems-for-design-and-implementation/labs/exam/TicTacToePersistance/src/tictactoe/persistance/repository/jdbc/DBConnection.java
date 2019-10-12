package tictactoe.persistance.repository.jdbc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    private static DBConnection instance;
    private static String XML_FILE = "database.xml";
    private Connection connection;

    private DBConnection()
    {
        initConnection();
    }

    public static DBConnection get()
    {
        if (instance == null)
        {
            instance = new DBConnection();
        }

        return instance;
    }

    private void initConnection()
    {
        try
        {
            File fXmlFile = new File(XML_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            Element configElement = doc.getDocumentElement();

            String driver = configElement.getElementsByTagName("driver").item(0).getTextContent();
            String username = configElement.getElementsByTagName("username").item(0).getTextContent();
            String password = configElement.getElementsByTagName("password").item(0).getTextContent();
            String database = configElement.getElementsByTagName("database").item(0).getTextContent();
            String url = configElement.getElementsByTagName("url").item(0).getTextContent() + database;

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
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (connection == null)
        {
            throw new RuntimeException("The SQL Connection was not set");
        }
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
