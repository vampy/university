using System;

namespace chat.network.dto
{


	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:20:27 PM </summary>
	/// 
	[Serializable]
	public class UserDTO
	{
		private string id;
		private string passwd;

		public UserDTO(string id) : this(id,"")
		{
		}

		public UserDTO(string id, string passwd)
		{
			this.id = id;
			this.passwd = passwd;
		}

		public virtual string Id
		{
			get
			{
				return id;
			}
			set
			{
				this.id = value;
			}
		}


		public virtual string Passwd
		{
			get
			{
				return passwd;
			}
		}
	}

}