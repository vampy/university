package project.model;


public class Order
{
    private int id;
    private String productName;
    private float productPrice;
    private int quantity;

    public Order(int id, String productName, float productPrice, int quantity)
    {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getTotalPrice() { return quantity * productPrice; }

    @Override
    public String toString()
    {
        return "Order{" +
            "id=" + id +
            ", productName='" + productName + '\'' +
            ", productPrice=" + productPrice +
            ", quantity=" + quantity +
            '}';
    }
}
