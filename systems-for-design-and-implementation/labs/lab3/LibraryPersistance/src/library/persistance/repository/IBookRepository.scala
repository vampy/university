package library.persistance.repository

import java.util

import library.model.{Book, BookLoan, User}

trait IBookRepository
{
    def getAll: util.List[Book]

    def getAllAvailable: util.List[Book]

    def getAllBorrowedBy(user: User): util.List[Book]

    def getAllBorrowedBy(user_id: Int): util.List[Book]

    def searchByTitle(title: String): util.List[Book]

    def getBookByID(book_id: Int): Book

    def borrowBook(book: Book, user: User)

    def borrowBook(book_id: Int, user_id: Int)

    def returnBook(book: Book, user: User)

    def returnBook(book_id: Int, user_id: Int)

    def update(book: Book)

    def getAllLoans: util.List[BookLoan]

    def getLoan(book_id: Int, user_id: Int): BookLoan
}

