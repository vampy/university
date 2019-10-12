package tictactoe.client.gui;

import tictactoe.model.Move;
import tictactoe.model.Opponent;
import tictactoe.services.TicTacToeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Terminal
{
    private static final boolean DEBUG = true;
    private TerminalController controller;
    public  JPanel             main_panel;
    public  JTable             tableUser;
    public  JPanel             bottom_half;
    public  JPanel             top_half;
    public  JLabel             bottom_label;
    public  JButton            button1;
    public  JButton            button2;
    public  JButton            button3;
    public  JButton            button4;
    public  JButton            button5;
    public  JButton            button6;
    public  JButton            button7;
    public  JButton            button8;
    public  JButton            button9;
    public  JButton            buttonStartGame;
    public  JLabel             fieldWaiting;
    public  Opponent           opponent;
    public String mark;

    private ArrayList<JButton> buttons;

    public Opponent getOpponent()
    {
        return opponent;
    }

    public void setOpponent(Opponent opponent)
    {
        this.opponent = opponent;
        mark = opponent.getMark().equals("X") ? "O" : "X";
        fieldWaiting.setText(String.format("Opponent_id = %d, mark = %s", opponent.getId(), opponent.getMark()));

        // we are X, we start first
        if (opponent.getMark().equals("O"))
        {
            enableButtons();
        }
    }

    private void enableButtons()
    {
        for (JButton button : buttons)
        {
            button.setEnabled(true);
        }
    }

    private void disableButtons(boolean text)
    {
        for (JButton button : buttons)
        {
            if (text)
            {
                button.setText("_");
            }
            button.setEnabled(false);
        }
    }

    private void buttonPressed(int index)
    {
        System.out.println("Button pressed " + index);
        if (!buttons.get(index).getText().equals("_"))
        {
            showErrorDialog(main_panel, "You can't set an already set value");
            return;
        }

        try
        {
            buttons.get(index).setText(mark);
            disableButtons(false);
            controller.move(index);
        }
        catch (TicTacToeException e)
        {
            e.printStackTrace();
        }
    }

    public void gridUpdated(Move move)
    {
        buttons.get(move.getCell()).setText(move.is_x() ? "X" : "O");
        enableButtons();
    }

    public void gameFinished(int winner_id)
    {
        if (winner_id == 0)
        {
            showSuccessDialog(main_panel, "DRAW");
        }
        else if (winner_id == controller.getUser().getId())
        {
            showSuccessDialog(main_panel, "YOU WIN");
        }
        else
        {
            showSuccessDialog(main_panel, "YOU LOST :(");
        }

        disableButtons(false);
    }

    public Terminal(final TerminalController controller)
    {
        this.controller = controller;
        final Terminal self = this;
        controller.setTerminal(this);

        // add buttons
        buttons = new ArrayList<>(9);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);
        buttons.add(button8);
        buttons.add(button9);

        int id = 0;
        for (JButton button : buttons)
        {
            final int hack = id;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    self.buttonPressed(hack);
                }
            });
            id++;
        }

        // disable all buttons
        disableButtons(true);

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

        buttonStartGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonStartGame.setEnabled(false);
                self.controller.startGame();
            }
        });
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
        top_half = new JPanel();
        top_half.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        main_panel.add(top_half, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        top_half.add(scrollPane1, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        scrollPane1.setViewportView(panel1);
        button1 = new JButton();
        button1.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(button1, gbc);
        button2 = new JButton();
        button2.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button2, gbc);
        button3 = new JButton();
        button3.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button3, gbc);
        button4 = new JButton();
        button4.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button4, gbc);
        button5 = new JButton();
        button5.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button5, gbc);
        button6 = new JButton();
        button6.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button6, gbc);
        button7 = new JButton();
        button7.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button7, gbc);
        button8 = new JButton();
        button8.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button8, gbc);
        button9 = new JButton();
        button9.setText("Button");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button9, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Play X/O Game");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        top_half.add(label1, gbc);
        fieldWaiting = new JLabel();
        fieldWaiting.setText("Waiting for players!");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        top_half.add(fieldWaiting, gbc);
        bottom_half = new JPanel();
        bottom_half.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
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
        bottom_label.setText("All games played");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        bottom_half.add(bottom_label, gbc);
        buttonStartGame = new JButton();
        buttonStartGame.setText("Start Game");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        main_panel.add(buttonStartGame, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return main_panel;
    }
}
