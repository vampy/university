package chat.client.gui

import java.util
import javax.swing.table.AbstractTableModel

import library.model.BookLoan

class BooksTerminalUserLibrarian extends AbstractTableModel
{
    private val cols: Array[String] = Array("Book id", "User id", "username", "Book Title", "Stock")
    private var books: util.List[BookLoan] = new util.ArrayList[BookLoan]

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

    override def getValueAt(rowIndex: Int, columnIndex: Int): AnyRef =
    {
        columnIndex match
        {
            case 0 =>
                return books.get(rowIndex).getBookID.asInstanceOf[AnyRef]
            case 1 =>
                return books.get(rowIndex).getUserID.asInstanceOf[AnyRef]
            case 2 =>
                return books.get(rowIndex).getUsername
            case 3 =>
                return books.get(rowIndex).getTitle
            case 4 =>
                return books.get(rowIndex).getStock.asInstanceOf[AnyRef]
        }
        null
    }

    def updateBookLoan(new_loan: BookLoan)
    {
        var found_index: Int = -1
        var loop: Boolean = true
        var i: Int = 0
        while (loop && i < books.size)
        {
            val current_loan: BookLoan = books.get(i)
            if (new_loan.getBookID == current_loan.getBookID && new_loan.getUserID == current_loan.getUserID)
            {
                found_index = i
                loop = false
            }
            i += 1
        }
        if (found_index != -1)
        {
            books.set(found_index, new_loan)
        }
        else
        {
            books.add(new_loan)
        }
        fireTableDataChanged()
    }

    def setBooks(books: util.List[BookLoan])
    {
        this.books = books
        fireTableDataChanged()
    }

    def get(index: Int): BookLoan =
    {
        books.get(index)
    }
}
