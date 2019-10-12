package library.network.dto;

import library.model.Book;
import library.model.User;

import java.io.Serializable;

public class BorrowDTO implements Serializable
{
    private Book book;
    private User user;

    public BorrowDTO(Book book, User user)
    {
        this.book = book;
        this.user = user;
    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "BorrowDTO{" +
            "book=" + book +
            ", user=" + user +
            '}';
    }
}
