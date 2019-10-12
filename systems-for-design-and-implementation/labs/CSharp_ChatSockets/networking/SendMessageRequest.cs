using System;
namespace chat.network.protocol
{

	using MessageDTO = chat.network.dto.MessageDTO;

	///
	/// <summary> * Created by IntelliJ IDEA.
	/// * User: grigo
	/// * Date: Mar 18, 2009
	/// * Time: 4:26:05 PM </summary>
	/// 
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


}