package view;

import controller.Interpreter;
import model.expression.*;
import model.statement.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI
{
    private JPanel      panel_top;
    private JLabel      label_top_menu;
    private JButton     button_input_program;
    private JButton     button_complete_eval;
    private JButton     button_debug_eval;
    private JButton     button_save_to_file;
    private JButton     button_input_default;
    private JButton     button_ser_to_file;
    private JButton     button_ser_from_file;
    private JButton     button_exit;
    private Interpreter interpreter;

    public GUI(final Interpreter interpreter)
    {
        this.interpreter = interpreter;

        JFrame frame = new JFrame("--- Toy interpreter ---");
        frame.setContentPane(panel_top);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        initHooks();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void run(final Interpreter interpreter)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    GUI gui = new GUI(interpreter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    public static String showInputDialog(Component parent, String messsage)
    {
        String[] options = {"OK"};
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel(messsage);
        JTextField txt = new JTextField(10);
        panel.add(lbl);
        panel.add(txt);

        JOptionPane.showOptionDialog(parent, panel, "Input", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        String text = txt.getText().trim();
        if (text.length() > 0)
        {
            return text;
        }

        return null;
    }

    public static void showErrorDialog(Component parent, String message)
    {
        JOptionPane.showMessageDialog(parent, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccessDialog(Component parent, String message)
    {
        JOptionPane.showMessageDialog(parent, message, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String getString(Component parent, String message)
    {
        String text = null;
        while (text == null)
        {
            text = showInputDialog(parent, message);
        }

        return text;
    }

    public static int getInt(Component parent, String message)
    {
        while (true)
        {
            String text = getString(parent, message);
            try
            {
                return Integer.parseInt(text);
            }
            catch (NumberFormatException e)
            {
                showErrorDialog(parent, "Input is not a number, please try again");
            }
        }
    }

    private void initHooks()
    {
        button_input_program.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed input program");

                interpreter.setMain(getStatement());
            }
        });
        button_complete_eval.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed complete eval");
                if (!interpreter.startMain())
                {
                    showErrorDialog(panel_top, "No program was loaded");
                    return;
                }
                interpreter.completeEval();
                String output = "Output: \n" + interpreter.getOutput() + "\n\n" + interpreter.getRepositoryString();
                showSuccessDialog(panel_top, output);
                interpreter.stopMain();
            }
        });
        button_debug_eval.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed debug eval");

                if (!interpreter.startMain())
                {
                    showErrorDialog(panel_top, "No program was loaded");
                    return;
                }

                GUIDebugDialog dialog = new GUIDebugDialog(interpreter);
            }
        });
        button_input_default.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed input default");
//                interpreter.setMain(new CompStatement(
//                    new CompStatement(
//                        new AssignStatement("nr", new HeapNewExpression(new ConstExpression(355))),
//                        new ForkStatement(new CompStatement(
//                            new AssignStatement("nr_fork", new ConstExpression(0)),
//                            new PrintStatement(new HeapReadExpression("nr_fork"))
//                        ))
//                    ),
//                    new CompStatement(
//                        new AssignStatement("another_nr", new ConstExpression(11)),
//                        new PrintStatement(
//                            new ArithmeticExpression(
//                                new HeapReadExpression("nr"),
//                                new ConstExpression(100),
//                                Operation.SUBTRACT
//                            )
//                        )
//                    )
//                ));

                 /*
                 v=new(read());
                 print(r(v));
                 fork(wh(v,1+read());

                 print(r(v));
                 fork(wh(v,20));

                 print(r(v)));
                 print(r(v)+1)
                */

                interpreter.setMain(new CompStatement(
                    new CompStatement(
                        new AssignStatement("v", new HeapNewExpression(new ReadExpression())), // v = new(read())
                        new CompStatement(
                            new PrintStatement(new HeapReadExpression("v")), // print(r(v))
                            new ForkStatement( // fork(wh(v, 1 + read());
                                new HeapWriteStatement(
                                    "v",
                                    new ArithmeticExpression(new ConstExpression(1), new ReadExpression(), Operation.ADD)
                                )
                            )
                        )
                    ),
                    new CompStatement(
                        new CompStatement(
                            new PrintStatement(new HeapReadExpression("v")), // print(r(v))
                            new ForkStatement( // fork(wh(v, 20));
                                new HeapWriteStatement(
                                    "v",
                                    new ConstExpression(20)
                                )
                            )
                        ),
                        new CompStatement(
                            new PrintStatement(new HeapReadExpression("v")), // print(r(v))
                            new PrintStatement( // print(r(v) + 1)
                                new ArithmeticExpression(new HeapReadExpression("v"), new ConstExpression(1), Operation.ADD)
                            )
                        )
                    )
                ));
                showSuccessDialog(panel_top, "Program added");
            }
        });
        button_ser_to_file.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed serialize to file");
                if (interpreter.serializeToFile())
                {
                    showSuccessDialog(panel_top, "Serialized to file.");
                }
                else
                {
                    showErrorDialog(panel_top, "Failed to serialize to file");
                }
            }
        });
        button_ser_from_file.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed serialize from file");
                if (interpreter.serializeFromFile())
                {
                    showSuccessDialog(panel_top, "Serialized from file.");
                }
                else
                {
                    showErrorDialog(panel_top, "Failed to serialize from file");
                }
            }
        });
        button_save_to_file.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed save to file");

                if (interpreter.saveToFile())
                {
                    showSuccessDialog(panel_top, "Saved to file");
                }
                else
                {
                    showErrorDialog(panel_top, "Failed to save to file");
                }
            }
        });
        button_exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
    }

    private Statement getStatement()
    {
        GUIStatementDialog dialog = new GUIStatementDialog();

        return dialog.getStatement();
    }
}
