package project.controller;


import project.model.Product;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TerminalTableModel extends AbstractTableModel
{
    private List<Product> products;
    private String[] cols = {"Product code", "Name", "Available quantity", "Price"};

    public TerminalTableModel(List<Product> products)
    {
        this.products = products;
    }

    @Override
    public int getRowCount()
    {
        return products.size();
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
                return products.get(rowIndex).getId();
            case 1:
                return products.get(rowIndex).getName();
            case 2:
                return products.get(rowIndex).getQuantity();
            case 3:
                return products.get(rowIndex).getPrice();
        }

        return null;
    }

    /**
     * Setter for property 'books'.
     *
     * @param products Value to set for property 'books'.
     */
    public void setProducts(List<Product> products)
    {
        this.products = products;
        fireTableDataChanged();
    }

    public Product get(int index)
    {
        return products.get(index);
    }
}
