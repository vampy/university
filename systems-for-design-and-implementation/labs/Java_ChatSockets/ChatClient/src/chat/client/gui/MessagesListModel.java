package chat.client.gui;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 3:00:45 PM
 */
public class MessagesListModel extends AbstractListModel
{
    private List<String> messages;

    public MessagesListModel()
    {
        messages = new ArrayList<String>();
    }

    public int getSize()
    {
        return messages.size();
    }

    public Object getElementAt(int index)
    {
        return messages.get(index);
    }

    public void newMessage(String idSender, String mesg)
    {
        String text = "[" + idSender + "]: " + mesg;
        messages.add(text);
        fireContentsChanged(this, messages.size() - 1, messages.size());
    }
}
