package chat.client.gui

import java.lang.reflect
import java.util

import library.model.{Book, BookLoan, User}
import library.services.{ILibraryClient, ILibraryServer, LibraryException}


class TerminalController(var server: ILibraryServer) extends ILibraryClient
{
    private var tableAllModel: BooksTerminalAllAvailable = null
    private var tableUserModel: BooksTerminalUser = null
    private var tableLibrarianModel: BooksTerminalUserLibrarian = null
    private var user: User = null

    tableAllModel = new BooksTerminalAllAvailable
    tableUserModel = new BooksTerminalUser
    tableLibrarianModel = new BooksTerminalUserLibrarian

    def getUser: User =
    {
        user
    }

    def getTableUserModel: BooksTerminalUser =
    {
        tableUserModel
    }

    @throws[LibraryException]
    def updateBook(book: Book)
    {
        println("Book updated: " + book)
        if (!isLibrarian)
        {
            tableAllModel.updateBook(book)
            tableUserModel.updateBook(user, book)
        }
    }

    def isLibrarian: Boolean =
    {
        user.isLibrarian
    }

    @throws[LibraryException]
    def updateLoan(loan: BookLoan)
    {
        println("Loan updated: " + loan)
        if (isLibrarian)
        {
            tableLibrarianModel.updateBookLoan(loan)
        }
    }

    @throws[LibraryException]
    def login(username: String, password: String)
    {
        // TODO do scala reflection
        val userL: User = new User(username, password)
        var method: reflect.Method = null
        try
        {
            method = server.getClass.getMethod("login", classOf[User], classOf[ILibraryClient])
        }
        catch
        {
            case e: SecurityException => e.printStackTrace()
            case e: NoSuchMethodException => e.printStackTrace()
        }
        println(method)
        try
        {
            if (method != null)
            {
                user = method.invoke(server, userL, this).asInstanceOf[User]
                reset()
            }
        }
        catch
        {
            case e: IllegalArgumentException => e.printStackTrace()
            case e: IllegalAccessException => e.printStackTrace()
        }
        //        user = server.login(userL, this)
        //        reset()
    }

    def logout()
    {
        try
        {
            server.logout(user, this)
        }
        catch
        {
            case e: LibraryException =>
                println("Logout error " + e)
        }
    }

    @throws[LibraryException]
    def borrowBook(selected_row: Int)
    {
        assert(!user.isLibrarian)
        val book: Book = getTableAllModel.get(selected_row)
        if (!book.canBorrow(user))
        {
            throw new LibraryException("Book is already borrowed")
        }
        if (user.getBorrowed.size >= 3)
        {
            throw new LibraryException("You can not borrow more than 3 books")
        }
        server.borrowBook(book, user)
        book.addBorrower(user.getId)
        user.addBorrowed(book.getId)
        reset()
    }

    def getTableAllModel: BooksTerminalAllAvailable =
    {
        tableAllModel
    }

    @throws[LibraryException]
    def returnBook(selected_row: Int)
    {
        assert(user.isLibrarian)
        val book: BookLoan = getTableLibrarianModel.get(selected_row)
        server.returnBook(book.getBookID, book.getUserID)
        reset()
    }

    @throws[LibraryException]
    def reset()
    {
        resetAllTable()
        resetBorrowedTable()
    }

    @throws[LibraryException]
    def resetAllTable()
    {
        if (!user.isLibrarian)
        {
            tableAllModel.setBooks(server.getAvailableBooks)
        }
    }

    @throws[LibraryException]
    def resetBorrowedTable()
    {
        if (user.isLibrarian)
        {
            tableLibrarianModel.setBooks(server.getAllLoans)
        }
        else
        {
            tableUserModel.setBooks(server.getBorrowedBy(user))
        }
    }

    def getTableLibrarianModel: BooksTerminalUserLibrarian =
    {
        tableLibrarianModel
    }

    @throws[LibraryException]
    def search(title: String)
    {
        tableAllModel.setBooks(searchByTitle(title))
    }

    @throws[LibraryException]
    def searchByTitle(title: String): util.List[Book] =
    {
        server.searchByTitle(title)
    }
}
