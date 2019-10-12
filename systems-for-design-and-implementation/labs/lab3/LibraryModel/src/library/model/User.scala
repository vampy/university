package library.model

import java.io.Serializable
import java.util

class User extends Serializable
{
    private var id: Int = -1
    private var username: String = null
    private var password: String = null
    private var is_librarian: Boolean = false
    private var borrowed: util.Set[Integer] = null

    def this(id: Int)
    {
        this()
        this.id = id
    }

    def this(username: String, password: String)
    {
        this()
        this.username = username
        this.password = password
    }

    def this(id: Int, username: String, password: String)
    {
        this()
        this.id = id
        this.username = username
        this.password = password
    }

    def this(id: Int, username: String, password: String, borrowed: util.Set[Integer])
    {
        this()
        this.id = id
        this.username = username
        this.password = password
        this.borrowed = borrowed
    }

    def this(id: Int, username: String, password: String, isLibrarian: Boolean, borrowed: util.Set[Integer])
    {
        this()
        this.id = id
        this.username = username
        this.password = password
        this.is_librarian = isLibrarian
        this.borrowed = borrowed
    }

    def getUsername: String =
    {
        username
    }

    def setUsername(username: String)
    {
        this.username = username
    }

    def getPassword: String =
    {
        password
    }

    def setPassword(password: String)
    {
        this.password = password
    }

    def getBorrowed: util.Set[Integer] =
    {
        borrowed
    }

    def setBorrowed(borrowed: util.Set[Integer])
    {
        this.borrowed = borrowed
    }

    def isLibrarian: Boolean =
    {
        is_librarian
    }

    def setLibrarian(librarian: Boolean)
    {
        is_librarian = librarian
    }

    def addBorrowed(book_id: Int)
    {
        assert(id != 0)
        borrowed.add(book_id)
    }

    def removeBorrowed(book_id: Int)
    {
        assert(id != 0)
        borrowed.remove(book_id)
    }

    override def equals(obj: Any): Boolean =
    {
        assert(id != 0)
        if (obj.isInstanceOf[User])
        {
            val user: User = obj.asInstanceOf[User]
            return id == user.getId
        }
        false
    }

    def getId: Int =
    {
        id
    }

    def setId(id: Int)
    {
        this.id = id
    }

    override def toString: String =
    {
        "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password + '\'' + ", is_librarian=" + is_librarian + ", borrowed=" + borrowed + '}'
    }
}
