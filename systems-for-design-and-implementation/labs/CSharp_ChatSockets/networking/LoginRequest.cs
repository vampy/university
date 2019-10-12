using System;
namespace chat.network.protocol
{

	using UserDTO = chat.network.dto.UserDTO;

	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:19:12 PM </summary>
	/// 
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

}