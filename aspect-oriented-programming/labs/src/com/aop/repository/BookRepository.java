package com.aop.repository;

import com.aop.controller.DBConnection;
import com.aop.model.Book;
import com.aop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements IBookRepository
{
    /**
     * Getter for property 'books'.
     *
     * @return Value for property 'books'.
     */
    public List<Book> getAll()
    {
        Connection connection = DBConnection.get().getConnection();
        List<Book> books = new ArrayList<Book>();
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books");
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                Book book =
                    new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date_publish"), rs.getInt("loaned_by"));
                books.add(book);

                if (book.isLoaned()) // add user
                {
                    PreparedStatement stmt_user =
                        connection.prepareStatement("SELECT * FROM users WHERE id=?");
                    stmt_user.setInt(1, book.getLoanedBy());

                    ResultSet rs_user = stmt_user.executeQuery();
                    if (!rs_user.next())
                    {
                        throw new SQLException("User id does not exist");
                    }
                    book.setUser(new User(rs_user.getInt("id"), rs_user.getString("username")));

                    stmt_user.close();
                }
            }
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
        Connection connection = DBConnection.get().getConnection();
        List<Book> books = new ArrayList<Book>();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("SELECT * FROM books WHERE loaned_by = NULL");
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date_publish"), rs.getInt("loaned_by")));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }

    @Override
    public List<Book> getAllLoanedBy(int userID)
    {
        Connection connection = DBConnection.get().getConnection();
        List<Book> books = new ArrayList<Book>();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("SELECT * FROM books WHERE loaned_by = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date_publish"), rs.getInt("loaned_by")));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }

    @Override
    public void update(Book book)
    {
        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("UPDATE books SET title=?, author=?, date_publish=?, loaned_by=? WHERE id = ?");
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublishDate());
            stmt.setInt(4, book.getLoanedBy());
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
    public void loanBook(int bookID, int userID)
    {
        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("UPDATE books SET loaned_by=? WHERE id = ?");
            stmt.setInt(1, userID);
            stmt.setInt(2, bookID);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }
    }

    @Override
    public void returnBook(int bookID)
    {
        Connection connection = DBConnection.get().getConnection();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("UPDATE books SET loaned_by=NULL WHERE id = ?");
            stmt.setInt(1, bookID);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }
    }

    @Override
    public List<Book> searchByTitle(String title)
    {
        Connection connection = DBConnection.get().getConnection();
        List<Book> books = new ArrayList<Book>();
        try
        {
            PreparedStatement stmt =
                connection.prepareStatement("SELECT * FROM books WHERE title LIKE ?");
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date_publish"), rs.getInt("loaned_by")));
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            throw new RepositoryException("Error " + e);
        }

        return books;
    }
}
