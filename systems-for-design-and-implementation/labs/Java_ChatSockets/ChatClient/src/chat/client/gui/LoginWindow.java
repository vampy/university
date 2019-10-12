package chat.client.gui;

import chat.services.ChatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 3:32:06 PM
 */
public class LoginWindow extends JFrame
{
    private JTextField     userId;
    private JPasswordField passwd;
    private JButton        logBut, cancelBut;
    private ChatClientCtrl ctrl;

    public LoginWindow(String title, ChatClientCtrl ctrl) throws HeadlessException
    {
        super(title);
        this.ctrl = ctrl;
        getContentPane().add(createLogin());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel createLogin()
    {
        JPanel res = new JPanel(new GridLayout(3, 1));
        JPanel line1 = new JPanel();
        line1.add(new JLabel("User id:"));
        line1.add(userId = new JTextField(15));
        res.add(line1);

        JPanel line2 = new JPanel();
        line2.add(new JLabel("Password:"));
        line2.add(passwd = new JPasswordField(15));
        res.add(line2);

        JPanel line3 = new JPanel();
        line3.add(logBut = new JButton("Login"));
        line3.add(cancelBut = new JButton("Clear"));
        ActionListener al = new ButListener();
        logBut.addActionListener(al);
        cancelBut.addActionListener(al);
        res.add(line3);
        return res;
    }

    private class ButListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == logBut)
            {
                System.out.println("Login button pressed.");
                String user = userId.getText();
                String pass = new String(passwd.getPassword());
                try
                {
                    ctrl.login(user, pass);
                    ChatWindow win = new ChatWindow("Chat window for " + user, ctrl);
                    win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    win.setSize(500, 400);
                    win.setVisible(true);
                    LoginWindow.this.dispose();
                }
                catch (ChatException e1)
                {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Login error " + e1, "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            else
            {
                System.out.println("Clear button pressed.");
                userId.setText("");
                passwd.setText("");
            }
        }
    }
}
