package library.model

import java.io.Serializable
import java.util

class Book(var id: Int, var title: String, var author: String, var datePublish: String, var stock: Int,
           var borrowers: util.Set[Integer]) extends Serializable
{
    def getId: Int =
    {
        id
    }

    def setId(id: Int)
    {
        this.id = id
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

    def getBorrowers: util.Set[Integer] =
    {
        borrowers
    }

    def setBorrowers(borrowers: util.Set[Integer])
    {
        this.borrowers = borrowers
    }

    def addBorrower(user_id: Int)
    {
        assert(canBorrow)
        borrowers.add(user_id)
    }

    def canBorrow: Boolean =
    {
        stock >= borrowers.size + 1
    }

    def removeBorrower(user_id: Int)
    {
        borrowers.remove(user_id)
    }

    def canBorrow(user: User): Boolean =
    {
        canBorrow(user.getId)
    }

    def canBorrow(user_id: Int): Boolean =
    {
        !borrowers.contains(user_id)
    }

    def getStockAvailable: Int =
    {
        stock - borrowers.size
    }

    override def equals(obj: Any): Boolean =
    {
        if (obj.isInstanceOf[Book])
        {
            val book: Book = obj.asInstanceOf[Book]
            return id == book.id
        }
        false
    }

    override def toString: String =
    {
        "Book{" + "id=" + id + ", title='" + title + '\'' + ", author='" + author + '\'' + ", datePublish='" + datePublish + '\'' + ", stock=" + stock + ", borrowers=" + borrowers + '}'
    }
}
