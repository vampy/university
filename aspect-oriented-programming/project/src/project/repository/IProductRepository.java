package project.repository;

import project.model.Order;
import project.model.Product;

import java.util.List;


public interface IProductRepository
{
    List<Product> getAll();
    List<Product> searchByName(String name);
    List<Order> getAllOrderedBy(int userID);
    int getAvailableQuantity(int productID);

    void placeOrder(int user_id, int product_id, int quantity);
    void cancelOrder(int order_id);
}
