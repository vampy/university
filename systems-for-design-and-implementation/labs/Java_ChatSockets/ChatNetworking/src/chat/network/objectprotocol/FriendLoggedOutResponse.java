package chat.network.objectprotocol;

import chat.network.dto.UserDTO;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 4:34:36 PM
 */
public class FriendLoggedOutResponse implements UpdateResponse
{
    private UserDTO friend;

    public FriendLoggedOutResponse(UserDTO friend)
    {
        this.friend = friend;
    }

    public UserDTO getFriend()
    {
        return friend;
    }
}
