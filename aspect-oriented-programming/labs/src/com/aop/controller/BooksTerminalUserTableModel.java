package com.aop.controller;

import com.aop.model.Book;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BooksTerminalUserTableModel extends AbstractTableModel
{
    private List<Book> books;
    private String[] cols = {"Id", "Title", "Author"};

    public BooksTerminalUserTableModel() {}

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
        }

        return null;
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
