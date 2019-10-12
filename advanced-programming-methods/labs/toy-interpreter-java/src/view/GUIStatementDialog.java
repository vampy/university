package view;

import model.statement.*;

import javax.swing.*;
import java.awt.event.*;

public class GUIStatementDialog extends JDialog
{
    private JPanel    contentPane;
    private JButton   buttonOK;
    private JButton   button_compound;
    private JButton   button_fork;
    private JButton   button_assign;
    private JButton   button_print;
    private JButton   button_if;
    private JLabel    label_value;
    private JButton   button_heap_write;
    private Statement statement;

    public GUIStatementDialog()
    {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Get Statement");
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        initHooks();

        setVisible(true);
    }

    /**
     * Getter for property 'statement'.
     *
     * @return Value for property 'statement'.
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Setter for property 'statement'.
     *
     * @param statement Value to set for property 'statement'.
     */
    public void setStatement(Statement statement)
    {
        this.statement = statement;
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


        button_compound.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed compound");

                GUIStatementDialog first = new GUIStatementDialog();
                GUIStatementDialog second = new GUIStatementDialog();

                statement = new CompStatement(first.getStatement(), second.getStatement());
                label_value.setText(statement.toString());
            }
        });
        button_assign.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed assign");

                String varname = GUI.getString(contentPane, "var name = ");
                GUIExpressionDialog dialog = new GUIExpressionDialog();

                statement = new AssignStatement(varname, dialog.getExpression());
                label_value.setText(statement.toString());
            }
        });
        button_print.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed print");

                GUIExpressionDialog dialog = new GUIExpressionDialog();

                statement = new PrintStatement(dialog.getExpression());
                label_value.setText(statement.toString());
            }
        });
        button_if.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed if");

                GUIExpressionDialog dialog1 = new GUIExpressionDialog();
                GUIStatementDialog dialog2 = new GUIStatementDialog();
                GUIStatementDialog dialog3 = new GUIStatementDialog();

                statement = new IfStatement(dialog1.getExpression(), dialog2.getStatement(), dialog3.getStatement());
                label_value.setText(statement.toString());
            }
        });
        button_fork.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed fork");

                GUIStatementDialog dialog = new GUIStatementDialog();

                statement = new ForkStatement(dialog.getStatement());
                label_value.setText(statement.toString());
            }
        });
        button_heap_write.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Pressed heap write");

                String varname = GUI.getString(contentPane, "var name = ");
                GUIExpressionDialog expression = new GUIExpressionDialog();

                statement = new HeapWriteStatement(varname, expression.getExpression());
                label_value.setText(statement.toString());
            }
        });
    }

    private void onOK()
    {
        System.out.println("Pressed ok");

        if (statement == null)
        {
            GUI.showErrorDialog(contentPane, "Did not select any statement");
            return;
        }

        dispose();
    }
}
