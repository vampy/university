package chat.client;

import chat.client.gui.ChatClientCtrl;
import chat.client.gui.LoginWindow;
import chat.network.objectprotocol.ChatServerObjectProxy;
import chat.services.IChatServer;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 3:24:40 PM
 */
public class StartObjectClient
{
    public static void main(String[] args)
    {
        IChatServer server = new ChatServerObjectProxy("localhost", 55555);
        ChatClientCtrl ctrl = new ChatClientCtrl(server);


        LoginWindow logWin = new LoginWindow("Chat XYZ", ctrl);
        logWin.setSize(200, 200);
        logWin.setVisible(true);
    }
}
