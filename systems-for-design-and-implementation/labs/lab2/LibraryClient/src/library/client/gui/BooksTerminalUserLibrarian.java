package library.client.gui;

import library.model.BookLoan;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BooksTerminalUserLibrarian extends AbstractTableModel
{
    private List<BookLoan> books = new ArrayList<BookLoan>();
    private String[]       cols  = {"Book id", "User id", "username", "Book Title", "Stock"};

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
                return books.get(rowIndex).getBookID();
            case 1:
                return books.get(rowIndex).getUserID();
            case 2:
                return books.get(rowIndex).getUsername();
            case 3:
                return books.get(rowIndex).getTitle();
            case 4:
                return books.get(rowIndex).getStock();
        }

        return null;
    }

    public void updateBookLoan(BookLoan new_loan)
    {
        // find book
        int found_index = -1;
        boolean loop = true;
        for (int i = 0; loop && i < books.size(); i++)
        {
            BookLoan current_loan = books.get(i);
            if (new_loan.getBookID() == current_loan.getBookID() && new_loan.getUserID() == current_loan.getUserID())
            {
                found_index = i;
                loop = false;
            }
        }

        if (found_index != -1)
        {
            // TODO this seems useless
            books.set(found_index, new_loan);
        }
        else // add
        {
            books.add(new_loan);
        }

        fireTableDataChanged();
    }

    public void setBooks(List<BookLoan> books)
    {
        this.books = books;
        fireTableDataChanged();
    }

    public BookLoan get(int index)
    {
        return books.get(index);
    }
}
