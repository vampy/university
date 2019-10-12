using System;
namespace chat.network.protocol
{

	using MessageDTO = chat.network.dto.MessageDTO;

	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:35:38 PM </summary>
	/// 
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