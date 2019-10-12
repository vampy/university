package project.view;

import project.controller.GUIController;
import project.controller.TerminalController;

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

        JFrame frame = new JFrame("---  Sales agency ---");
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

        button_create_console.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Clicked create terminal");
                Terminal.run(new TerminalController(controller.getSalesAgency()), null);
            }
        });

        // TODO remove these
        Terminal.run(new TerminalController(controller.getSalesAgency()), "daniel");
        Terminal.run(new TerminalController(controller.getSalesAgency()), "gabi");
    }

    public static String showInputDialog(Component parent, String message, String title)
    {
        String[] options = {"OK"};
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel(message);
        JTextField txt = new JTextField(10);
        panel.add(lbl);
        panel.add(txt);

        JOptionPane.showOptionDialog(parent, panel, title, JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

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
