package com.aop.model;

public class Book
{
    private int id;
    private String title;
    private String author;
    private String publishDate;
    private int loaned_by = 0;
    private User user = null;

    public Book(int id, String title, String author, String publishDate, int loaned_by)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.loaned_by = loaned_by;
    }

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Getter for property 'title'.
     *
     * @return Value for property 'title'.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Getter for property 'author'.
     *
     * @return Value for property 'author'.
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Getter for property 'publishDate'.
     *
     * @return Value for property 'publishDate'.
     */
    public String getPublishDate()
    {
        return publishDate;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * Getter for property 'loaned'.
     *
     * @return Value for property 'loaned'.
     */
    public int getLoanedBy()
    {
        return loaned_by;
    }

    public boolean isLoaned() { return loaned_by != 0;}


    /**
     * Setter for property 'loaned'.
     *
     * @param loaned Value to set for property 'loaned'.
     */
    public void setLoanedBy(int loaned)
    {
        this.loaned_by = loaned;
    }

    @Override
    public String toString()
    {
        return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", publishDate='" + publishDate + '\'' +
            ", loaned_by=" + loaned_by +
            '}';
    }
}
