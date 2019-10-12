package library.network.utils

import java.io.IOException
import java.net.{ServerSocket, Socket}

// and https://stackoverflow.com/questions/6223959/must-override-val-variable-in-scala/6224035#6224035
// and https://stackoverflow.com/questions/33228711/scala-constructor-confusion-please-clarify/33229314#33229314
abstract class AbstractServer(port: Int)
{
    private var server: ServerSocket = null

    @throws[ServerException]
    def start()
    {
        try
        {
            server = new ServerSocket(port)
            while (true)
            {
                {
                    println("Waiting for clients ...")
                    val client: Socket = server.accept
                    println("Client connected ...")
                    processRequest(client)
                }
            }
        }
        catch
        {
            case e: IOException =>
                throw new ServerException("Starting server error. Maybe that port is already taken?", e)
        }
        finally
        {
            try
            {
                if (server != null)
                {
                    server.close()
                }
            }
            catch
            {
                case e: IOException =>
                    throw new ServerException("Closing server error ", e)
            }
        }
    }

    @throws[ServerException]
    def stop()
    {
        try
        {
            server.close()
        }
        catch
        {
            case e: IOException =>
                throw new ServerException("Closing server error ", e)
        }
    }

    protected def processRequest(client: Socket)
}