package library.services.impl;

import library.model.Book;
import library.model.BookLoan;
import library.model.User;
import library.persistance.repository.IBookRepository;
import library.persistance.repository.IUserRepository;
import library.persistance.repository.RepositoryException;
import library.services.ILibraryClient;
import library.services.ILibraryServer;
import library.services.LibraryException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LibraryServerImpl implements ILibraryServer
{
    private IUserRepository userRepository;
    private IBookRepository bookRepository;

    // unique <>_<>
    private int librarianID = -1;
    private Map<Integer, ILibraryClient> loggedClients;

    public LibraryServerImpl(IUserRepository userRepository, IBookRepository bookRepository)
    {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User login(User user, ILibraryClient client) throws LibraryException
    {
        boolean isValid = userRepository.verifyUser(user);
        if (!isValid)
        {
            throw new LibraryException("Authentication failed");
        }

        // we get the full user
        User new_user = userRepository.getUserByUsername(user.getUsername());
        if (isLogged(new_user))
        {
            throw new LibraryException("User already logged in");
        }

        // Only one librarian
        if (new_user.isLibrarian())
        {
            if (librarianID == -1)
            {
                librarianID = new_user.getId();
            }
            else
            {
                throw new LibraryException("Another librarian is already logged in");
            }
        }

        loggedClients.put(new_user.getId(), client);

        return new_user;
    }

    private boolean isLogged(User user)
    {
        return loggedClients.get(user.getId()) != null;
    }

    @Override
    public synchronized void logout(User user, ILibraryClient client) throws LibraryException
    {
        ILibraryClient localClient = loggedClients.remove(user.getId());
        if (localClient == null)
        {
            throw new LibraryException("User " + user.getId() + " is not logged in.");
        }

        // reset librarian
        if (user.isLibrarian())
        {
            librarianID = -1;
        }
    }

    @Override
    public synchronized List<Book> getAvailableBooks() throws LibraryException
    {
        try
        {
            return bookRepository.getAllAvailable();
        }
        catch (RepositoryException e)
        {
            throw new LibraryException(e.getMessage());
        }
    }

    @Override
    public synchronized List<BookLoan> getAllLoans() throws LibraryException
    {
        try
        {
            return bookRepository.getAllLoans();
        }
        catch (RepositoryException e)
        {
            throw new LibraryException(e.getMessage());
        }
    }

    @Override
    public synchronized List<Book> getBorrowedBy(User user) throws LibraryException
    {
        try
        {
            return bookRepository.getAllBorrowedBy(user);
        }
        catch (RepositoryException e)
        {
            throw new LibraryException(e.getMessage());
        }
    }

    @Override
    public synchronized List<Book> searchByTitle(String title) throws LibraryException
    {
        try
        {
            return bookRepository.searchByTitle(title);
        }
        catch (RepositoryException e)
        {
            throw new LibraryException(e.getMessage());
        }
    }

    @Override
    public synchronized void borrowBook(Book book, User user) throws LibraryException
    {
        try
        {
            bookRepository.borrowBook(book, user);
            notifyUsersUpdateBook(book);
            notifyLibrariansBorrowBook(book.getId(), user.getId());
        }
        catch (RepositoryException e)
        {
            throw new LibraryException(e.getMessage());
        }
    }

    @Override
    public synchronized void returnBook(int book, int user) throws LibraryException
    {
        try
        {
            bookRepository.returnBook(book, user);
            notifyUsersUpdateBook(this.bookRepository.getBookByID(book));
        }
        catch (RepositoryException e)
        {
            throw new LibraryException(e.getMessage());
        }
    }

    private void notifyLibrariansBorrowBook(int book_id, int user_id) throws LibraryException
    {
        System.out.println("notifyLibrariansBorrowBook that loan updated: " + book_id + " | " + user_id);
        final BookLoan loan = bookRepository.getLoan(book_id, user_id);

        if (librarianID == -1)
        {
            System.out.println("No librarians logged in");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(1);
        final ILibraryClient client = loggedClients.get(librarianID);
        assert client != null;

        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Notifying librarian [" + librarianID + "] that loan updated.");
                    client.updateLoan(loan);
                }
                catch (LibraryException e)
                {
                    System.out.println("Error notifying user of book loan " + e);
                }
            }
        });
    }

    private void notifyUsersUpdateBook(Book old_book) throws LibraryException
    {
        System.out.println("notifyUsersUpdateBook that book changed: " + old_book);
        final Book new_book = bookRepository.getBookByID(old_book.getId());

        ExecutorService executor = Executors.newFixedThreadPool(loggedClients.size());
        for (Map.Entry<Integer, ILibraryClient> pair : loggedClients.entrySet())
        {
            final ILibraryClient client = pair.getValue();
            final int user_id = pair.getKey();

            // only users
            if (user_id != -1 && user_id != librarianID)
            {
                executor.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            System.out.println("Notifying [" + user_id + "] that book updated.");
                            client.updateBook(new_book);
                        }
                        catch (LibraryException e)
                        {
                            System.out.println("Error notifying user of book update " + e);
                        }
                    }
                });
            }
        }
    }
}
