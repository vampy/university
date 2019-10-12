package chat.network.objectprotocol;

import chat.network.dto.UserDTO;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 4:19:12 PM
 */
public class LoginRequest implements Request
{
    private UserDTO user;

    public LoginRequest(UserDTO user)
    {
        this.user = user;
    }

    public UserDTO getUser()
    {
        return user;
    }
}
