package view;

import model.expression.*;

import javax.swing.*;
import java.awt.event.*;

public class GUIExpressionDialog extends JDialog
{
    private JPanel     contentPane;
    private JButton    buttonOK;
    private JLabel     label_value;
    private JButton    button_const;
    private JButton    button_heap_read;
    private JButton    button_var;
    private JButton    button_arithmetic;
    private JButton    button_heap_new;
    private JButton    button_read;
    private Expression expression;

    public GUIExpressionDialog()
    {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Get Expression");
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        initHooks();

        setVisible(true);
    }

    /**
     * Getter for property 'expression'.
     *
     * @return Value for property 'expression'.
     */
    public Expression getExpression()
    {
        return expression;
    }

    /**
     * Setter for property 'expression'.
     *
     * @param expression Value to set for property 'expression'.
     */
    public void setExpression(Expression expression)
    {
        this.expression = expression;
    }

    private void initHooks()
    {
        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onOK();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        button_const.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed const");

                expression = new ConstExpression(GUI.getInt(contentPane, "const value = "));
                label_value.setText(expression.toString());
            }
        });
        button_var.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed var");

                expression = new VarExpression(GUI.getString(contentPane, "var name = "));
                label_value.setText(expression.toString());
            }
        });
        button_arithmetic.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed arithmetic");

                GUIExpressionDialog left = new GUIExpressionDialog();

                // get operator
                Object[] selectionValues = {"Add", "Subtract", "Divide", "Multiply"};
                String initialSelection = "Add";
                Object selection = JOptionPane.showInputDialog(
                    contentPane,
                    "Type of operation?",
                    "left <operation> right",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    selectionValues,
                    initialSelection
                );

                int operator = Operation.ADD;
                if (selection.equals("Add"))
                {
                    operator = Operation.ADD;
                }
                else if (selection.equals("Subtract"))
                {
                    operator = Operation.SUBTRACT;
                }
                else if (selection.equals("Divide"))
                {
                    operator = Operation.DIVIDE;
                }
                else if (selection.equals("Multiply"))
                {
                    operator = Operation.MULTIPLY;
                }
                else
                {
                    System.out.println("ERROR: No operator matches. This should never happen");
                }

                GUIExpressionDialog right = new GUIExpressionDialog();

                expression = new ArithmeticExpression(left.getExpression(), right.getExpression(), operator);
                label_value.setText(expression.toString());
            }
        });
        button_heap_new.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed heap new");

                GUIExpressionDialog val = new GUIExpressionDialog();

                expression = new HeapNewExpression(val.getExpression());
                label_value.setText(expression.toString());
            }
        });
        button_heap_read.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed heap read");

                expression = new HeapReadExpression(GUI.getString(contentPane, "pointer name = "));
                label_value.setText(expression.toString());
            }
        });
        button_read.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed read from console");

                expression = new ReadExpression();
                label_value.setText(expression.toString());
            }
        });

    }

    private void onOK()
    {
        System.out.println("Pressed ok");

        if (expression == null)
        {
            GUI.showErrorDialog(contentPane, "Did not select any expression");
            return;
        }

        dispose();
    }
}
