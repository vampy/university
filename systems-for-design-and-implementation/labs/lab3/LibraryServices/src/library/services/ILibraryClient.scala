package library.services

import library.model.{Book, BookLoan}

trait ILibraryClient
{
    @throws[LibraryException]
    def updateBook(book: Book)

    @throws[LibraryException]
    def updateLoan(loan: BookLoan)
}
