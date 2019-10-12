package library.network.rpcprotocol

import java.io._
import java.net.Socket
import java.util

import com.google.gson.{JsonObject, JsonParser}
import library.model.{Book, BookLoan, User}
import library.network.dto.{BorrowDTO, TupleInt}
import library.network.rpcprotocol.RequestType.RequestType
import library.services.{ILibraryClient, ILibraryServer, LibraryException}

import scala.util.control.Breaks._

object LibraryClientRPCWorker
{
    private val OK_RESPONSE: Response = new Response.Builder().`type`(ResponseType.OK.toString).build
}

class LibraryClientRPCWorker(var server: ILibraryServer, var connection: Socket) extends JsonSerialization with ILibraryClient with Runnable
{
    private var input: Reader = null
    private var output: Writer = null
    private var isConnected: Boolean = false
    private var writer: PrintWriter = null

    println("LibraryClientRPCWorker")
    try
    {
        output = new OutputStreamWriter(connection.getOutputStream)
        output.flush()
        input = new InputStreamReader(connection.getInputStream)
        writer = new PrintWriter(output)
        isConnected = true
    }
    catch
    {
        case e: IOException => e.printStackTrace()
    }

    override def run()
    {
        while (isConnected)
        {
            try
            {
                var inputLine: String = null
                var obj: JsonObject = null

                var in: BufferedReader = new BufferedReader(input)
                breakable {
                    while ((inputLine = in.readLine()) != null) {
                        obj = new JsonParser().parse(inputLine).getAsJsonObject
                        break
                    }
                }

                if (obj != null)
                {
                    val response: Response = handleRequest(obj)
                    if (response != null)
                    {
                        sendResponse(response)
                    }
                    else
                    {
                        println("NULLLL RESPONSE")
                    }
                }
            }
            catch
            {
                case e: IOException =>
                    println("Argggghh, IOException happened in RPCWorker")
                    e.printStackTrace()
                    println("Socket closed. TIMED OUT")
                    isConnected = false
                case e: ClassNotFoundException => e.printStackTrace()
            }
            try
            {
                Thread.sleep(1000)
            }
            catch
            {
                case e: InterruptedException => e.printStackTrace()
            }
        }
        try
        {
            if (input != null)
            {
                input.close()
            }
            if (output != null)
            {
                output.close()
            }
            if (connection != null)
            {
                connection.close()
            }
        }
        catch
        {
            case e: IOException => println("Error " + e)
        }
    }

    private def handleRequest(obj: JsonObject): Response =
    {
        println("[Server] Handling request: " + obj.toString)

        val responseType: RequestType = RequestType.withName(obj.get("type").getAsString)
        var jdata: String = null

        if (obj.has("data"))
        {
            if (responseType == RequestType.SEARCH_TITLE) {
                jdata = obj.getAsJsonPrimitive("data").toString
            }
            else
            {
                jdata = obj.getAsJsonObject("data").toString
            }
        }

        if (responseType eq RequestType.LOGIN)
        {
            println("Login request")
            val user: User = fromJSON(jdata, classOf[User])
            try
            {
                val fullUser: User = server.login(user, this)
                return new Response.Builder().`type`(ResponseType.OK.toString).data(fullUser).build
            }
            catch
            {
                case e: LibraryException =>
                    isConnected = false
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        else if (responseType eq RequestType.LOGOUT)
        {
            println("Logout request")
            val user: User = fromJSON(jdata, classOf[User])
            try
            {
                server.logout(user, this)
                isConnected = false
                return LibraryClientRPCWorker.OK_RESPONSE
            }
            catch
            {
                case e: LibraryException =>
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        else if (responseType eq RequestType.GET_BOOKS_AVAILABLE)
        {
            println("Get all available books request ...")
            try
            {
                val books: util.List[Book] = server.getAvailableBooks
                return new Response.Builder().`type`(ResponseType.GET_BOOKS_AVAILABLE.toString).data(books).build
            }
            catch
            {
                case e: LibraryException =>
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        else if (responseType eq RequestType.GET_ALL_LOANS)
        {
            println("Get all loans request ...")
            try
            {
                val books: util.List[BookLoan] = server.getAllLoans
                return new Response.Builder().`type`(ResponseType.GET_ALL_LOANS.toString).data(books).build
            }
            catch
            {
                case e: LibraryException =>
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        else if (responseType eq RequestType.SEARCH_TITLE)
        {
            println("Search title request ...")
            val title: String = fromJSON(jdata, classOf[String])
            try
            {
                val books: util.List[Book] = server.searchByTitle(title)
                return new Response.Builder().`type`(ResponseType.SEARCH_TITLE.toString).data(books).build
            }
            catch
            {
                case e: LibraryException =>
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        else if (responseType eq RequestType.GET_BORROWED_BY)
        {
            println("Get borrowed by request ...")
            val user: User = fromJSON(jdata, classOf[User])
            try
            {
                val books: util.List[Book] = server.getBorrowedBy(user)
                return new Response.Builder().`type`(ResponseType.GET_BORROWED_BY.toString).data(books).build
            }
            catch
            {
                case e: LibraryException =>
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        else if (responseType eq RequestType.BORROW_BOOK)
        {
            println("Borrow book request ...")
            val borrow: BorrowDTO = fromJSON(jdata, classOf[BorrowDTO])
            try
            {
                server.borrowBook(borrow.getBook, borrow.getUser)
                return new Response.Builder().`type`(ResponseType.BORROW_BOOK.toString).build
            }
            catch
            {
                case e: LibraryException =>
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        else if (responseType eq RequestType.RETURN_BOOK)
        {
            println("Return book request ...")
            val borrow: TupleInt = fromJSON(jdata, classOf[TupleInt])
            try
            {
                server.returnBook(borrow.getFirst, borrow.getSecond)
                return new Response.Builder().`type`(ResponseType.BORROW_BOOK.toString).build
            }
            catch
            {
                case e: LibraryException =>
                    return new Response.Builder().`type`(ResponseType.ERROR.toString).data(e.getMessage).build
            }
        }
        null
    }

    @throws[IOException]
    private def sendResponse(response: Response)
    {
        val json: String = toJSON(response, classOf[Response])

        println("[Server] Sending response: " + json)
        //output.writeObject(response)
        //output.flush()
        writer.println(json)
        writer.flush()
    }

    @throws[LibraryException]
    def updateBook(book: Book)
    {
        val resp: Response = new Response.Builder().`type`(ResponseType.UPDATE_BOOK.toString).data(book).build
        println("Book updated " + book)
        try
        {
            sendResponse(resp)
        }
        catch
        {
            case e: IOException => e.printStackTrace()
        }
    }

    @throws[LibraryException]
    def updateLoan(loan: BookLoan)
    {
        val resp: Response = new Response.Builder().`type`(ResponseType.UPDATE_LOAN.toString).data(loan).build
        println("Book loan " + loan)
        try
        {
            sendResponse(resp)
        }
        catch
        {
            case e: IOException => e.printStackTrace()
        }
    }
}
