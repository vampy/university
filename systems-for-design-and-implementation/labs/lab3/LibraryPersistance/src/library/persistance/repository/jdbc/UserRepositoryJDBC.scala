package library.persistance.repository.jdbc

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.util

import library.model.{User, UserException}
import library.persistance.repository.{IUserRepository, RepositoryException}

class UserRepositoryJDBC extends IUserRepository
{
    def verifyUser(user: User): Boolean =
    {
        val connection: Connection = DBConnection.get.getConnection
        assert(connection != null)
        try
        {
            val stmt: PreparedStatement = connection
                .prepareStatement("SELECT * FROM users WHERE username=? AND password=?")
            stmt.setString(1, user.getUsername)
            stmt.setString(2, user.getPassword)
            val rs: ResultSet = stmt.executeQuery
            rs.next
        }
        catch
        {
            case e: SQLException =>
                throw new UserException("Error " + e)
        }
    }

    def getUserByUsername(username: String): User =
    {
        val connection: Connection = DBConnection.get.getConnection
        var user: User = null
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")
            stmt.setString(1, username)
            val rs: ResultSet = stmt.executeQuery
            if (rs.next)
            {
                val id: Int = rs.getInt("id")
                val is_librarian: Boolean = rs.getInt("is_librarian") == 1
                user = new User(id, rs.getString("username"), rs.getString("password"), is_librarian,
                    getBorrowedBooks(id))
            }
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        user
    }

    private def getBorrowedBooks(user_id: Int): util.Set[Integer] =
    {
        val connection: Connection = DBConnection.get.getConnection
        val books: util.Set[Integer] = new util.HashSet[Integer]
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement("SELECT book_id FROM loans WHERE user_id = ?")
            stmt.setInt(1, user_id)
            val rs: ResultSet = stmt.executeQuery
            while (rs.next)
            {
                {
                    books.add(rs.getInt("book_id"))
                }
            }
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        books
    }
}
