using System;
using System.Collections;
using System.Collections.Generic;
using System.Net.Sockets;

using System.Text;
using System.Threading;
using chat.persistence.repository;
using chat.persistence.repository.mock;
using chat.persistence;
using chat.services;
using chat.network.client;
using ServerTemplate;
namespace chat
{
    using server;
    class StartServer
    {
        static void Main(string[] args)
        {
            
            IUserRepository userRepo = new UserRepositoryMock();
            IChatServer serviceImpl = new ChatServerImpl(userRepo);

            //IChatServer serviceImpl = new ChatServerImpl();
			SerialChatServer server = new SerialChatServer("127.0.0.1", 55555, serviceImpl);
            server.Start();
            Console.WriteLine("Server started ...");
            //Console.WriteLine("Press <enter> to exit...");
            Console.ReadLine();
            
        }
    }

    public class SerialChatServer: ConcurrentServer 
    {
        private IChatServer server;
        private ChatClientWorker worker;
        public SerialChatServer(string host, int port, IChatServer server) : base(host, port)
            {
                this.server = server;
                Console.WriteLine("SerialChatServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new ChatClientWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }
    
}
