package library.services

import java.util

import library.model.{Book, BookLoan, User}

trait ILibraryServer
{
    @throws[LibraryException]
    def login(user: User, client: ILibraryClient): User

    @throws[LibraryException]
    def logout(user: User, client: ILibraryClient)

    @throws[LibraryException]
    def getAvailableBooks: util.List[Book]

    @throws[LibraryException]
    def getBorrowedBy(user: User): util.List[Book]

    @throws[LibraryException]
    def searchByTitle(title: String): util.List[Book]

    @throws[LibraryException]
    def getAllLoans: util.List[BookLoan]

    @throws[LibraryException]
    def borrowBook(book: Book, user: User)

    @throws[LibraryException]
    def returnBook(book_id: Int, user_id: Int)
}
