using System;
namespace chat.network.protocol
{

	using UserDTO = chat.network.dto.UserDTO;

	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:33:37 PM </summary>
	/// 
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

}