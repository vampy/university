package library.client.gui;

import library.model.Book;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BooksTerminalAllAvailable extends AbstractTableModel
{
    private List<Book> books = new ArrayList<Book>();
    private String[]   cols  = {"Id", "Title", "Author", "Availability"};

    @Override
    public int getRowCount()
    {
        return books.size();
    }

    @Override
    public String getColumnName(int column)
    {
        return cols[column];
    }

    @Override
    public int getColumnCount()
    {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return books.get(rowIndex).getId();
            case 1:
                return books.get(rowIndex).getTitle();
            case 2:
                return books.get(rowIndex).getAuthor();
            case 3:
                return books.get(rowIndex).canBorrow() ? books.get(rowIndex).getStockAvailable() : "Borrowed :(";
        }

        return null;
    }

    public void updateBook(Book new_book)
    {
        // find book
        int found_index = -1;
        boolean loop = true;
        for (int i = 0; loop && i < books.size(); i++)
        {
            Book current_book = books.get(i);
            if (new_book.getId() == current_book.getId())
            {
                found_index = i;
                loop = false;
            }
        }

        if (found_index != -1)
        {
            // see if new book we can borrow, if not, remove it, otherwise update it
            if (new_book.canBorrow())
            {
                books.set(found_index, new_book);
            }
            else
            {
                books.remove(found_index);
            }
        }
        else // add
        {
            books.add(new_book);
        }

        fireTableDataChanged();
    }

    /**
     * Setter for property 'books'.
     *
     * @param books Value to set for property 'books'.
     */
    public void setBooks(List<Book> books)
    {
        this.books = books;
        fireTableDataChanged();
    }

    public Book get(int index)
    {
        return books.get(index);
    }
}
