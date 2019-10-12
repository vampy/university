using System;
namespace chat.network.protocol
{

	using UserDTO = chat.network.dto.UserDTO;

	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:31:16 PM </summary>
	/// 
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

}