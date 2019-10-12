package com.aop.view;

import com.aop.controller.GUIController;
import com.aop.controller.TerminalController;
import com.aop.model.Book;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI
{
    private GUIController controller;
    private JButton       button_create_console;
    private JPanel        main_panel;
    private JButton       button_toggle_loan;
    private JTable        table;

    public GUI(final GUIController controller)
    {
        this.controller = controller;
        table.setModel(controller.getTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(2);
        columnModel.getColumn(1).setPreferredWidth(3);
        columnModel.getColumn(3).setPreferredWidth(5);

        JFrame frame = new JFrame("--- LIBRARY ---");
        frame.setContentPane(main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(400, 400);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                GUI.this.controller.close();
            }
        });

        button_toggle_loan.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked remove loanBook");
                int row = table.getSelectedRow();
                if (row >= 0)
                {
                    Book book = controller.getTableModel().get(row);
                    if (book.isLoaned())
                    {
                        controller.returnBook(book);
                    }
                    else
                    {
                        controller.loanBook(book, 3); // 3 is admin user
                    }
                    System.out.println(book);
                }
            }
        });
        button_create_console.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked create terminal");
                Terminal.run(new TerminalController(controller.getLibrary()), null);
            }
        });

        // TODO remove these
        Terminal.run(new TerminalController(controller.getLibrary()), "alex");
        Terminal.run(new TerminalController(controller.getLibrary()), "daniel");
    }

    public static String showInputDialog(Component parent, String message, String title)
    {
        String[] options = {"OK"};
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel(message);
        JTextField txt = new JTextField(10);
        panel.add(lbl);
        panel.add(txt);

        JOptionPane.showOptionDialog(parent, panel, title, JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        String text = txt.getText().trim();
        if (text.length() > 0)
        {
            return text;
        }

        return null;
    }

    public static String showInputDialog(Component parent, String message)
    {
        return showInputDialog(parent, message, "Input");
    }

    public static void showErrorDialog(Component parent, String message)
    {
        JOptionPane.showMessageDialog(parent, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccessDialog(Component parent, String message)
    {
        JOptionPane.showMessageDialog(parent, message, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void run(final GUIController controller)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    GUI gui = new GUI(controller);
                    //System.out.println(controller.getLibrary().getAllBooks());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
