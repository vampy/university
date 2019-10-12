package library.model;

import java.io.Serializable;

public class BookLoan implements Serializable
{
    private int    userID;
    private int    bookID;
    private String dateLoan;
    private String title;
    private String author;
    private String datePublish;
    private int    stock;
    private String username;

    public BookLoan(int userID, int bookID, String dateLoan, String title, String author, String datePublish, int stock, String username)
    {
        this.userID = userID;
        this.bookID = bookID;
        this.dateLoan = dateLoan;
        this.title = title;
        this.author = author;
        this.datePublish = datePublish;
        this.stock = stock;
        this.username = username;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public int getBookID()
    {
        return bookID;
    }

    public void setBookID(int bookID)
    {
        this.bookID = bookID;
    }

    public String getDateLoan()
    {
        return dateLoan;
    }

    public void setDateLoan(String dateLoan)
    {
        this.dateLoan = dateLoan;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getDatePublish()
    {
        return datePublish;
    }

    public void setDatePublish(String datePublish)
    {
        this.datePublish = datePublish;
    }

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    public String toString()
    {
        return "BookLoan{" +
            "userID=" + userID +
            ", bookID=" + bookID +
            ", dateLoan='" + dateLoan + '\'' +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", datePublish='" + datePublish + '\'' +
            ", stock=" + stock +
            ", username='" + username + '\'' +
            '}';
    }
}
