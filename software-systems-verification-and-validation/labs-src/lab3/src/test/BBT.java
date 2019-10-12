package test;

import controller.Ctrl;
import model.Product;
import org.junit.Assert;
import org.junit.Test;
import persistence.FileIO;

public class BBT
{

    private Ctrl ctrl;

    @org.junit.Before
    public void setUp() throws Exception
    {
        ctrl = new Ctrl();
    }

    @org.junit.After
    public void tearDown() throws Exception
    {

    }

    public static String repeat(String str, int times)
    {
        return new String(new char[times]).replace("\0", str);
    }

    @Test
    public void testValidProduct() // TC 1
    {
        Product p = new Product(0, "r", "r", 0);
        Assert.assertEquals(true, ctrl.addProduct(p));
    }

    @Test
    public void testCodeNegative() // TC 2
    {
        Product p = new Product(-1, "random_name", "random_category", 42);
        Assert.assertEquals(false, ctrl.addProduct(p));
    }

    @Test
    public void testNameOverMaxLength() // TC 3
    {
        String name = repeat("q", FileIO.MAX_LENGTH + 1);
        Product p = new Product(2, name, "random_category", 2);
        Assert.assertEquals(false, ctrl.addProduct(p));
    }

    @Test
    public void testNameMaxLength() // TC 4
    {
        String name = repeat("q", FileIO.MAX_LENGTH);
        Product p = new Product(2, name, "random_category", 2);
        Assert.assertEquals(true, ctrl.addProduct(p));
    }

    @Test
    public void testNameEmpty() // TC 5
    {
        Product p = new Product(100, "", "random_category", 100);
        Assert.assertEquals(false, ctrl.addProduct(p));
    }

    @Test
    public void testQuantityNegative() // TC 6
    {
        Product p = new Product(99, "random_name", "random_category", -1);
        Assert.assertEquals(false, ctrl.addProduct(p));
    }

    @Test
    public void testCategoryOverMaxLength() // TC 7
    {
        String category = repeat("q", FileIO.MAX_LENGTH + 1);
        Product p = new Product(99, "random_name", category, 23);
        Assert.assertEquals(false, ctrl.addProduct(p));
    }

    @Test
    public void testCategoryMaxLength() // TC 8
    {
        String category = repeat("q", FileIO.MAX_LENGTH);
        Product p = new Product(99, "random_name", category, 23);
        Assert.assertEquals(true, ctrl.addProduct(p));
    }

    @Test
    public void testCategoryEmpty() // TC 9
    {
        Product p = new Product(55, "random_name", "", 55);
        Assert.assertEquals(false, ctrl.addProduct(p));
    }

}
