package tictactoe.client.gui;

import tictactoe.services.TicTacToeException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog
{

    private JTextField         tfUsername;
    private JPasswordField     pfPassword;
    private JLabel             lbUsername;
    private JLabel             lbPassword;
    private JButton            btnLogin;
    private JButton            btnCancel;
    private boolean            succeeded;
    private TerminalController ctrl;


    public LoginDialog(final TerminalController ctrl)
    {
        this.ctrl = ctrl;

        //
        JPanel panel = new JPanel(new GridBagLayout());
//        panel.setPreferredSize(new Dimension(640, 480));
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                System.out.println("Login button pressed.");
                try
                {
                    ctrl.login(getUsername(), getPassword());
                    Terminal.run(ctrl);
                    LoginDialog.this.dispose();
                }
                catch (TicTacToeException ex)
                {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                        "Invalid username or password. Details: " + ex.getMessage(),
                        "Login",
                        JOptionPane.ERROR_MESSAGE);
                    // reset password
                    pfPassword.setText("");
                }
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public String getUsername()
    {
        return tfUsername.getText().trim();
    }

    public String getPassword()
    {
        return new String(pfPassword.getPassword());
    }
}
