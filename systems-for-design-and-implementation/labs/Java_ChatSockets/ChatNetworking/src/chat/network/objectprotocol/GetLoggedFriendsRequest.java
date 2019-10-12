package chat.network.objectprotocol;

import chat.network.dto.UserDTO;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 4:28:46 PM
 */
public class GetLoggedFriendsRequest implements Request
{
    private UserDTO user;

    public GetLoggedFriendsRequest(UserDTO user)
    {
        this.user = user;
    }

    public UserDTO getUser()
    {
        return user;
    }
}

