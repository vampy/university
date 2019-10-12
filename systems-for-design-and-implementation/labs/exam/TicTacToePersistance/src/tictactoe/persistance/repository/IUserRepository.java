package tictactoe.persistance.repository;


import tictactoe.model.User;

public interface IUserRepository
{
    boolean verifyUser(User user);

    User getUserByUsername(String username);
}
