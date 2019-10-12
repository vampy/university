package library.server

import library.network.utils.{AbstractServer, LibraryRPCConcurrentServer, ServerException}
import library.persistance.repository.jdbc.{BookRepositoryJDBC, UserRepositoryJDBC}
import library.persistance.repository.{IBookRepository, IUserRepository}
import library.services.impl.LibraryServerImpl
import library.services.{Constants, ILibraryServer}

object ServerStartRPC
{
    def main(args: Array[String])
    {
        println("Starting Server")
        val userRepository: IUserRepository = new UserRepositoryJDBC
        val bookRepository: IBookRepository = new BookRepositoryJDBC

        val libraryServerImpl: ILibraryServer = new LibraryServerImpl(userRepository, bookRepository)
        val server: AbstractServer = new LibraryRPCConcurrentServer(Constants.PORT, libraryServerImpl)
        try
        {
            server.start()
        }
        catch
        {
            case e: ServerException => println(e.getMessage)
        }
    }
}
