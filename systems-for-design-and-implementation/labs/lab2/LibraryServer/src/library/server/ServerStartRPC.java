package library.server;


import library.network.utils.AbstractServer;
import library.network.utils.LibraryRPCConcurrentServer;
import library.network.utils.ServerException;
import library.persistance.repository.IBookRepository;
import library.persistance.repository.IUserRepository;
import library.persistance.repository.jdbc.BookRepositoryJDBC;
import library.persistance.repository.jdbc.UserRepositoryJDBC;
import library.services.Constants;
import library.services.ILibraryServer;
import library.services.impl.LibraryServerImpl;

public class ServerStartRPC
{
    public static void main(String[] args)
    {
        System.out.println("Starting Server");
        IUserRepository userRepository = new UserRepositoryJDBC();
        IBookRepository bookRepository = new BookRepositoryJDBC();

        ILibraryServer libraryServerImpl = new LibraryServerImpl(userRepository, bookRepository);
        AbstractServer server = new LibraryRPCConcurrentServer(Constants.PORT, libraryServerImpl);
        try
        {
            server.start();
        }
        catch (ServerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
