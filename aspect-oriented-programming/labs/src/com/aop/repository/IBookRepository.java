package com.aop.repository;

import com.aop.model.Book;

import java.util.List;

public interface IBookRepository
{
    List<Book> getAll();
    List<Book> getAllAvailable();
    List<Book> getAllLoanedBy(int userID);
    List<Book> searchByTitle(String title);

    void loanBook(int bookID, int userID);
    void returnBook(int bookID);
    void update(Book book);
}
