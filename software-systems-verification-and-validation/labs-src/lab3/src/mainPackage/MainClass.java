package mainPackage;

import controller.Ctrl;
import model.Product;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainClass
{
    private static int MENU_ADD_PRODUCT   = 1;
    private static int MENU_STOCK_PRODUCT = 2;
    private static int MENU_LIST_CATEGORY = 3;

    private static int MENU_HELP = 0;
    private static int MENU_QUIT = 99;

    public static void printMenu()
    {
        String out = "---------- Shop menu ------------\n\n";
        out += String.format("%d. This help menu\n", MENU_HELP);
        out += String.format("%d. Add product\n", MENU_ADD_PRODUCT);
        out += String.format("%d. Stock product\n", MENU_STOCK_PRODUCT);
        out += String.format("%d. List all products from category\n\n", MENU_LIST_CATEGORY);
        out += String.format("%d. Quit\n", MENU_QUIT);

        System.out.print(out);
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        Scanner input = new Scanner(System.in);

        Ctrl controller = new Ctrl();
        controller.readProducts("products.txt");

        printMenu();
        while (true)
        {
            System.out.print(">>> ");
            int command;
            try
            {
                command = input.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Please choose an option from the menu");
                command = MENU_HELP;
            }
            if (command == MENU_ADD_PRODUCT)
            {
                Product p = new Product();
                if (p.read())
                {
                    System.out.println(controller.addProduct(p));
                }
                else
                {
                    System.out.println("Failed to read product, try again");
                }
            }
            else if (command == MENU_STOCK_PRODUCT)
            {
                System.out.print("name = ");
                String name = input.next().trim();
                System.out.println("Stock = " + controller.getStockProduct(name));
            }
            else if (command == MENU_LIST_CATEGORY)
            {
                System.out.print("category = ");
                String category = input.next();
                System.out.println("The products having the category " + category + ":");
                System.out.println(controller.getProductsCategory(category).toString());
            }
            else if (command == MENU_QUIT)
            {
                System.out.println("Quiting....");
                break;
            }
            else
            {
                printMenu();
            }
        }
        input.close();
    }
}
