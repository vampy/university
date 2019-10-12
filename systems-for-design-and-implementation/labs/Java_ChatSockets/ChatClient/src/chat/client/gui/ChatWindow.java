package chat.client.gui;

import chat.services.ChatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 2:52:27 PM
 */
public class ChatWindow extends JFrame
{
    private JList friendsList, messagesList;
    private JTextField     message;
    private JButton        sendButt;
    private ChatClientCtrl ctrl;

    public ChatWindow(String title, ChatClientCtrl ctrl)
    {
        super(title);
        this.ctrl = ctrl;
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createSendMessage(), BorderLayout.SOUTH);
        panel.add(createMessages(), BorderLayout.WEST);
        panel.add(createFriends(), BorderLayout.CENTER);
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });
    }

    private void close()
    {
        ctrl.logout();
    }

    private JPanel createFriends()
    {
        JPanel res = new JPanel(new GridLayout(1, 1));
        friendsList = new JList(ctrl.getFriendsModel());
        JScrollPane scroll = new JScrollPane(friendsList);
        res.add(scroll);
        res.setBorder(BorderFactory.createTitledBorder("Logged in friends"));
        return res;
    }

    private JPanel createMessages()
    {
        JPanel res = new JPanel(new GridLayout(1, 1));
        messagesList = new JList(ctrl.getMessagesModel());
        JScrollPane scroll = new JScrollPane(messagesList);
        res.add(scroll);
        res.setBorder(BorderFactory.createTitledBorder("Messages"));
        return res;
    }

    private JPanel createSendMessage()
    {
        JPanel res = new JPanel(new GridLayout(2, 1));
        JPanel line1 = new JPanel();
        line1.add(new JLabel("Message "));
        line1.add(message = new JTextField(30));
        res.add(line1);
        JPanel line2 = new JPanel();
        line2.add(sendButt = new JButton("Send message"));
        sendButt.addActionListener(new ButListener());
        res.add(line2);
        return res;
    }


    private class ButListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Send button pressed");
            int indexFriend = friendsList.getSelectedIndex();
            if (indexFriend >= 0)
            {
                String txtMsg = message.getText();
                if ("".equals(txtMsg.trim()))
                {
                    JOptionPane.showMessageDialog(ChatWindow.this, "You must enter the message", "Send error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try
                {
                    ctrl.sendMessage(indexFriend, txtMsg);
                }
                catch (ChatException e1)
                {
                    JOptionPane.showMessageDialog(ChatWindow.this, "Error " + e1, "Send error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
            else
            {
                JOptionPane.showMessageDialog(ChatWindow.this, "You must select a friend ", "Send error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

}
