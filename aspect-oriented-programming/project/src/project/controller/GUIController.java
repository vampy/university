package project.controller;

import project.model.SalesAgency;

public class GUIController extends AbstractController
{
    private SalesAgency salesAgency;
    private MainTableModel tableModel;

    public GUIController(SalesAgency salesAgency)
    {
        this.salesAgency = salesAgency;
        tableModel = new MainTableModel(salesAgency.getAllProducts());
    }

    public SalesAgency getSalesAgency()
    {
        return salesAgency;
    }

    /**
     * Getter for property 'tableModel'.
     *
     * @return Value for property 'tableModel'.
     */
    public MainTableModel getTableModel()
    {
        return tableModel;
    }

    public void placeOrder(int userID, int productID, int quantity)
    {
        salesAgency.placeOrder(userID,productID,quantity);
    }

    public void cancelOrder(int orderID)
    {
        salesAgency.cancelOrder(orderID);
    }
    public void setTableProductsFromSalesAgency()
    {
        tableModel.setProducts(salesAgency.getAllProducts());
    }

//    @Override
//    public void update(Object arg)
//    {
//        setTableBooksFromLibrary();
//    }

    @Override
    public void close() {}
}
