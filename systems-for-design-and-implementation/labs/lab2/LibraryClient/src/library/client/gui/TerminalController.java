package library.client.gui;

import library.model.Book;
import library.model.BookLoan;
import library.model.User;
import library.services.ILibraryClient;
import library.services.ILibraryServer;
import library.services.LibraryException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TerminalController implements ILibraryClient
{
    private BooksTerminalAllAvailable  tableAllModel;
    private BooksTerminalUser          tableUserModel;
    private BooksTerminalUserLibrarian tableLibrarianModel;
    private ILibraryServer             server;
    private User                       user;

    public TerminalController(ILibraryServer server)
    {
        this.server = server;
        tableAllModel = new BooksTerminalAllAvailable();
        tableUserModel = new BooksTerminalUser();
        tableLibrarianModel = new BooksTerminalUserLibrarian();
    }

    public User getUser()
    {
        return user;
    }

    /**
     * Getter for property 'tableAllModel'.
     *
     * @return Value for property 'tableAllModel'.
     */
    public BooksTerminalAllAvailable getTableAllModel()
    {
        return tableAllModel;
    }

    public BooksTerminalUser getTableUserModel()
    {
        return tableUserModel;
    }

    public BooksTerminalUserLibrarian getTableLibrarianModel()
    {
        return tableLibrarianModel;
    }

    @Override
    public void updateBook(Book book) throws LibraryException
    {
        System.out.println("Book updated: " + book);
        if (!isLibrarian())
        {
            tableAllModel.updateBook(book);
            tableUserModel.updateBook(user, book);
        }
    }

    @Override
    public void updateLoan(BookLoan loan) throws LibraryException
    {
        System.out.println("Loan updated: " + loan);
        if (isLibrarian())
        {
            tableLibrarianModel.updateBookLoan(loan);
        }
    }

    public boolean isLibrarian()
    {
        return user.isLibrarian();
    }

    public void login(String username, String password) throws LibraryException
    {
        User userL = new User(username, password);

        try
        {
            Method method = server.getClass().getMethod("login", User.class, ILibraryClient.class);

            if (method != null)
            {
                user = (User) method.invoke(server, userL, this);
                reset();
            }
        }
        catch (SecurityException|NoSuchMethodException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
        {
            e.printStackTrace();
        }

//        user = server.login(userL, this);
//
//        reset();
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

    public List<Book> searchByTitle(String title) throws LibraryException
    {
        return server.searchByTitle(title);
    }

    public void borrowBook(int selected_row) throws LibraryException
    {
        assert !user.isLibrarian();

        Book book = getTableAllModel().get(selected_row);
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
        reset();
    }

    public void returnBook(int selected_row) throws LibraryException
    {
        assert user.isLibrarian();

        BookLoan book = getTableLibrarianModel().get(selected_row);
        server.returnBook(book.getBookID(), book.getUserID());

        // TODO this should just update the tables, sigh
        reset();
    }

    public void search(String title) throws LibraryException
    {
        tableAllModel.setBooks(searchByTitle(title));
    }

    public void resetAllTable() throws LibraryException
    {
        if (!user.isLibrarian())
        {
            tableAllModel.setBooks(server.getAvailableBooks());
        }
    }

    public void resetBorrowedTable() throws LibraryException
    {
        if (user.isLibrarian())
        {
            tableLibrarianModel.setBooks(server.getAllLoans());
        }
        else
        {
            tableUserModel.setBooks(server.getBorrowedBy(user));
        }
    }

    public void reset() throws LibraryException
    {
        resetAllTable();
        resetBorrowedTable();
    }
}
