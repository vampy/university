package view;

import controller.Interpreter;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;

public class GUIDebugDialog extends JDialog
{
    public static int step = 1;
    private JPanel      contentPane;
    private JButton     buttonCancel;
    private JTextArea   text_output;
    private JButton     button_all;
    private JButton     button_reload;
    private JButton     button_next;
    private Interpreter interpreter;

    public GUIDebugDialog(Interpreter interpreter)
    {
        this.interpreter = interpreter;
        setContentPane(contentPane);
        setModal(true);
        //pack();
        setSize(1280, 720);
        setTitle("Debug Eval");
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonCancel);

        text_output.setTabSize(4);

        initHooks();
        setVisible(true);
    }

    private void initHooks()
    {
        buttonCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        });
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        button_all.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed debug all");

                int step = 1;
                String output = "";
                while (interpreter.debugEval())
                {
                    output += String.format("Program State after %d step(s):\n %s \n\n", step, interpreter.getOutput());
                    step++;
                }
                if (step > 1)
                {
                    text_output.setText(output);
                }

                GUI.showSuccessDialog(contentPane, "Program finished...");
                saveToFile("debug.txt");
            }
        });
        button_next.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed debug next");

                if (interpreter.debugEval())
                {
                    String output = String.format("Program State after %d step(s):\n %s \n", step, interpreter.getOutput());
                    step++;
                    text_output.setText(text_output.getText() + "\n" + output);
                }
                else
                {
                    GUI.showSuccessDialog(contentPane, "Program finished...");
                    step = 1;
                    saveToFile("debug.txt");
                }
            }
        });
        button_reload.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed debug reload");

                interpreter.stopMain();
                if (!interpreter.startMain())
                {
                    GUI.showErrorDialog(contentPane, "Something went wrong, reloading the program.");
                    return;
                }
                step = 1;
                GUI.showSuccessDialog(contentPane, "Program reloaded...");
                text_output.setText("");
            }
        });
    }

    private void onCancel()
    {
        System.out.println("Pressed debug cancel");
        dispose();
        step = 1;
        interpreter.stopMain(); // execute all
    }

    protected void saveToFile(String filename)
    {
        try
        {
            PrintWriter writer = new PrintWriter(filename);

            writer.println(text_output.getText());

            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("ERROR: could not write: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
