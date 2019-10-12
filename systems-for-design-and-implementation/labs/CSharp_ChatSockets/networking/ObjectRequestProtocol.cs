using System;
namespace chat.network.protocol
{
	using UserDTO = chat.network.dto.UserDTO;
	using MessageDTO = chat.network.dto.MessageDTO;


	public interface Request 
	{
	}


	[Serializable]
	public class LoginRequest : Request
	{
		private UserDTO user;

		public LoginRequest(UserDTO user)
		{
			this.user = user;
		}

		public virtual UserDTO User
		{
			get
			{
				return user;
			}
		}
	}

	[Serializable]
	public class LogoutRequest : Request
	{
		private UserDTO user;

		public LogoutRequest(UserDTO user)
		{
			this.user = user;
		}

		public virtual UserDTO User
		{
			get
			{
				return user;
			}
		}
	}

	[Serializable]
	public class SendMessageRequest : Request
	{
		private MessageDTO message;

		public SendMessageRequest(MessageDTO message)
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

	[Serializable]
	public class GetLoggedFriendsRequest : Request
	{
		private UserDTO user;

		public GetLoggedFriendsRequest(UserDTO user)
		{
			this.user = user;
		}

		public virtual UserDTO User
		{
			get
			{
				return user;
			}
		}
	}


}