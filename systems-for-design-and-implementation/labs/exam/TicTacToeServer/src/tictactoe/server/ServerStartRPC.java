package tictactoe.server;


import tictactoe.network.utils.AbstractServer;
import tictactoe.network.utils.TicTacToeRPCConcurrentServer;
import tictactoe.network.utils.ServerException;
import tictactoe.persistance.repository.IUserRepository;
import tictactoe.persistance.repository.jdbc.UserRepositoryJDBC;
import tictactoe.services.Constants;
import tictactoe.services.ITicTacToeServer;
import tictactoe.services.impl.TicTacToeServerImpl;

public class ServerStartRPC
{
    public static void main(String[] args)
    {
        System.out.println("Starting Server");
        IUserRepository userRepository = new UserRepositoryJDBC();

        ITicTacToeServer libraryServerImpl = new TicTacToeServerImpl(userRepository);
        AbstractServer server = new TicTacToeRPCConcurrentServer(Constants.PORT, libraryServerImpl);
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
