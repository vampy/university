package controller;

import model.Product;
import persistence.FileIO;

import java.io.IOException;
import java.util.ArrayList;

public class Ctrl
{
    FileIO io = new FileIO();

    public void readProducts(String f)
    {
        try
        {
            io.readFile(f);
        }
        catch (NumberFormatException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean addProduct(Product p)
    {
        try
        {
            return io.addNewProduct(p);
        }
        catch (NumberFormatException | IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Product> getProductsCategory(String cat)
    {
        return io.getProductsCategory(cat);
    }

    public int getStockProduct(String pname)
    {
        return io.getStockProduct(pname);
    }

    // TODO usage
    public ArrayList<Product> stockSituation()
    {
        return io.stockSituation();
    }
}
