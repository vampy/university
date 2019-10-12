package library.model

import java.io.Serializable

class BookLoan(var userID: Int, var bookID: Int, var dateLoan: String, var title: String, var author: String,
               var datePublish: String, var stock: Int, var username: String) extends Serializable
{
    def getUserID: Int =
    {
        userID
    }

    def setUserID(userID: Int)
    {
        this.userID = userID
    }

    def getBookID: Int =
    {
        bookID
    }

    def setBookID(bookID: Int)
    {
        this.bookID = bookID
    }

    def getDateLoan: String =
    {
        dateLoan
    }

    def setDateLoan(dateLoan: String)
    {
        this.dateLoan = dateLoan
    }

    def getTitle: String =
    {
        title
    }

    def setTitle(title: String)
    {
        this.title = title
    }

    def getAuthor: String =
    {
        author
    }

    def setAuthor(author: String)
    {
        this.author = author
    }

    def getDatePublish: String =
    {
        datePublish
    }

    def setDatePublish(datePublish: String)
    {
        this.datePublish = datePublish
    }

    def getStock: Int =
    {
        stock
    }

    def setStock(stock: Int)
    {
        this.stock = stock
    }

    def getUsername: String =
    {
        username
    }

    def setUsername(username: String)
    {
        this.username = username
    }

    override def toString: String =
    {
        "BookLoan{" + "userID=" + userID + ", bookID=" + bookID + ", dateLoan='" + dateLoan + '\'' + ", title='" + title + '\'' + ", author='" + author + '\'' + ", datePublish='" + datePublish + '\'' + ", stock=" + stock + ", username='" + username + '\'' + '}'
    }
}
