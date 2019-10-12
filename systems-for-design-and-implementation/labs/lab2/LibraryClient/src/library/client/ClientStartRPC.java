package library.client;


import library.client.gui.LoginDialog;
import library.client.gui.Terminal;
import library.client.gui.TerminalController;
import library.network.rpcprotocol.LibraryServerRPCProxy;
import library.services.Constants;
import library.services.ILibraryServer;
import library.services.LibraryException;

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
            ILibraryServer server = new LibraryServerRPCProxy(Constants.HOSTNAME, Constants.PORT);
            TerminalController ctrl = new TerminalController(server);

            try
            {
                ctrl.login(debug_username, debug_password);
                Terminal.run(ctrl);
            }
            catch (LibraryException ex)
            {
                ex.printStackTrace();
                System.out.println("Failed to start debug " + debug_username);
            }
        }
        else
        {
            System.out.println("Starting Client");
            ILibraryServer server = new LibraryServerRPCProxy(Constants.HOSTNAME, Constants.PORT);
            TerminalController ctrl = new TerminalController(server);

            new LoginDialog(ctrl);
        }
    }
}
