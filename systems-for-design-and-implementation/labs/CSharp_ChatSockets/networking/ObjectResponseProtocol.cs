using System;
namespace chat.network.protocol
{
	using UserDTO = chat.network.dto.UserDTO;
	using MessageDTO = chat.network.dto.MessageDTO;

	public interface Response 
	{
	}

	[Serializable]
	public class OkResponse : Response
	{
		
	}

    [Serializable]
	public class ErrorResponse : Response
	{
		private string message;

		public ErrorResponse(string message)
		{
			this.message = message;
		}

		public virtual string Message
		{
			get
			{
				return message;
			}
		}
	}

	[Serializable]
	public class GetLoggedFriendsResponse : Response
	{
		private UserDTO[] friends;

		public GetLoggedFriendsResponse(UserDTO[] friends)
		{
			this.friends = friends;
		}

		public virtual UserDTO[] Friends
		{
			get
			{
				return friends;
			}
		}
	}
	public interface UpdateResponse : Response
	{
	}

	[Serializable] 
	public class FriendLoggedInResponse : UpdateResponse
	{
		private UserDTO friend;

		public FriendLoggedInResponse(UserDTO friend)
		{
			this.friend = friend;
		}

		public virtual UserDTO Friend
		{
			get
			{
				return friend;
			}
		}
	}

	[Serializable]
	public class FriendLoggedOutResponse : UpdateResponse
	{
		private UserDTO friend;

		public FriendLoggedOutResponse(UserDTO friend)
		{
			this.friend = friend;
		}

		public virtual UserDTO Friend
		{
			get
			{
				return friend;
			}
		}
	}


	[Serializable]
	public class NewMessageResponse : UpdateResponse
	{
		private MessageDTO message;

		public NewMessageResponse(MessageDTO message)
		{
			this.message = message;
		}

		public virtual MessageDTO Message
		{
			get
			{
				return message;
			}
		}
	}

}