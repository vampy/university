package library.services.impl

import java.util
import java.util.concurrent.{ConcurrentHashMap, ExecutorService, Executors}

import library.model.{Book, BookLoan, User}
import library.persistance.repository.{IBookRepository, IUserRepository, RepositoryException}
import library.services.{ILibraryClient, ILibraryServer, LibraryException}

class LibraryServerImpl(var userRepository: IUserRepository, var bookRepository: IBookRepository) extends ILibraryServer
{
    private var librarianID: Int = -1
    private var loggedClients: util.Map[Integer, ILibraryClient] = null

    this.loggedClients = new ConcurrentHashMap[Integer, ILibraryClient]

    @throws[LibraryException]
    def login(user: User, client: ILibraryClient): User =
    {
        val isValid: Boolean = this.userRepository.verifyUser(user)
        if (!isValid)
        {
            throw new LibraryException("Authentication failed")
        }

        val new_user: User = userRepository.getUserByUsername(user.getUsername)
        if (isLogged(new_user))
        {
            throw new LibraryException("User already logged in")
        }
        if (new_user.isLibrarian)
        {
            if (librarianID == -1)
            {
                librarianID = new_user.getId
            }
            else
            {
                throw new LibraryException("Another librarian is already logged in")
            }
        }
        loggedClients.put(new_user.getId, client)
        new_user
    }

    private def isLogged(user: User): Boolean =
    {
        this.loggedClients.get(user.getId) != null
    }

    @throws[LibraryException]
    def logout(user: User, client: ILibraryClient)
    {
        val localClient: ILibraryClient = loggedClients.remove(user.getId)
        if (localClient == null)
        {
            throw new LibraryException("User " + user.getId + " is not logged in.")
        }
        if (user.isLibrarian)
        {
            librarianID = -1
        }
    }

    @throws[LibraryException]
    def getAvailableBooks: util.List[Book] =
    {
        try
        {
            bookRepository.getAllAvailable
        }
        catch
        {
            case e: RepositoryException => throw new LibraryException(e.getMessage)
        }
    }

    @throws[LibraryException]
    def getAllLoans: util.List[BookLoan] =
    {
        try
        {
            bookRepository.getAllLoans
        }
        catch
        {
            case e: RepositoryException =>
                throw new LibraryException(e.getMessage)
        }
    }

    @throws[LibraryException]
    def getBorrowedBy(user: User): util.List[Book] =
    {
        try
        {
            bookRepository.getAllBorrowedBy(user)
        }
        catch
        {
            case e: RepositoryException =>
                throw new LibraryException(e.getMessage)
        }
    }

    @throws[LibraryException]
    def searchByTitle(title: String): util.List[Book] =
    {
        try
        {
            bookRepository.searchByTitle(title)
        }
        catch
        {
            case e: RepositoryException =>
                throw new LibraryException(e.getMessage)
        }
    }

    @throws[LibraryException]
    def borrowBook(book: Book, user: User)
    {
        try
        {
            bookRepository.borrowBook(book, user)
            notifyUsersUpdateBook(book)
            notifyLibrariansBorrowBook(book.getId, user.getId)
        }
        catch
        {
            case e: RepositoryException =>
                throw new LibraryException(e.getMessage)
        }
    }

    @throws[LibraryException]
    private def notifyLibrariansBorrowBook(book_id: Int, user_id: Int)
    {
        println("notifyLibrariansBorrowBook that loan updated: " + book_id + " | " + user_id)
        val loan: BookLoan = bookRepository.getLoan(book_id, user_id)
        if (librarianID == -1)
        {
            println("No librarians logged in")
            return
        }
        val executor: ExecutorService = Executors.newFixedThreadPool(1)
        val client: ILibraryClient = loggedClients.get(librarianID)
        assert(client != null)
        executor.execute(new Runnable()
        {
            def run()
            {
                try
                {
                    println("Notifying librarian [" + librarianID + "] that loan updated.")
                    client.updateLoan(loan)
                }
                catch
                {
                    case e: LibraryException =>
                        println("Error notifying user of book loan " + e)
                }
            }
        })
    }

    @throws[LibraryException]
    def returnBook(book: Int, user: Int)
    {
        try
        {
            bookRepository.returnBook(book, user)
            notifyUsersUpdateBook(this.bookRepository.getBookByID(book))
        }
        catch
        {
            case e: RepositoryException =>
                throw new LibraryException(e.getMessage)
        }
    }

    @throws[LibraryException]
    private def notifyUsersUpdateBook(old_book: Book)
    {
        println("notifyUsersUpdateBook that book changed: " + old_book)
        val new_book: Book = bookRepository.getBookByID(old_book.getId)
        val executor: ExecutorService = Executors.newFixedThreadPool(loggedClients.size)
        import scala.collection.JavaConversions._
        for (pair <- loggedClients.entrySet)
        {
            val client: ILibraryClient = pair.getValue
            val user_id: Int = pair.getKey
            if (user_id != -1 && user_id != librarianID)
            {
                executor.execute(new Runnable()
                {
                    def run()
                    {
                        try
                        {
                            println("Notifying [" + user_id + "] that book updated.")
                            client.updateBook(new_book)
                        }
                        catch
                        {
                            case e: LibraryException =>
                                println("Error notifying user of book update " + e)
                        }
                    }
                })
            }
        }
    }
}
