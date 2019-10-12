package project.controller;

import project.model.*;


public class TerminalController extends AbstractController
{
    private SalesAgency            salesAgency;
    private TerminalTableModel     tableAllModel;
    private TerminalUserTableModel tableUserModel;
    private User                   user;

    public TerminalController(SalesAgency salesAgency)
    {
        this.salesAgency = salesAgency;
        tableAllModel = new TerminalTableModel(salesAgency.getAllProducts());
        tableUserModel = new TerminalUserTableModel();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
        tableUserModel.setOrders(salesAgency.getAllOrderedBy(user.getId()));
    }

    /**
     * Getter for property 'tableAllModel'.
     *
     * @return Value for property 'tableAllModel'.
     */
    public TerminalTableModel getTableAllModel()
    {
        return tableAllModel;
    }

    public TerminalUserTableModel getTableUserModel()
    {
        return tableUserModel;
    }

    public int getAvailableQuantity(int productID)
    {
       return salesAgency.getAvailableQuantity(productID);
    }

    public void placeOrder(Product product, int quantity)
    {
        salesAgency.placeOrder(user.getId(),product.getId(),quantity);
    }

    public void cancelOrder(Order order)
    {
        salesAgency.cancelOrder(order.getId());
    }


    public void search(String name)
    {
        tableAllModel.setProducts(salesAgency.searchByName(name));
    }

    public void reset()
    {
        tableAllModel.setProducts(salesAgency.getAllProducts());
        tableUserModel.setOrders(salesAgency.getAllOrderedBy(user.getId()));
    }

//    @Override
//    public void update(Object arg)
//    {
//        System.out.println("Inside ObserverAspect: TerminalController.update");
//        reset();
//    }

    @Override
    public void close() {}
}
