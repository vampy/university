package library.network.utils

import java.net.Socket

abstract class AbstractConcurrentServer(port: Int) extends AbstractServer(port)
{
    println("Concurrent AbstractServer")

    protected def processRequest(client: Socket)
    {
        val tw: Thread = createWorker(client)
        tw.start()
    }

    protected def createWorker(client: Socket): Thread
}