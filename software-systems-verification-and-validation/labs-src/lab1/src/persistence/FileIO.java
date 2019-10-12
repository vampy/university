package persistence;

import model.Product;

import java.io.*;
import java.util.ArrayList;

public class FileIO
{
    private ArrayList<Product> allProducts = new ArrayList<Product>();

    public ArrayList<Product> getAllProducts()
    {
        return allProducts;
    }

    public void readFile(String fname) throws NumberFormatException, IOException
    {
        FileInputStream f = new FileInputStream(fname);
        DataInputStream in = new DataInputStream(f);
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        String rd;
        String[] line;
        while ((rd = buf.readLine()) != null)
        {
            line = rd.split(" ");
            allProducts.add(new Product(Integer.parseInt(line[0].trim()), line[1].trim(), line[2].trim(), Integer.parseInt(line[3].trim())));
        }
        in.close();

        System.out.println(allProducts);
    }

    public String addNewProduct(Product p) throws IOException
    {
        if (p.getCode() > 0 || p.getQuantity() >= 0)
        {
            BufferedWriter out = new BufferedWriter(new FileWriter("products.txt", true));
            boolean isFound = false;
            for (Product i : allProducts)
            {
                if (i.getCode() == p.getCode())
                {
                    isFound = true;
                    break;
                }
            }
            if (!isFound)
            {
                out.newLine();
                out.write(p.getCode() + " " + p.getName() + " " + p.getCategory() + " " + p.getQuantity());
                out.close();
                allProducts.add(p);
            }
            else
            {
                System.err.println("This code already exists");
                out.close();
                return "This code already exists";
            }
        }
        else
        {
            return "code q";
        }
        System.out.println("Product added");
        return "Success";
    }

    public ArrayList<Product> getProductsCategory(String cat)
    {
        ArrayList<Product> cProducts = new ArrayList<Product>();
        for (Product p : allProducts)
        {
            if (p.getCategory().equals(cat))
            {
                cProducts.add(p);
            }
        }

        return cProducts;
    }

    public int getStockProduct(String pname)
    {
        for (Product p : allProducts)
        {
            if (p.getName().equals(pname))
                return p.getQuantity();
        }

        return 0;
    }

    // TODO usage
    public ArrayList<Product> stockSituation()
    {
        return allProducts;
    }

}
