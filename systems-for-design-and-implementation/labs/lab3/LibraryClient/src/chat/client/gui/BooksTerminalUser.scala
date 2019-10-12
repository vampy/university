package chat.client.gui

import java.util
import javax.swing.table.AbstractTableModel

import library.model.{Book, User}

class BooksTerminalUser extends AbstractTableModel
{
    private val cols: Array[String] = Array("Id", "Title", "Author")
    private var books: util.List[Book] = new util.ArrayList[Book]

    override def getRowCount: Int =
    {
        this.books.size
    }

    override def getColumnName(column: Int): String =
    {
        this.cols(column)
    }

    override def getColumnCount: Int =
    {
        cols.length
    }

    override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef =
    {
        columnIndex match
        {
            case 0 =>
                return this.books.get(rowIndex).getId.asInstanceOf[AnyRef]
            case 1 =>
                return this.books.get(rowIndex).getTitle
            case 2 =>
                return this.books.get(rowIndex).getAuthor
        }
        null
    }

    def updateBook(user: User, new_book: Book)
    {
        var found_index: Int = -1
        var loop: Boolean = true
        var i: Int = 0
        while (loop && i < this.books.size)
        {

            val current_book: Book = this.books.get(i)
            if (new_book.getId == current_book.getId)
            {
                found_index = i
                loop = false
            }
            i += 1
        }
        if (found_index != -1)
        {
            if (new_book.canBorrow(user))
            {
                this.books.remove(found_index)
                user.removeBorrowed(new_book.getId)
            }
            else
            {
                this.books.set(found_index, new_book)
            }
        }
        else
        {
            println("THIS SHOULD NEVER HAPPEN")
        }
        fireTableDataChanged()
    }

    def setBooks(books: util.List[Book])
    {
        this.books = books
        fireTableDataChanged()
    }

    def get(index: Int): Book =
    {
        this.books.get(index)
    }
}
