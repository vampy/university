package library.services;

import library.model.Book;
import library.model.BookLoan;

public interface ILibraryClient
{
    void updateBook(Book book) throws LibraryException;

    // used by librarian
    void updateLoan(BookLoan loan) throws LibraryException;
}
