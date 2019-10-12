namespace chat.network.dto
{

	using User = chat.model.User;
	using Message = chat.model.Message;

	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 20, 2009
	/// * Time: 8:07:36 AM </summary>
	/// 
	public class DTOUtils
	{
		public static User getFromDTO(UserDTO usdto)
		{
			string id =usdto.Id;
			string pass =usdto.Passwd;
			return new User(id, pass);

		}
		public static UserDTO getDTO(User user)
		{
			string id =user.Id;
			string pass =user.Password;
			return new UserDTO(id, pass);
		}

		public static Message getFromDTO(MessageDTO mdto)
		{
			User sender =new User(mdto.SenderId);
			User receiver =new User(mdto.ReceiverId);
			string text =mdto.Text;
			return new Message(sender,  receiver, text);

		}

		public static MessageDTO getDTO(Message message)
		{
			string senderId =message.Sender.Id;
			string receiverId =message.Receiver.Id;
			string txt =message.Text;
			return new MessageDTO(senderId, txt, receiverId);
		}

		public static UserDTO[] getDTO(User[] users)
		{
			UserDTO[] frDTO =new UserDTO[users.Length];
			for(int i=0;i<users.Length;i++)
			{
				frDTO[i]=getDTO(users[i]);
			}
			return frDTO;
		}

		public static User[] getFromDTO(UserDTO[] users)
		{
			User[] friends =new User[users.Length];
			for(int i=0;i<users.Length;i++)
			{
				friends[i]=getFromDTO(users[i]);
			}
			return friends;
		}
	}

}