package library.persistance.repository.jdbc

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.util

import library.model.{Book, BookLoan, User}
import library.persistance.repository.{IBookRepository, Log, RepositoryException}

class BookRepositoryJDBC extends IBookRepository
{
    def getAll: util.List[Book] =
    {
        Log.get.info("[Entering] ")
        val connection: Connection = DBConnection.get.getConnection
        var books: util.List[Book] = null
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement("SELECT * FROM books")
            val rs: ResultSet = stmt.executeQuery
            books = buildBooksFromResultSet(rs)
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        books
    }

    private def buildBooksFromResultSet(rs: ResultSet): util.List[Book] =
    {
        val books: util.List[Book] = new util.ArrayList[Book]
        try
        {
            while (rs.next)
            {
                {
                    val id: Int = rs.getInt("id")
                    val title: String = rs.getString("title")
                    val author: String = rs.getString("author")
                    val date_publish: String = rs.getString("date_publish")
                    val stock: Int = rs.getInt("stock")
                    val book: Book = new Book(id, title, author, date_publish, stock, getBorrowersBook(id))
                    books.add(book)
                }
            }
        }
        catch
        {
            case e: SQLException =>
                throw new RepositoryException("Error " + e)
        }
        books
    }

    def getAllAvailable: util.List[Book] =
    {
        Log.get.info("[Entering] ")
        val connection: Connection = DBConnection.get.getConnection
        var books: util.List[Book] = null
        val sql: String = "SELECT * FROM `books` WHERE id NOT IN (SELECT id FROM (SELECT B.id, B.stock FROM `books` B INNER JOIN `loans` L ON L.book_id = B.id GROUP BY L.book_id HAVING COUNT(*) >= B.stock) AS Hack);"
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement(sql)
            val rs: ResultSet = stmt.executeQuery
            books = buildBooksFromResultSet(rs)
            stmt.close()
        }
        catch
        {
            case e: SQLException =>
                throw new RepositoryException("Error " + e)
        }
        books
    }

    def getAllBorrowedBy(user: User): util.List[Book] =
    {
        getAllBorrowedBy(user.getId)
    }

    def getAllBorrowedBy(user_id: Int): util.List[Book] =
    {
        Log.get.info("[Entering] ")
        val connection: Connection = DBConnection.get.getConnection
        var books: util.List[Book] = null
        val sql: String = "SELECT B.id, B.title, B.author, B.date_publish, B.stock FROM `books` B INNER JOIN `loans` L ON L.book_id = B.id WHERE L.user_id = ?"
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement(sql)
            stmt.setInt(1, user_id)
            val rs: ResultSet = stmt.executeQuery
            books = buildBooksFromResultSet(rs)
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        books
    }

    def getAllLoans: util.List[BookLoan] =
    {
        Log.get.info("[Entering] ")
        val connection: Connection = DBConnection.get.getConnection
        val loans: util.List[BookLoan] = new util.ArrayList[BookLoan]
        val sql: String = "SELECT user_id, book_id, date_loan, title, author, date_publish, stock,  username FROM loans L INNER JOIN books B ON B.id = L.book_id INNER JOIN users U ON U.id = L.user_Id"
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement(sql)
            val rs: ResultSet = stmt.executeQuery
            while (rs.next)
            {
                {
                    val user_id: Int = rs.getInt("user_id")
                    val book_id: Int = rs.getInt("book_id")
                    val date_loan: String = rs.getString("date_loan")
                    val title: String = rs.getString("title")
                    val author: String = rs.getString("author")
                    val date_publish: String = rs.getString("date_publish")
                    val stock: Int = rs.getInt("stock")
                    val username: String = rs.getString("username")
                    val loan: BookLoan = new BookLoan(user_id, book_id, date_loan, title, author, date_publish, stock,
                        username)
                    loans.add(loan)
                }
            }
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        loans
    }

    def getLoan(book_id: Int, user_id: Int): BookLoan =
    {
        Log.get.info("[Entering] ")
        val connection: Connection = DBConnection.get.getConnection
        var loan: BookLoan = null
        val sql: String = "SELECT user_id, book_id, date_loan, title, author, date_publish, stock,  username FROM loans L INNER JOIN books B ON B.id = L.book_id INNER JOIN users U ON U.id = L.user_Id WHERE L.book_id = ? AND L.user_id = ?"
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement(sql)
            stmt.setInt(1, book_id)
            stmt.setInt(2, user_id)
            val rs: ResultSet = stmt.executeQuery
            if (rs.next)
            {
                val date_loan: String = rs.getString("date_loan")
                val title: String = rs.getString("title")
                val author: String = rs.getString("author")
                val date_publish: String = rs.getString("date_publish")
                val stock: Int = rs.getInt("stock")
                val username: String = rs.getString("username")
                loan = new BookLoan(user_id, book_id, date_loan, title, author, date_publish, stock, username)
            }
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        loan
    }

    def update(book: Book)
    {
        Log.get.info("[Entering] book=" + book)
        val connection: Connection = DBConnection.get.getConnection
        try
        {
            val stmt: PreparedStatement = connection
                .prepareStatement("UPDATE books SET title=?, author=?, date_publish=?, stock=? WHERE id = ?")
            stmt.setString(1, book.getTitle)
            stmt.setString(2, book.getAuthor)
            stmt.setString(3, book.getDatePublish)
            stmt.setInt(4, book.getStock)
            stmt.setInt(5, book.getId)
            stmt.executeUpdate
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
    }

    def borrowBook(book: Book, user: User)
    {
        borrowBook(book.getId, user.getId)
    }

    def borrowBook(book_id: Int, user_id: Int)
    {
        Log.get.info("[Entering] bookID=" + book_id + ", userID=" + user_id)
        val connection: Connection = DBConnection.get.getConnection
        try
        {
            val stmt: PreparedStatement = connection
                .prepareStatement("INSERT INTO loans(book_id, user_id) VALUES(?, ?)")
            stmt.setInt(1, book_id)
            stmt.setInt(2, user_id)
            stmt.executeUpdate
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
    }

    def returnBook(book: Book, user: User)
    {
        returnBook(book.getId, user.getId)
    }

    def returnBook(book_id: Int, user_id: Int)
    {
        Log.get.info("[Entering] bookID=" + book_id)
        val connection: Connection = DBConnection.get.getConnection
        try
        {
            val stmt: PreparedStatement = connection
                .prepareStatement("DELETE FROM loans WHERE book_id = ? AND user_id = ?")
            stmt.setInt(1, book_id)
            stmt.setInt(2, user_id)
            stmt.executeUpdate
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
    }

    def getBookByID(book_id: Int): Book =
    {
        val connection: Connection = DBConnection.get.getConnection
        var book: Book = null
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement("SELECT * FROM books WHERE id = ?")
            stmt.setInt(1, book_id)
            val rs: ResultSet = stmt.executeQuery
            if (rs.next)
            {
                val id: Int = rs.getInt("id")
                val title: String = rs.getString("title")
                val author: String = rs.getString("author")
                val date_publish: String = rs.getString("date_publish")
                val stock: Int = rs.getInt("stock")
                book = new Book(id, title, author, date_publish, stock, getBorrowersBook(id))
            }
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        book
    }

    private def getBorrowersBook(book_id: Int): util.Set[Integer] =
    {
        val connection: Connection = DBConnection.get.getConnection
        val users: util.Set[Integer] = new util.HashSet[Integer]
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement("SELECT user_id FROM loans WHERE book_id = ?")
            stmt.setInt(1, book_id)
            val rs: ResultSet = stmt.executeQuery
            while (rs.next)
            {
                {
                    users.add(rs.getInt("user_id"))
                }
            }
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        users
    }

    def searchByTitle(title: String): util.List[Book] =
    {
        Log.get.info("[Entering] title=" + title)
        val connection: Connection = DBConnection.get.getConnection
        var books: util.List[Book] = null
        try
        {
            val stmt: PreparedStatement = connection.prepareStatement("SELECT * FROM books WHERE title LIKE ?")
            stmt.setString(1, "%" + title + "%")
            val rs: ResultSet = stmt.executeQuery
            books = buildBooksFromResultSet(rs)
            stmt.close()
        }
        catch
        {
            case e: SQLException => throw new RepositoryException("Error " + e)
        }
        books
    }
}
