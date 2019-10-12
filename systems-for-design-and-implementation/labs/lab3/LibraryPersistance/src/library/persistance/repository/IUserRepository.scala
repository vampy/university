package library.persistance.repository

import library.model.User

trait IUserRepository
{
    def verifyUser(user: User): Boolean

    def getUserByUsername(username: String): User
}
