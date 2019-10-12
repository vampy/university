package chat.network.utils;

import chat.network.rpcprotocol.ChatClientRpcWorker;
import chat.services.IChatServer;

import java.net.Socket;

/**
 * Created by grigo on 2/25/16.
 */
public class ChatRpcConcurrentServer extends AbsConcurrentServer
{
    private IChatServer chatServer;

    public ChatRpcConcurrentServer(int port, IChatServer chatServer)
    {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client)
    {
        ChatClientRpcWorker worker = new ChatClientRpcWorker(chatServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
