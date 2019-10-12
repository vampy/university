package tictactoe.client;


import tictactoe.client.gui.LoginDialog;
import tictactoe.client.gui.Terminal;
import tictactoe.client.gui.TerminalController;
import tictactoe.network.rpcprotocol.TicTacToeServerRPCProxy;
import tictactoe.services.Constants;
import tictactoe.services.ITicTacToeServer;
import tictactoe.services.TicTacToeException;

import java.io.IOException;
import java.net.Socket;

public class ClientStartRPC
{
    public static void main(String[] args)
    {
        boolean DEBUG = false;
        String debug_username = null;
        String debug_password = null;
        if (args.length >= 2) // 1 username + 1 password
        {
            DEBUG = true;
            debug_username = args[0];
            debug_password = args[1];
        }

        if (DEBUG)
        {
            System.out.println("In debug mode");
        }

        if (DEBUG)
        {
            // cuz wait for server
            Socket sock;
            boolean doLoop = true;
            while (doLoop)
            {
                try
                {
                    sock = new Socket(Constants.HOSTNAME, Constants.PORT);
                    if (sock != null)
                    {
                        sock.close();
                        doLoop = false;
                    }
                }
                catch (IOException e)
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            System.out.println("Starting Debug Client");
            ITicTacToeServer server = new TicTacToeServerRPCProxy(Constants.HOSTNAME, Constants.PORT);
            TerminalController ctrl = new TerminalController(server);

            try
            {
                ctrl.login(debug_username, debug_password);
                Terminal.run(ctrl);
            }
            catch (TicTacToeException ex)
            {
                ex.printStackTrace();
                System.out.println("Failed to start debug " + debug_username);
            }
        }
        else
        {
            System.out.println("Starting Client");
            ITicTacToeServer server = new TicTacToeServerRPCProxy(Constants.HOSTNAME, Constants.PORT);
            TerminalController ctrl = new TerminalController(server);

            new LoginDialog(ctrl);
        }
    }
}
