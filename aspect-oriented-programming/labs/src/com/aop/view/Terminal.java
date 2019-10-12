package com.aop.view;

import com.aop.controller.TerminalController;
import com.aop.model.Book;
import com.aop.model.User;

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
    private JButton            button_loan;
    private JTable             tableUser;
    private JButton            button_return;
    private JButton            button_reset;
    private int loans = 0;

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
        loans = controller.getCountLoaned();

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

        button_loan.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked button loanBook");
                int row = table.getSelectedRow();
                if (row >= 0)
                {
                    Book book = controller.getTableAllModel().get(row);
                    if (book.isLoaned())
                    {
                        GUI.showErrorDialog(main_panel, "Book is already loaned");
                        return;
                    }
                    if (loans != 3)
                    {
                        System.out.println(book);
                        controller.loanBook(book);
                        loans++;
                    }
                    else
                    {
                        GUI.showErrorDialog(main_panel, "You already loaned 3 books");
                    }
                }
            }
        });
        button_return.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked button return");
                int row = tableUser.getSelectedRow();
                if (row >= 0)
                {
                    Book book = controller.getTableUserModel().get(row);
                    controller.returnBook(book);
                    loans--;
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
