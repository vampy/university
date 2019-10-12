package project.view;

import project.controller.TerminalController;
import project.model.Order;
import project.model.Product;
import project.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Terminal
{
    private static final boolean DEBUG = true;
    private TerminalController controller;
    private JPanel             main_panel;
    private JTextField         search_textbox;
    private JButton            search_button;
    private JTable             table;
    private JButton            button_place_order;
    private JTable             tableUser;
    private JButton            button_cancel_order;
    private JButton            button_reset;

    public Terminal(final TerminalController controller, String dusername)
    {
        // login user
        String username;
        if (!DEBUG || dusername == null)
        {
            username = GUI.showInputDialog(null, "Username: ", "Login");
            if (username == null)
            {
                GUI.showErrorDialog(null, "Username is invalid");
                return;
            }
        }
        else
        {
            username = dusername;
        }

        User user = User.login(username);
        if (user == null)
        {
            GUI.showErrorDialog(null, "Username does not exist in the database");
            return;
        }
        controller.setUser(user);


        this.controller = controller;
        table.setModel(controller.getTableAllModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(2);
        table.getColumnModel().getColumn(1).setPreferredWidth(3);
        tableUser.setModel(controller.getTableUserModel());
        tableUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JFrame frame = new JFrame("--- Terminal: " + controller.getUser().getUsername() + " ---");
        frame.setContentPane(main_panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Terminal.this.controller.close();
            }
        });

        search_button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String title = search_textbox.getText();
                System.out.println("Clicked search button: " + title);

                controller.search(title);
            }
        });
        button_reset.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                search_textbox.setText("");
                System.out.println("Clicked reset search button");

                controller.reset();
            }
        });

        button_place_order.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked button placeOrder");
                int row = table.getSelectedRow();
                if (row >= 0)
                {
                    Product product = controller.getTableAllModel().get(row);
                    int wanted_quantity;
                    try
                    {
                        wanted_quantity = Integer.valueOf(GUI.showInputDialog(null, "Quantity: ", "Ok"));

                    }
                    catch (NumberFormatException nfe)
                    {
                        GUI.showErrorDialog(main_panel, "Please insert an integer");
                        return;
                    }

                    if (controller.getAvailableQuantity(product.getId()) < wanted_quantity)
                    {
                        GUI.showErrorDialog(main_panel, "The quantity for this order is greater than the available quantity");
                        return;
                    }
                    controller.placeOrder(product, wanted_quantity);
                }
            }
        });
        button_cancel_order.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked button cancelOrder");
                int row = tableUser.getSelectedRow();
                if (row >= 0)
                {
                    Order order = controller.getTableUserModel().get(row);
                    controller.cancelOrder(order);

                }
            }
        });

    }

    public static void run(final TerminalController controller, final String dusername)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Terminal gui = new Terminal(controller, dusername);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
