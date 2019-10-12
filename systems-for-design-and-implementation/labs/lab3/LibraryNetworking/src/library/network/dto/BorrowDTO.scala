package library.network.dto

import java.io.Serializable

import library.model.{Book, User}

class BorrowDTO(var book: Book, var user: User) extends Serializable
{
    def getBook: Book =
    {
        book
    }

    def setBook(book: Book)
    {
        this.book = book
    }

    def getUser: User =
    {
        user
    }

    def setUser(user: User)
    {
        this.user = user
    }

    override def toString: String =
    {
        "BorrowDTO{" + "book=" + book + ", user=" + user + '}'
    }
}
