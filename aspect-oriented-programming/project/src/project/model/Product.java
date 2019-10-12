package project.model;


public class Product
{
    private int id;
    private String name;
    private int quantity;
    private float price;

    public Product(int id, String name, int quantity, float price)
    {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public float getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return "Product{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", quantity=" + quantity +
            ", price=" + price +
            '}';
    }
}
