package library.services;

import library.model.Book;
import library.model.BookLoan;
import library.model.User;

import java.util.List;

public interface ILibraryServer
{
    User login(User user, ILibraryClient client) throws LibraryException;

    void logout(User user, ILibraryClient client) throws LibraryException;

    List<Book> getAvailableBooks() throws LibraryException;

    List<Book> getBorrowedBy(User user) throws LibraryException;

    List<Book> searchByTitle(String title) throws LibraryException;

    List<BookLoan> getAllLoans() throws LibraryException;

    void borrowBook(Book book, User user) throws LibraryException;

    void returnBook(int book_id, int user_id) throws LibraryException;
}
