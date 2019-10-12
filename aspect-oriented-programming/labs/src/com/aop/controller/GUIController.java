package com.aop.controller;

import com.aop.log.Log;
import com.aop.model.Book;
import com.aop.model.Library;

import java.util.Observable;
import java.util.Observer;

public class GUIController extends AbstractController
{
    private Library library;
    private BooksMainTableModel tableModel;

    public GUIController(Library library)
    {
        this.library = library;
        tableModel = new BooksMainTableModel(library.getAllBooks());
    }

    public Library getLibrary()
    {
        return library;
    }

    /**
     * Getter for property 'tableModel'.
     *
     * @return Value for property 'tableModel'.
     */
    public BooksMainTableModel getTableModel()
    {
        return tableModel;
    }

    public void loanBook(Book book, int userID)
    {
        library.loanBook(book, userID);
    }

    public void returnBook(Book book)
    {
        library.returnBook(book);
    }

    public void setTableBooksFromLibrary()
    {
        tableModel.setBooks(library.getAllBooks());
    }

//    @Override
//    public void update(Object arg)
//    {
//        setTableBooksFromLibrary();
//    }

    @Override
    public void close()
    {

    }
}
