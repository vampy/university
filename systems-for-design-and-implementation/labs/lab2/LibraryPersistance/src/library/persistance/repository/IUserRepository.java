package library.persistance.repository;


import library.model.User;

public interface IUserRepository
{
    boolean verifyUser(User user);

    User getUserByUsername(String username);
}
