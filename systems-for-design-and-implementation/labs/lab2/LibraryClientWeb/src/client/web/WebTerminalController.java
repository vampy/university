package client.web;

import library.model.Book;
import library.model.BookLoan;
import library.model.User;
import library.services.ILibraryClient;
import library.services.ILibraryServer;
import library.services.LibraryException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class WebTerminalController implements ILibraryClient
{
    private ILibraryServer server;
    private User           user;

    private List<Book>     availableBooks;
    private List<BookLoan> availableLoans;
    private List<Book>     borrowedBooks;

    public List<Book> getAvailableBooks()
    {
        return availableBooks;
    }

    public void setAvailableBooks(List<Book> availableBooks)
    {
        this.availableBooks = availableBooks;
    }

    public List<BookLoan> getAvailableLoans()
    {
        return availableLoans;
    }

    public void setAvailableLoans(List<BookLoan> availableLoans)
    {
        this.availableLoans = availableLoans;
    }

    public List<Book> getBorrowedBooks()
    {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks)
    {
        this.borrowedBooks = borrowedBooks;
    }

    public WebTerminalController(ILibraryServer server)
    {
        this.server = server;
    }

    public User getUser()
    {
        return user;
    }

    @Override
    public void updateBook(Book book) throws LibraryException
    {
        System.out.println("Book updated: " + book);
        if (!isLibrarian())
        {
        }
    }

    @Override
    public void updateLoan(BookLoan loan) throws LibraryException
    {
        System.out.println("Loan updated: " + loan);
        if (isLibrarian())
        {
        }
    }

    public boolean isLibrarian()
    {
        return user.isLibrarian();
    }

    public boolean login(String username, String password) throws LibraryException
    {
        User userL = new User(username, password);

        try
        {
            Method method = server.getClass().getMethod("login", User.class, ILibraryClient.class);

            if (method != null)
            {
                user = (User) method.invoke(server, userL, this);
            }
        }
        catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void logout()
    {
        try
        {
            server.logout(user, this);
        }
        catch (LibraryException e)
        {
            System.out.println("Logout error " + e);
        }
    }

    Book findAvailableBookById(int bookId)
    {
        for (Book book : availableBooks)
        {
            if (book.getId() == bookId)
                return book;
        }

        return null;
    }

    Book findBorrowedBookById(int bookId)
    {
        for (Book book : borrowedBooks)
        {
            if (book.getId() == bookId)
                return book;
        }

        return null;
    }

    public void borrowBook(int bookId) throws LibraryException
    {
        assert !user.isLibrarian();

        Book book = findAvailableBookById(bookId);
        if (!book.canBorrow(user))
        {
            throw new LibraryException("Book is already borrowed");
        }
        if (user.getBorrowed().size() >= 3)
        {
            throw new LibraryException("You can not borrow more than 3 books");
        }
        server.borrowBook(book, user);
        book.addBorrower(user.getId());
        user.addBorrowed(book.getId());

        // TODO this should just update the tables, sigh
        //reset();
    }

    public void returnBook(int bookid, int userId) throws LibraryException
    {
        //assert user.isLibrarian();

//        Book book = findBorrowedBookById(bookid);
        server.returnBook(bookid, userId);

        // TODO this should just update the tables, sigh
        //reset();
    }

    public List<Book> searchByTitle(String title) throws LibraryException
    {
        return server.searchByTitle(title);
    }

    public void resetAllTable() throws LibraryException
    {
        if (!user.isLibrarian())
        {
            server.getAvailableBooks();
        }
    }

    public void resetBorrowedTable() throws LibraryException
    {
        if (user.isLibrarian())
        {
            server.getAllLoans();
        }
        else
        {
            server.getBorrowedBy(user);
        }
    }

    public void reset() throws LibraryException
    {
        resetAllTable();
        resetBorrowedTable();
    }
}
