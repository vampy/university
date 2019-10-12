package library.network.utils

import java.net.Socket

import library.network.rpcprotocol.LibraryClientRPCWorker
import library.services.ILibraryServer

class LibraryRPCConcurrentServer(val port: Int, val libraryServer: ILibraryServer)
    extends AbstractConcurrentServer(port)
{
    println("LibraryRPCConcurrentServer")

    protected def createWorker(client: Socket): Thread =
    {
        val worker: LibraryClientRPCWorker = new LibraryClientRPCWorker(libraryServer, client)
        new Thread(worker)
    }
}
