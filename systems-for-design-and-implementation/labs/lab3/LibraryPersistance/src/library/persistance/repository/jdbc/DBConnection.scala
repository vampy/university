package library.persistance.repository.jdbc

import java.io.File
import java.sql.{Connection, DriverManager, SQLException}
import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}

import org.w3c.dom.{Document, Element}

object DBConnection
{
    private val XML_FILE: String = "database.xml"
    private var instance: DBConnection = null

    def get: DBConnection =
    {
        if (instance == null)
        {
            instance = new DBConnection
        }
        instance
    }
}

class DBConnection private
{
    private var connection: Connection = null
    initConnection()

    def getConnection: Connection =
    {
        try
        {
            if (connection != null && connection.isClosed)
            {
                initConnection()
            }
        }
        catch
        {
            case e: SQLException =>
                println("Error DB " + e)
        }
        connection
    }

    private def initConnection()
    {
        try
        {
            val fXmlFile: File = new File(DBConnection.XML_FILE)
            val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance
            val dBuilder: DocumentBuilder = dbFactory.newDocumentBuilder
            val doc: Document = dBuilder.parse(fXmlFile)
            doc.getDocumentElement.normalize()
            val configElement: Element = doc.getDocumentElement
            val driver: String = configElement.getElementsByTagName("driver").item(0).getTextContent
            val username: String = configElement.getElementsByTagName("username").item(0).getTextContent
            val password: String = configElement.getElementsByTagName("password").item(0).getTextContent
            val database: String = configElement.getElementsByTagName("database").item(0).getTextContent
            val url: String = configElement.getElementsByTagName("url").item(0).getTextContent + database
            try
            {
                Class.forName(driver)
                println(String.format("Driver %s loaded", driver))
                connection = DriverManager.getConnection(url, username, password)
            }
            catch
            {
                case e: ClassNotFoundException =>
                    println("Error loading driver " + e)
                case e: SQLException =>
                    println("Error getting connection " + e)
            }
        }
        catch
        {
            case e: Exception => e.printStackTrace()
        }
        if (connection == null)
        {
            throw new RuntimeException("The SQL Connection was not set")
        }
    }
}
