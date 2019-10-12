package library.network.utils;

import library.network.rpcprotocol.LibraryClientRPCWorker;
import library.services.ILibraryServer;

import java.net.Socket;

public class LibraryRPCConcurrentServer extends AbstractConcurrentServer
{
    private ILibraryServer libraryServer;

    public LibraryRPCConcurrentServer(int port, ILibraryServer libraryServer)
    {
        super(port);
        this.libraryServer = libraryServer;
        System.out.println("LibraryRPCConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client)
    {
        LibraryClientRPCWorker worker = new LibraryClientRPCWorker(libraryServer, client);
        return new Thread(worker);
    }
}
