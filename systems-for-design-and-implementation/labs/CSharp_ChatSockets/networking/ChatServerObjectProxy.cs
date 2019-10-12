using System;
using System.Collections.Generic;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using chat.services;
using chat.model;
using chat.network.dto;
using chat.network.protocol;
namespace chat.network.server
{
	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:36:34 PM </summary>
	/// 
	public class ChatServerProxy : IChatServer
	{
		private string host;
		private int port;

		private IChatObserver client;

		private NetworkStream stream;
		
        private IFormatter formatter;
		private TcpClient connection;

		private Queue<Response> responses;
		private volatile bool finished;
        private EventWaitHandle _waitHandle;
		public ChatServerProxy(string host, int port)
		{
			this.host = host;
			this.port = port;
			responses=new Queue<Response>();
		}

		public virtual void login(User user, IChatObserver client)
		{
			initializeConnection();
			UserDTO udto = DTOUtils.getDTO(user);
			sendRequest(new LoginRequest(udto));
			Response response =readResponse();
			if (response is OkResponse)
			{
				this.client=client;
				return;
			}
			if (response is ErrorResponse)
			{
				ErrorResponse err =(ErrorResponse)response;
				closeConnection();
				throw new ChatException(err.Message);
			}
		}

		public virtual void sendMessage(Message message)
		{
			MessageDTO mdto =DTOUtils.getDTO(message);
			sendRequest(new SendMessageRequest(mdto));
			Response response =readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err =(ErrorResponse)response;
				throw new ChatException(err.Message);
			}
		}

	public virtual void logout(User user, IChatObserver client)
		{
			UserDTO udto =DTOUtils.getDTO(user);
			sendRequest(new LogoutRequest(udto));
			Response response =readResponse();
			closeConnection();
			if (response is ErrorResponse)
			{
				ErrorResponse err =(ErrorResponse)response;
				throw new ChatException(err.Message);
			}
		}



		public virtual User[] getLoggedFriends(User user)
		{
			UserDTO udto =DTOUtils.getDTO(user);
			sendRequest(new GetLoggedFriendsRequest(udto));
			Response response =readResponse();
			if (response is ErrorResponse)
			{
				ErrorResponse err =(ErrorResponse)response;
				throw new ChatException(err.Message);
			}
			GetLoggedFriendsResponse resp =(GetLoggedFriendsResponse)response;
			UserDTO[] frDTO =resp.Friends;
			User[] friends =DTOUtils.getFromDTO(frDTO);
			return friends;
		}

		private void closeConnection()
		{
			finished=true;
			try
			{
				stream.Close();
				//output.close();
				connection.Close();
                _waitHandle.Close();
				client=null;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}

		}

		private void sendRequest(Request request)
		{
			try
			{
                formatter.Serialize(stream, request);
                stream.Flush();
			}
			catch (Exception e)
			{
				throw new ChatException("Error sending object "+e);
			}

		}

		private Response readResponse()
		{
			Response response =null;
			try
			{
                _waitHandle.WaitOne();
				lock (responses)
				{
                    //Monitor.Wait(responses); 
                    response = responses.Dequeue();
                
				}
				

			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
			return response;
		}
		private void initializeConnection()
		{
			 try
			 {
				connection=new TcpClient(host,port);
				stream=connection.GetStream();
                formatter = new BinaryFormatter();
				finished=false;
                _waitHandle = new AutoResetEvent(false);
				startReader();
			}
			catch (Exception e)
			{
                Console.WriteLine(e.StackTrace);
			}
		}
		private void startReader()
		{
			Thread tw =new Thread(run);
			tw.Start();
		}


		private void handleUpdate(UpdateResponse update)
		{
			if (update is FriendLoggedInResponse)
			{

				FriendLoggedInResponse frUpd =(FriendLoggedInResponse)update;
				User friend =DTOUtils.getFromDTO(frUpd.Friend);
				Console.WriteLine("Friend logged in "+friend);
				try
				{
					client.friendLoggedIn(friend);
				}
				catch (ChatException e)
				{
                    Console.WriteLine(e.StackTrace); 
				}
			}
			if (update is FriendLoggedOutResponse)
			{
				FriendLoggedOutResponse frOutRes =(FriendLoggedOutResponse)update;
				User friend =DTOUtils.getFromDTO(frOutRes.Friend);
				Console.WriteLine("Friend logged out "+friend);
				try
				{
					client.friendLoggedOut(friend);
				}
				catch (ChatException e)
				{
                    Console.WriteLine(e.StackTrace);
				}
			}

			if (update is NewMessageResponse)
			{
				NewMessageResponse msgRes =(NewMessageResponse)update;
				Message message =DTOUtils.getFromDTO(msgRes.Message);
				try
				{
					client.messageReceived(message);
				}
				catch (ChatException e)
				{
                    Console.WriteLine(e.StackTrace);
				}
			}
		}
		public virtual void run()
			{
				while(!finished)
				{
					try
					{
                        object response = formatter.Deserialize(stream);
						Console.WriteLine("response received "+response);
						if (response is UpdateResponse)
						{
							 handleUpdate((UpdateResponse)response);
						}
						else
						{
							
							lock (responses)
							{
                                					
								 
                                responses.Enqueue((Response)response);
                               
							}
                            _waitHandle.Set();
						}
					}
					catch (Exception e)
					{
						Console.WriteLine("Reading error "+e);
					}
					
				}
			}
		//}
	}

}