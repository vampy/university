package chat.client;

import chat.client.gui.ChatClientCtrl;
import chat.client.gui.LoginWindow;
import chat.network.rpcprotocol.ChatServerRpcProxy;
import chat.services.IChatServer;

/**
 * Created by grigo on 2/25/16.
 */
public class StartRpcClient
{
    public static void main(String[] args)
    {
        IChatServer server = new ChatServerRpcProxy("localhost", 55555);
        ChatClientCtrl ctrl = new ChatClientCtrl(server);

        LoginWindow logWin = new LoginWindow("Chat XYZ", ctrl);
        logWin.setSize(200, 200);
        logWin.setVisible(true);
    }
}
