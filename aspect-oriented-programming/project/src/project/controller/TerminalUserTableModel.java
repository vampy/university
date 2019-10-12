package project.controller;

import project.model.Order;


import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TerminalUserTableModel extends AbstractTableModel
{
    private List<Order> orders;
    private String[] cols = {"Order id", "Product name", "Quantity", "Total price"};

    public TerminalUserTableModel() {}

    @Override
    public int getRowCount()
    {
        return orders.size();
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
                return orders.get(rowIndex).getId();
            case 1:
                return orders.get(rowIndex).getProductName();
            case 2:
                return orders.get(rowIndex).getQuantity();
            case 3:
                return orders.get(rowIndex).getTotalPrice();
        }

        return null;
    }

    /**
     * Setter for property 'books'.
     *
     * @param orders Value to set for property 'books'.
     */
    public void setOrders(List<Order> orders)
    {
        this.orders = orders;
        fireTableDataChanged();
    }

    public Order get(int index)
    {
        return orders.get(index);
    }
}
