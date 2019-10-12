package library.persistance.repository.jdbc;

import library.model.Book;
import library.model.BookLoan;
import library.model.User;
import library.persistance.repository.IBookRepository;
import library.persistance.repository.Log;
import library.persistance.repository.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BookRepositoryJDBC implements IBookRepository
{
    private Set<Integer> getBorrowersBook(int book_id)
    {
        Connection connection = DBConnection.get().getConnection();
        Set<Integer> users = new HashSet<Integer>();

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT user_id FROM loans WHERE book_id = ?");
            stmt.setInt(1, book_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                users.add(rs.getInt("user_id"));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return users;
    }

    private List<Book> buildBooksFromResultSet(ResultSet rs)
    {
        List<Book> books = new ArrayList<Book>();
        try
        {
            while (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String date_publish = rs.getString("date_publish");
                int stock = rs.getInt("stock");

                Book book = new Book(id, title, author, date_publish, stock, getBorrowersBook(id));
                books.add(book);
            }
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }

    /**
     * Getter for property 'books'.
     *
     * @return Value for property 'books'.
     */
    public List<Book> getAll()
    {
        Log.get().info("[Entering] ");

        Connection connection = DBConnection.get().getConnection();
        List<Book> books;
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books");
            ResultSet rs = stmt.executeQuery();
            books = buildBooksFromResultSet(rs);
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }

    @Override
    public List<Book> getAllAvailable()
    {
        Log.get().info("[Entering] ");

        Connection connection = DBConnection.get().getConnection();
        List<Book> books;
        // TODO, I am sure there is a more elegant way, but deadlines man!!!
        String sql = "SELECT * FROM `books` WHERE id NOT IN (SELECT id FROM (SELECT B.id, B.stock FROM `books` B INNER JOIN `loans` L ON L.book_id = B.id GROUP BY L.book_id HAVING COUNT(*) >= B.stock) AS Hack);";
        try
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            books = buildBooksFromResultSet(rs);
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }

    @Override
    public List<Book> getAllBorrowedBy(int user_id)
    {
        Log.get().info("[Entering] ");

        Connection connection = DBConnection.get().getConnection();
        List<Book> books;
        String sql = "SELECT B.id, B.title, B.author, B.date_publish, B.stock FROM `books` B INNER JOIN `loans` L ON L.book_id = B.id WHERE L.user_id = ?";
        try
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            books = buildBooksFromResultSet(rs);
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }

    @Override
    public List<Book> getAllBorrowedBy(User user)
    {
        return getAllBorrowedBy(user.getId());
    }

    @Override
    public List<BookLoan> getAllLoans()
    {
        Log.get().info("[Entering] ");

        Connection connection = DBConnection.get().getConnection();
        List<BookLoan> loans = new ArrayList<BookLoan>();
        String sql = "SELECT user_id, book_id, date_loan, title, author, date_publish, stock,  username FROM loans L INNER JOIN books B ON B.id = L.book_id INNER JOIN users U ON U.id = L.user_Id";
        try
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                int user_id = rs.getInt("user_id");
                int book_id = rs.getInt("book_id");
                String date_loan = rs.getString("date_loan");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String date_publish = rs.getString("date_publish");
                int stock = rs.getInt("stock");
                String username = rs.getString("username");

                BookLoan loan = new BookLoan(user_id, book_id, date_loan, title, author, date_publish, stock, username);
                loans.add(loan);
            }

            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return loans;
    }

    @Override
    public BookLoan getLoan(int book_id, int user_id)
    {
        Log.get().info("[Entering] ");

        Connection connection = DBConnection.get().getConnection();
        BookLoan loan = null;
        String sql = "SELECT user_id, book_id, date_loan, title, author, date_publish, stock,  username FROM loans L INNER JOIN books B ON B.id = L.book_id INNER JOIN users U ON U.id = L.user_Id WHERE L.book_id = ? AND L.user_id = ?";
        try
        {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, book_id);
            stmt.setInt(2, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String date_loan = rs.getString("date_loan");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String date_publish = rs.getString("date_publish");
                int stock = rs.getInt("stock");
                String username = rs.getString("username");

                loan = new BookLoan(user_id, book_id, date_loan, title, author, date_publish, stock, username);
            }

            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return loan;
    }

    @Override
    public void update(Book book)
    {
        Log.get().info("[Entering] book=" + book);

        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("UPDATE books SET title=?, author=?, date_publish=?, stock=? WHERE id = ?");
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getDatePublish());
            stmt.setInt(4, book.getStock());
            stmt.setInt(5, book.getId());
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }
    }

    @Override
    public void borrowBook(int book_id, int user_id)
    {
        Log.get().info("[Entering] bookID=" + book_id + ", userID=" + user_id);

        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("INSERT INTO loans(book_id, user_id) VALUES(?, ?)");
            stmt.setInt(1, book_id);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }
    }

    @Override
    public void borrowBook(Book book, User user)
    {
        borrowBook(book.getId(), user.getId());
    }

    @Override
    public void returnBook(int book_id, int user_id)
    {
        Log.get().info("[Entering] bookID=" + book_id);

        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("DELETE FROM loans WHERE book_id = ? AND user_id = ?");
            stmt.setInt(1, book_id);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }
    }

    @Override
    public void returnBook(Book book, User user)
    {
        returnBook(book.getId(), user.getId());
    }

    @Override
    public Book getBookByID(int book_id)
    {
        Connection connection = DBConnection.get().getConnection();
        Book book = null;

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE id = ?");
            stmt.setInt(1, book_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String date_publish = rs.getString("date_publish");
                int stock = rs.getInt("stock");

                book = new Book(id, title, author, date_publish, stock, getBorrowersBook(id));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return book;
    }

    @Override
    public List<Book> searchByTitle(String title)
    {
        Log.get().info("[Entering] title=" + title);

        Connection connection = DBConnection.get().getConnection();
        List<Book> books;
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("SELECT * FROM books WHERE title LIKE ?");
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            books = buildBooksFromResultSet(rs);
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }
}
