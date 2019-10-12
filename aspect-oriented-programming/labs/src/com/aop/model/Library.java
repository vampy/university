package com.aop.model;

import com.aop.aspect.AuthRequired;
import com.aop.aspect.Subject;
import com.aop.log.Log;
import com.aop.repository.IBookRepository;

import java.util.List;
import com.aop.aspect.SubjectChanged;

public class Library implements Subject
{
    private IBookRepository books;

    public Library(IBookRepository books)
    {
        this.books = books;
    }

    /**
     * Getter for property 'books'.
     *
     * @return Value for property 'books'.
     */
    public List<Book> getAllBooks()
    {
        return books.getAll();
    }
    public List<Book> getAllAvailable()
    {
        return books.getAllAvailable();
    }
    public List<Book> getAllLoanedBy(int userID)
    {
        return books.getAllLoanedBy(userID);
    }

    public List<Book> searchByTitle(String title)
    {
        return books.searchByTitle(title);
    }

    @SubjectChanged
    @AuthRequired
    public void loanBook(Book book, int userID)
    {
        book.setLoanedBy(userID);
        books.loanBook(book.getId(), userID);
    }

    @SubjectChanged
    @AuthRequired
    public void returnBook(Book book)
    {
        book.setLoanedBy(0);
        books.returnBook(book.getId());
    }
}
