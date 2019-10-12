package library.client.gui;

import library.services.LibraryException;

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
    private JButton            button_borrow;
    private JTable             tableUser;
    private JButton            button_return;
    private JButton            button_reset;
    private JPanel             bottom_half;
    private JPanel             search_top;
    private JPanel             top_half;
    private JLabel             bottom_label;

    public Terminal(final TerminalController controller)
    {
        this.controller = controller;

        final JFrame frame = new JFrame("--- Terminal: " + controller.getUser().getUsername() + " ---");
        frame.setContentPane(main_panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

//        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                controller.logout();
            }
        });

        // only librarians can return
        if (controller.isLibrarian())
        {
            button_return.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Clicked button return");
                    int row = tableUser.getSelectedRow();
                    if (row >= 0)
                    {
                        try
                        {
                            controller.returnBook(row);
                        }
                        catch (LibraryException ex)
                        {
                            Terminal.showErrorDialog(main_panel, ex.getMessage());
                        }
                    }
                }
            });

            bottom_label.setText("All books");
            search_top.setVisible(false);
            top_half.setVisible(false);

            tableUser.setModel(controller.getTableLibrarianModel());
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        else
        {
            table.setModel(controller.getTableAllModel());
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getColumnModel().getColumn(0).setPreferredWidth(2);
            table.getColumnModel().getColumn(1).setPreferredWidth(3);
            tableUser.setModel(controller.getTableUserModel());
            tableUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            search_button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String title = search_textbox.getText();
                    System.out.println("Clicked search button: " + title);

                    try
                    {
                        controller.search(title);
                    }
                    catch (LibraryException ex)
                    {
                        Terminal.showErrorDialog(frame, ex.getMessage());
                    }
                }
            });

            button_reset.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    search_textbox.setText("");
                    System.out.println("Clicked reset search button");

                    try
                    {
                        controller.reset();
                    }
                    catch (LibraryException ex)
                    {
                        Terminal.showErrorDialog(frame, ex.getMessage());
                    }
                }
            });

            button_borrow.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Clicked button loanBook");
                    int row = table.getSelectedRow();
                    if (row >= 0)
                    {
                        try
                        {
                            controller.borrowBook(row);
                        }
                        catch (LibraryException ex)
                        {
                            Terminal.showErrorDialog(main_panel, ex.getMessage());
                        }
                    }
                }
            });

            button_return.setVisible(false);
        }
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
// FIXME Enable the connection to the e gui when you want to edit the gui, alright?

    public static void run(final TerminalController controller)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    new Terminal(controller);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        main_panel = new JPanel();
        main_panel.setLayout(new GridBagLayout());
        search_top = new JPanel();
        search_top.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.01;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 5, 0, 0);
        main_panel.add(search_top, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Book title TEST:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        search_top.add(label1, gbc);
        search_textbox = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        search_top.add(search_textbox, gbc);
        search_button = new JButton();
        search_button.setText("Search");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        search_top.add(search_button, gbc);
        button_reset = new JButton();
        button_reset.setText("Reset");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        search_top.add(button_reset, gbc);
        top_half = new JPanel();
        top_half.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        main_panel.add(top_half, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        top_half.add(scrollPane1, gbc);
        table = new JTable();
        scrollPane1.setViewportView(table);
        button_borrow = new JButton();
        button_borrow.setText("Borrow selected book");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top_half.add(button_borrow, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("All books");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        top_half.add(label2, gbc);
        bottom_half = new JPanel();
        bottom_half.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        main_panel.add(bottom_half, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bottom_half.add(scrollPane2, gbc);
        tableUser = new JTable();
        scrollPane2.setViewportView(tableUser);
        bottom_label = new JLabel();
        bottom_label.setText("Borrowed books");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        bottom_half.add(bottom_label, gbc);
        button_return = new JButton();
        button_return.setText("Return selected book");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottom_half.add(button_return, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return main_panel;
    }
}
