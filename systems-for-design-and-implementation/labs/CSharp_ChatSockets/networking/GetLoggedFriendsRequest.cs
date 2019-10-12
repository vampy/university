using System;
namespace chat.network.protocol
{

	using UserDTO = chat.network.dto.UserDTO;

	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:28:46 PM </summary>
	/// 
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