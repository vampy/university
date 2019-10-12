package chat.network.objectprotocol;

import chat.network.dto.UserDTO;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 4:31:16 PM
 */
public class GetLoggedFriendsResponse implements Response
{
    private UserDTO[] friends;

    public GetLoggedFriendsResponse(UserDTO[] friends)
    {
        this.friends = friends;
    }

    public UserDTO[] getFriends()
    {
        return friends;
    }
}
