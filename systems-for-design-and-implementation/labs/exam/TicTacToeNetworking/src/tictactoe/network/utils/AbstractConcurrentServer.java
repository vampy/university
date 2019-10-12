package tictactoe.network.utils;

import java.net.Socket;

/**
 * Wrapper around Threaded Socket
 */
public abstract class AbstractConcurrentServer extends AbstractServer
{
    public AbstractConcurrentServer(int port)
    {
        super(port);
        System.out.println("Concurrent AbstractServer");
    }

    protected void processRequest(Socket client)
    {
        Thread tw = createWorker(client);
        tw.start();
    }

    protected abstract Thread createWorker(Socket client);
}