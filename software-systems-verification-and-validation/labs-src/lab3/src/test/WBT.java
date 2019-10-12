package test;

import model.Product;
import org.junit.Assert;
import org.junit.Test;
import persistence.FileIO;

import java.util.ArrayList;

public class WBT
{
    private FileIO fileIO;

    @org.junit.Before
    public void setUp() throws Exception
    {
        fileIO = new FileIO();
    }

    @Test
    public void testEmpty()
    {
        Assert.assertTrue(fileIO.getProductsCategory("random").isEmpty());
    }

    @Test
    public void testNoCategory()
    {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1, "random_name", "random", 99));
        products.add(new Product(2, "random_name", "random", 99));
        fileIO.setAllProducts(products);
        Assert.assertTrue(fileIO.getProductsCategory("wow").isEmpty());
    }

    @Test
    public void testCategory()
    {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(1, "random_name", "random", 99));
        products.add(new Product(2, "random_name", "random", 99));
        products.add(new Product(3, "random_name", "wow", 2));
        fileIO.setAllProducts(products);

        ArrayList<Product> test_products = fileIO.getProductsCategory("wow");
        Assert.assertEquals(test_products.size(), 1);
        Assert.assertEquals(test_products.get(0), products.get(2));
    }

}
