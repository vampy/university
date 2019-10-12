package project.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import project.controller.DB;
import project.model.Order;
import project.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCProductRepository implements IProductRepository
{
    @Override
    public List<Product> getAll()
    {
        return DB.get().getJdbcTemplate().query("SELECT * FROM products", new ProductMapper());
    }

    @Override
    public List<Product> searchByName(String name)
    {
        return DB.get().getJdbcTemplate().query(
            "SELECT * FROM products WHERE name LIKE ?",
            new ProductMapper(),
            "%" + name + "%"
        );
    }

    @Override
    public List<Order> getAllOrderedBy(int userID)
    {
        return DB.get().getJdbcTemplate()
            .query("SELECT orders.id, products.name, products.price, orders.quantity FROM orders " +
                    "INNER JOIN products on orders.product_id = products.id WHERE orders.user_id = ?",
                new OrderMapper(), userID);
    }

    @Override
    public int getAvailableQuantity(int productID)
    {
        return DB.get().getJdbcTemplate()
            .queryForObject("SELECT quantity FROM products WHERE id=?", new Object[]{productID}, Integer.class);
    }

    @Override
    public void placeOrder(int userID, int productID, int quantity)
    {
        int remained_quantity = getAvailableQuantity(productID) - quantity;
        Assert.isTrue(remained_quantity >= 0);
        DB.get().getJdbcTemplate().update("UPDATE products SET quantity=? WHERE id=?", remained_quantity, productID);
        DB.get().getJdbcTemplate().update("INSERT INTO orders (user_id,product_id,quantity) VALUES (?,?,?)", userID, productID, quantity);
    }

    @Override
    public void cancelOrder(int orderID)
    {
        int quantity_ordered = DB.get().getJdbcTemplate()
            .queryForObject("SELECT quantity FROM orders WHERE id=?", new Object[]{orderID}, Integer.class);
        int product_ordered_id = DB.get().getJdbcTemplate()
            .queryForObject("SELECT product_id FROM orders WHERE id=?", new Object[]{orderID}, Integer.class);
        int new_quantity = getAvailableQuantity(product_ordered_id) + quantity_ordered;

        DB.get().getJdbcTemplate().update("UPDATE products SET quantity = ? WHERE id=?", new_quantity, product_ordered_id);
        DB.get().getJdbcTemplate().update("DELETE FROM orders WHERE id = ?", orderID);
    }

    private static class ProductMapper implements RowMapper<Product>
    {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getFloat("price")
            );
        }
    }

    private static class OrderMapper implements RowMapper<Order>
    {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return new Order(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getFloat("price"),
                rs.getInt("quantity")
            );
        }
    }

}
