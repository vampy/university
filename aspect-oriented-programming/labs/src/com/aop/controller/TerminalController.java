package com.aop.controller;

import com.aop.log.Log;
import com.aop.model.Book;
import com.aop.model.Library;
import com.aop.model.User;


public class TerminalController extends AbstractController
{
    private Library                     library;
    private BooksTerminalTableModel     tableAllModel;
    private BooksTerminalUserTableModel tableUserModel;
    private User                        user;

    public TerminalController(Library library)
    {
        this.library = library;
        tableAllModel = new BooksTerminalTableModel(library.getAllBooks());
        tableUserModel = new BooksTerminalUserTableModel();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
        tableUserModel.setBooks(library.getAllLoanedBy(user.getId()));
    }

    /**
     * Getter for property 'tableAllModel'.
     *
     * @return Value for property 'tableAllModel'.
     */
    public BooksTerminalTableModel getTableAllModel()
    {
        return tableAllModel;
    }

    public BooksTerminalUserTableModel getTableUserModel()
    {
        return tableUserModel;
    }

    public void loanBook(Book book)
    {
        library.loanBook(book, user.getId());
    }

    public void returnBook(Book book)
    {
        library.returnBook(book);
    }

    public int getCountLoaned()
    {
        return library.getAllLoanedBy(user.getId()).size();
    }

    public void search(String title)
    {
        tableAllModel.setBooks(library.searchByTitle(title));
    }

    public void reset()
    {
        tableAllModel.setBooks(library.getAllBooks());
        tableUserModel.setBooks(library.getAllLoanedBy(user.getId()));
    }

//    @Override
//    public void update(Object arg)
//    {
//        System.out.println("Inside ObserverAspect: TerminalController.update");
//        reset();
//    }

    @Override
    public void close()
    {

    }
}
