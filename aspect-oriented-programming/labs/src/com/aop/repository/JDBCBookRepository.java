package com.aop.repository;

import com.aop.model.Book;
import com.aop.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCBookRepository extends JdbcDaoSupport implements IBookRepository
{
    @Override
    public List<Book> getAll()
    {
        List<Book> books = getJdbcTemplate().query("SELECT * FROM books", new BookMapper());
        for (Book book: books)
        {
            if (book.isLoaned())
            {
                User user = getJdbcTemplate().queryForObject(
                    "SELECT * FROM users WHERE id=?",
                    new UserMapper(),
                    book.getLoanedBy()
                );
                book.setUser(user);
            }
        }

        return books;
    }

    @Override
    public List<Book> getAllAvailable()
    {
        return getJdbcTemplate().query("SELECT * FROM books WHERE loaned_by = NULL", new BookMapper());
    }

    @Override
//    @Cacheable(value="loanedBy", key="#id")
    public List<Book> getAllLoanedBy(int userID)
    {
        return getJdbcTemplate().query("SELECT * FROM books WHERE loaned_by = ?", new BookMapper(), userID);
    }

    @Override
//    @Cacheable(key="#title")
    public List<Book> searchByTitle(String title)
    {
        return getJdbcTemplate().query(
            "SELECT * FROM books WHERE title LIKE ?",
            new BookMapper(),
            "%" + title + "%"
        );
    }

    @Override
    public void loanBook(int bookID, int userID)
    {
        getJdbcTemplate().update("UPDATE books SET loaned_by=? WHERE id = ?", userID, bookID);
    }

    @Override
    public void returnBook(int bookID)
    {
        getJdbcTemplate().update("UPDATE books SET loaned_by=NULL WHERE id = ?", bookID);
    }

    @Override
    public void update(Book book)
    {
        getJdbcTemplate().update(
            "UPDATE books SET title=?, author=?, date_publish=?, loaned_by=? WHERE id = ?",
            book.getTitle(),
            book.getAuthor(),
            book.getPublishDate(),
            book.getLoanedBy(),
            book.getId()
        );
    }

    private static class BookMapper implements RowMapper<Book>
    {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("date_publish"),
                rs.getInt("loaned_by")
            );
        }
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
