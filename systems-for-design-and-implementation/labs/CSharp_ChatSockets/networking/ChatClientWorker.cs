using System;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using chat.services;
using chat.model;
using chat.network.dto;
using chat.network.protocol;
namespace chat.network.client
{

	
	
	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:04:43 PM </summary>
	/// 
	public class ChatClientWorker :  IChatObserver //, Runnable
	{
		private IChatServer server;
		private TcpClient connection;

		private NetworkStream stream;
		private IFormatter formatter;
		private volatile bool connected;
		public ChatClientWorker(IChatServer server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{
				
				stream=connection.GetStream();
                formatter = new BinaryFormatter();
				connected=true;
			}
			catch (Exception e)
			{
                Console.WriteLine(e.StackTrace);
			}
		}

		public virtual void run()
		{
			while(connected)
			{
				try
				{
                    object request = formatter.Deserialize(stream);
					object response =handleRequest((Request)request);
					if (response!=null)
					{
					   sendResponse((Response) response);
					}
				}
				catch (Exception e)
				{
                    Console.WriteLine(e.StackTrace);
				}
				
				try
				{
					Thread.Sleep(1000);
				}
				catch (Exception e)
				{
                    Console.WriteLine(e.StackTrace);
				}
			}
			try
			{
				stream.Close();
				connection.Close();
			}
			catch (Exception e)
			{
				Console.WriteLine("Error "+e);
			}
		}
		public virtual void messageReceived(Message message)
		{
			MessageDTO mdto = DTOUtils.getDTO(message);
			Console.WriteLine("Message received  "+message);
			try
			{
				sendResponse(new NewMessageResponse(mdto));
			}
			catch (Exception e)
			{
				throw new ChatException("Sending error: "+e);
			}
		}

	public virtual void friendLoggedIn(User friend)
		{
			UserDTO udto =DTOUtils.getDTO(friend);
			Console.WriteLine("Friend logged in "+friend);
			try
			{
				sendResponse(new FriendLoggedInResponse(udto));
			}
			catch (Exception e)
			{
                Console.WriteLine(e.StackTrace);
			}
		}
		public virtual void friendLoggedOut(User friend)
		{
			UserDTO udto =DTOUtils.getDTO(friend);
			Console.WriteLine("Friend logged out "+friend);
			try
			{
				sendResponse(new FriendLoggedOutResponse(udto));
			}
			catch (Exception e)
			{
                Console.WriteLine(e.StackTrace);
			}
		}

		private Response handleRequest(Request request)
		{
			Response response =null;
			if (request is LoginRequest)
			{
				Console.WriteLine("Login request ...");
				LoginRequest logReq =(LoginRequest)request;
				UserDTO udto =logReq.User;
				User user =DTOUtils.getFromDTO(udto);
				try
                {
                    lock (server)
                    {
                        server.login(user, this);
                    }
					return new OkResponse();
				}
				catch (ChatException e)
				{
					connected=false;
					return new ErrorResponse(e.Message);
				}
			}
			if (request is LogoutRequest)
			{
				Console.WriteLine("Logout request");
				LogoutRequest logReq =(LogoutRequest)request;
				UserDTO udto =logReq.User;
				User user =DTOUtils.getFromDTO(udto);
				try
				{
                    lock (server)
                    {

                        server.logout(user, this);
                    }
					connected=false;
					return new OkResponse();

				}
				catch (ChatException e)
				{
				   return new ErrorResponse(e.Message);
				}
			}
			if (request is SendMessageRequest)
			{
				Console.WriteLine("SendMessageRequest ...");
				SendMessageRequest senReq =(SendMessageRequest)request;
				MessageDTO mdto =senReq.Message;
				Message message =DTOUtils.getFromDTO(mdto);
				try
				{
                    lock (server)
                    {
                        server.sendMessage(message);
                    }
                        return new OkResponse();
				}
				catch (ChatException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is GetLoggedFriendsRequest)
			{
				Console.WriteLine("GetLoggedFriends Request ...");
				GetLoggedFriendsRequest getReq =(GetLoggedFriendsRequest)request;
				UserDTO udto =getReq.User;
				User user =DTOUtils.getFromDTO(udto);
				try
				{
                    User[] friends;
                    lock (server)
                    {

                         friends = server.getLoggedFriends(user);
                    }
					UserDTO[] frDTO =DTOUtils.getDTO(friends);
					return new GetLoggedFriendsResponse(frDTO);
				}
				catch (ChatException e)
				{
					return new ErrorResponse(e.Message);
				}
			}
			return response;
		}

	private void sendResponse(Response response)
		{
			Console.WriteLine("sending response "+response);
            formatter.Serialize(stream, response);
            stream.Flush();
			
		}
	}

}