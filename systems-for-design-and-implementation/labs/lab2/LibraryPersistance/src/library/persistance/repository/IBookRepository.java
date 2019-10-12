package library.persistance.repository;


import library.model.Book;
import library.model.BookLoan;
import library.model.User;

import java.util.List;

public interface IBookRepository
{
    List<Book> getAll();

    List<Book> getAllAvailable();

    List<Book> getAllBorrowedBy(User user);

    List<Book> getAllBorrowedBy(int user_id);

    List<Book> searchByTitle(String title);

    Book getBookByID(int book_id);

    void borrowBook(Book book, User user);

    void borrowBook(int book_id, int user_id);

    void returnBook(Book book, User user);

    void returnBook(int book_id, int user_id);

    void update(Book book);

    List<BookLoan> getAllLoans();

    BookLoan getLoan(int book_id, int user_id);
}

