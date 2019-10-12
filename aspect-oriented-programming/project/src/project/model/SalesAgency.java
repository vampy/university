package project.model;

import project.aspect.AuthRequired;
import project.aspect.Subject;
import project.aspect.SubjectChanged;
import project.repository.IProductRepository;

import java.util.List;


public class SalesAgency implements Subject
{
    IProductRepository products;

    public SalesAgency(IProductRepository products){ this.products = products; }

    public List<Product> getAllProducts()
    {
        return products.getAll();
    }

    public List<Order> getAllOrderedBy(int userID)
    {
        return products.getAllOrderedBy(userID);
    }

    public int getAvailableQuantity(int productID) { return products.getAvailableQuantity(productID); }

    public List<Product> searchByName(String name) { return products.searchByName(name);}

    @SubjectChanged
    @AuthRequired
    public void placeOrder(int user_id, int product_id, int quantity)
    {
        products.placeOrder(user_id, product_id, quantity);
    }

    @SubjectChanged
    @AuthRequired
    public void cancelOrder(int order_id)
    {
        products.cancelOrder(order_id);
    }
}
