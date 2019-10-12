package chat.client.gui

import java.util
import javax.swing.table.AbstractTableModel

import library.model.Book

class BooksTerminalAllAvailable extends AbstractTableModel
{
    private val cols: Array[String] = Array("Id", "Title", "Author", "Availability")
    private var books: util.List[Book] = new util.ArrayList[Book]

    override def getRowCount: Int =
    {
        books.size
    }

    override def getColumnName(column: Int): String =
    {
        cols(column)
    }

    override def getColumnCount: Int =
    {
        cols.length
    }

    // sigh https://biomunky.wordpress.com/2011/02/02/scala-swing-tables/
    override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef =
    {
        columnIndex match
        {
            case 0 =>
                return books.get(rowIndex).getId.asInstanceOf[AnyRef]
            case 1 =>
                return books.get(rowIndex).getTitle
            case 2 =>
                return books.get(rowIndex).getAuthor
            case 3 =>
                return if (books.get(rowIndex).canBorrow)
                {
                    books.get(rowIndex).getStockAvailable.asInstanceOf[AnyRef]
                }
                else
                {
                    "Borrowed :("
                }
        }
        null
    }

    def updateBook(new_book: Book)
    {
        var found_index: Int = -1
        var loop: Boolean = true
        var i: Int = 0
        while (loop && i < books.size)
        {
            val current_book: Book = books.get(i)
            if (new_book.getId == current_book.getId)
            {
                found_index = i
                loop = false
            }

            i += 1
        }
        if (found_index != -1)
        {
            if (new_book.canBorrow)
            {
                books.set(found_index, new_book)
            }
            else
            {
                books.remove(found_index)
            }
        }
        else
        {
            books.add(new_book)
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
        books.get(index)
    }
}
