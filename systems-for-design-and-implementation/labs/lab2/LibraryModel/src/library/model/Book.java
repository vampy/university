package library.model;

import java.io.Serializable;
import java.util.Set;

public class Book implements Serializable
{
    private int    id;
    private String title;
    private String author;
    private String datePublish;
    private int    stock;

    // Id of all users who borrowed this book
    private Set<Integer> borrowers;

    public Book(int id, String title, String author, String datePublish, int stock, Set<Integer> borrowers)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.datePublish = datePublish;
        this.stock = stock;
        this.borrowers = borrowers;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public Set<Integer> getBorrowers()
    {
        return borrowers;
    }

    public void setBorrowers(Set<Integer> borrowers)
    {
        this.borrowers = borrowers;
    }

    public void addBorrower(int user_id)
    {
        assert canBorrow();
        borrowers.add(user_id);
    }

    public void removeBorrower(int user_id)
    {
        borrowers.remove(user_id);
    }

    /**
     * If can borrow one more book of this kind;
     */
    public boolean canBorrow()
    {
        return stock >= borrowers.size() + 1;
    }

    // Only if user did not already borrow
    public boolean canBorrow(int user_id)
    {
        return !borrowers.contains(user_id);
    }

    public boolean canBorrow(User user)
    {
        return canBorrow(user.getId());
    }

    public int getStockAvailable()
    {
        return stock - borrowers.size();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Book)
        {
            Book book = (Book) obj;
            return id == book.id;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", datePublish='" + datePublish + '\'' +
            ", stock=" + stock +
            ", borrowers=" + borrowers +
            '}';
    }
}
