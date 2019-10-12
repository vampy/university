package chat.client

import java.io.IOException
import java.net.Socket

import chat.client.gui.{LoginDialog, Terminal, TerminalController}
import library.network.rpcprotocol.LibraryServerRPCProxy
import library.services.{Constants, ILibraryServer, LibraryException}

object ClientStartRPC
{
    def main(args: Array[String])
    {
        var DEBUG: Boolean = false
        var debug_username: String = null
        var debug_password: String = null
        if (args.length >= 2)
        {
            DEBUG = true
            debug_username = args(0)
            debug_password = args(1)
        }
        if (DEBUG)
        {
            println("In debug mode")
            var sock: Socket = null
            var doLoop: Boolean = true
            while (doLoop)
            {

                try
                {
                    sock = new Socket(Constants.HOSTNAME, Constants.PORT)
                    if (sock != null)
                    {
                        sock.close()
                        doLoop = false
                    }
                }
                catch
                {
                    case e: IOException =>
                        try
                        {
                            Thread.sleep(100)
                        }
                        catch
                        {
                            case ex: InterruptedException =>
                                Thread.currentThread.interrupt()
                        }
                }

            }
            println("Starting Debug Client")
            val server: ILibraryServer = new LibraryServerRPCProxy(Constants.HOSTNAME, Constants.PORT)
            val ctrl: TerminalController = new TerminalController(server)
            try
            {
                ctrl.login(debug_username, debug_password)
                Terminal.run(ctrl)
            }
            catch
            {
                case ex: LibraryException =>
                    ex.printStackTrace()
                    println("Failed to start debug " + debug_username)
            }
        }
        else
        {
            println("Starting Client")
            val server: ILibraryServer = new LibraryServerRPCProxy(Constants.HOSTNAME, Constants.PORT)
            val ctrl: TerminalController = new TerminalController(server)
            new LoginDialog(ctrl)
        }
    }
}
