package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Product
{
    private int    code     = 0;
    private String name;
    private String category;
    private int    quantity = 0;

    public Product(int code, String name, String category, int quantity)
    {
        this.code = code;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public Product()
    {
        // TODO Auto-generated constructor stub
    }

    public int getCode()
    {
        return quantity;
    }

    // TODO usage
    public void setCode(int code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCategory()
    {
        return category;
    }

    // TODO usage
    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getQuantity()
    {
        return quantity;
    }

    // TODO usage
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    // TODO usage
    public boolean compareTo(Product q)
    {
        return code == q.getCode();
    }

    public boolean read() throws IOException
    {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        System.out.print("code = ");
        String scode = br.readLine();

        System.out.print("name = ");
        this.name = br.readLine();

        System.out.print("category = ");
        this.category = br.readLine();

        System.out.print("quantity = ");
        String squantity = br.readLine();

        try
        {
            this.quantity = Integer.parseInt(squantity);
        }
        catch (Exception e)
        {
            System.err.print("Quantity not >=0");
            return false;
        }

        try
        {
            this.code = Integer.parseInt(scode);
        }
        catch (Exception e)
        {
            System.err.print("error Code not >0");
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "Product [code=" + code + ", name=" + name + ", category="
            + category + ", quantity=" + quantity + "]";
    }


}
