package tictactoe.network.utils;

import tictactoe.network.rpcprotocol.TicTacToeClientRPCWorker;
import tictactoe.services.ITicTacToeServer;

import java.net.Socket;

public class TicTacToeRPCConcurrentServer extends AbstractConcurrentServer
{
    private ITicTacToeServer tictactoeServer;

    public TicTacToeRPCConcurrentServer(int port, ITicTacToeServer tictactoeServer)
    {
        super(port);
        this.tictactoeServer = tictactoeServer;
        System.out.println("TicTacToeRPCConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client)
    {
        TicTacToeClientRPCWorker worker = new TicTacToeClientRPCWorker(tictactoeServer, client);
        return new Thread(worker);
    }
}
