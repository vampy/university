import chat.network.utils.AbstractServer;
import chat.network.utils.ChatRpcConcurrentServer;
import chat.network.utils.ServerException;
import chat.persistence.repository.UserRepository;
import chat.persistence.repository.mock.UserRepositoryMock;
import chat.server.ChatServerImpl;
import chat.services.IChatServer;

/**
 * Created by grigo on 2/25/16.
 */
public class StartRpcServer
{
    public static void main(String[] args)
    {
        UserRepository repo = new UserRepositoryMock();
      /*8 Properties serverProps=new Properties(System.getProperties());
        try {
            serverProps.load(new FileReader("chatserver.properties"));
            System.setProperties(serverProps);

            System.out.println("Properties set. ");
            //System.getProperties().list(System.out);
        } catch (IOException e) {
            System.out.println("Cannot find chatserver.properties "+e);
            return;
        }*/

        IChatServer chatServerImpl = new ChatServerImpl(repo);
        //IChatServer chatServerImpl=new ChatServerImpl();
        AbstractServer server = new ChatRpcConcurrentServer(55555, chatServerImpl);
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
