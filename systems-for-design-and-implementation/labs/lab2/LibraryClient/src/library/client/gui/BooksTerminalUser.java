package library.client.gui;


import library.model.Book;
import library.model.User;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

// Only used by the librarian and displays the books
public class BooksTerminalUser extends AbstractTableModel
{
    private List<Book> books = new ArrayList<Book>();
    private String[]   cols  = {"Id", "Title", "Author"};

    public BooksTerminalUser()
    {

    }

    @Override
    public int getRowCount()
    {
        return this.books.size();
    }

    @Override
    public String getColumnName(int column)
    {
        return this.cols[column];
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
                return this.books.get(rowIndex).getId();
            case 1:
                return this.books.get(rowIndex).getTitle();
            case 2:
                return this.books.get(rowIndex).getAuthor();
        }

        return null;
    }

    public void updateBook(User user, Book new_book)
    {
        // find book
        int found_index = -1;
        boolean loop = true;
        for (int i = 0; loop && i < this.books.size(); i++)
        {
            Book current_book = this.books.get(i);
            if (new_book.getId() == current_book.getId())
            {
                found_index = i;
                loop = false;
            }
        }

        // can not add
        if (found_index != -1)
        {
            // see if new book we can borrow, if yes remove it, because it was returned
            // otherwise update it
            if (new_book.canBorrow(user))
            {
                // book was returned
                this.books.remove(found_index);
                user.removeBorrowed(new_book.getId());
            }
            else
            {
                this.books.set(found_index, new_book);
            }
        }
        else
        {
            System.out.println("THIS SHOULD NEVER HAPPEN");
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
        return this.books.get(index);
    }
}
