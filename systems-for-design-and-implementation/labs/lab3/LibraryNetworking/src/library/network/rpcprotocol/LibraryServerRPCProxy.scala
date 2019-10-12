package library.network.rpcprotocol

import java.io._
import java.net.{ConnectException, Socket}
import java.nio.charset.StandardCharsets
import java.util
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.google.gson.{JsonObject, JsonParser}
import library.model.{Book, BookLoan, User}
import library.network.dto.{BorrowDTO, TupleInt}
import library.network.rpcprotocol.ResponseType.ResponseType
import library.services.{ILibraryClient, ILibraryServer, LibraryException}

import scala.util.control.Breaks._

// Used by the client
class LibraryServerRPCProxy(var host: String, var port: Int) extends JsonSerialization with ILibraryServer
{
    private var client: ILibraryClient = null
    private var input: Reader = null
    private var output: Writer = null
    private var writer: PrintWriter = null
    private var connection: Socket = null
    private var responses: BlockingQueue[Response] = null
    private var isFinished: Boolean = false

    responses = new LinkedBlockingQueue[Response]

    @throws[LibraryException]
    def login(user: User, client: ILibraryClient): User =
    {
        initializeConnection()
        val req: Request = new Request.Builder().`type`(RequestType.LOGIN.toString).data(user).build
        sendRequest(req)
        val response: Response = readResponse
        if (response.getType eq ResponseType.OK)
        {
            this.client = client
            return fromJSON(response.getData.asInstanceOf[String], classOf[User])
            //return response.getData.asInstanceOf[User]
        }
        if (response.getType eq ResponseType.ERROR)
        {
            closeConnection()
            throw new LibraryException(response.getData.toString)
        }
        null
    }

    @throws[LibraryException]
    private def initializeConnection()
    {
        try
        {
            connection = new Socket(host, port)
            output = new OutputStreamWriter(connection.getOutputStream, StandardCharsets.UTF_8)
            output.flush()
            input = new InputStreamReader(connection.getInputStream, StandardCharsets.UTF_8)
            writer = new PrintWriter(output)
            isFinished = false
            startReader()
            println("Connection Initialized")
            assert(input != null)
        }
        catch
        {
            case e: ConnectException => throw new LibraryException(e.getMessage)
            case e: IOException => e.printStackTrace()
        }
    }

    private def startReader()
    {
        val tw: Thread = new Thread(new ReaderThread)
        tw.start()
    }

    @throws[LibraryException]
    private def sendRequest(request: Request)
    {
        try
        {
            val requestJSON: String = toJSON(request, classOf[Request])
            println("[Client] Sending request: " + requestJSON)
            writer.println(requestJSON)
            writer.flush()
        }
        catch
        {
            case e: Exception => println(
                e.printStackTrace(new PrintWriter(new StringWriter()))); throw new LibraryException(
                "Error sending object " + e)
        }
    }

    @throws[LibraryException]
    private def readResponse: Response =
    {
        var response: Response = null
        try
        {
            response = responses.take
        }
        catch
        {
            case e: InterruptedException => e.printStackTrace()
        }
        response
    }

    private def closeConnection()
    {
        isFinished = true
        try
        {
            input.close()
            output.close()
            connection.close()
            client = null
            println("Connection Closed")
        }
        catch
        {
            case e: IOException => e.printStackTrace()
        }
    }

    @throws[LibraryException]
    def logout(user: User, client: ILibraryClient)
    {
        val req: Request = new Request.Builder().`type`(RequestType.LOGOUT.toString).data(user).build
        sendRequest(req)
        val response: Response = readResponse
        closeConnection()
        if (response.getType eq ResponseType.ERROR)
        {
            throw new LibraryException(response.getData.toString)
        }
    }

    @throws[LibraryException]
    def getAvailableBooks: util.List[Book] =
    {
        val req: Request = new Request.Builder().`type`(RequestType.GET_BOOKS_AVAILABLE.toString).build
        sendRequest(req)
        val response: Response = readResponse
        if (response.getType eq ResponseType.ERROR)
        {
            throw new LibraryException(response.getData.toString)
        }
        convertToCustomList(response.getData.asInstanceOf[String], classOf[Book])
    }

    @throws[LibraryException]
    def getAllLoans: util.List[BookLoan] =
    {
        val req: Request = new Request.Builder().`type`(RequestType.GET_ALL_LOANS.toString).build
        sendRequest(req)
        val response: Response = readResponse
        if (response.getType eq ResponseType.ERROR)
        {
            throw new LibraryException(response.getData.toString)
        }
        convertToCustomList(response.getData.asInstanceOf[String], classOf[BookLoan])
    }

    @throws[LibraryException]
    def getBorrowedBy(user: User): util.List[Book] =
    {
        val req: Request = new Request.Builder().`type`(RequestType.GET_BORROWED_BY.toString).data(user).build
        sendRequest(req)
        val response: Response = readResponse
        if (response.getType eq ResponseType.ERROR)
        {
            throw new LibraryException(response.getData.toString)
        }
        convertToCustomList(response.getData.asInstanceOf[String], classOf[Book])
    }

    @throws[LibraryException]
    def searchByTitle(title: String): util.List[Book] =
    {
        val req: Request = new Request.Builder().`type`(RequestType.SEARCH_TITLE.toString).data(title).build
        sendRequest(req)
        val response: Response = readResponse
        if (response.getType eq ResponseType.ERROR)
        {
            throw new LibraryException(response.getData.toString)
        }
        convertToCustomList(response.getData.asInstanceOf[String], classOf[Book])
    }

    @throws[LibraryException]
    def borrowBook(book: Book, user: User)
    {
        val borrow: BorrowDTO = new BorrowDTO(book, user)
        val req: Request = new Request.Builder().`type`(RequestType.BORROW_BOOK.toString).data(borrow).build
        sendRequest(req)
        val response: Response = readResponse
        if (response.getType eq ResponseType.ERROR)
        {
            throw new LibraryException(response.getData.toString)
        }
    }

    @throws[LibraryException]
    def returnBook(book_id: Int, user_id: Int)
    {
        val borrow: TupleInt = new TupleInt(book_id, user_id)
        val req: Request = new Request.Builder().`type`(RequestType.RETURN_BOOK.toString).data(borrow).build
        sendRequest(req)
        val response: Response = readResponse
        if (response.getType eq ResponseType.ERROR)
        {
            throw new LibraryException(response.getData.toString)
        }
    }

    private def handleUpdate(responseType: ResponseType, data: String)
    {
        if (responseType eq ResponseType.UPDATE_BOOK)
        {
            val book: Book = fromJSON(data, classOf[Book])
            try
            {
                client.updateBook(book)
            }
            catch
            {
                case e: LibraryException => e.printStackTrace()
            }
        }
        else if (responseType eq ResponseType.UPDATE_LOAN)
        {
            val loan: BookLoan = fromJSON(data, classOf[BookLoan])
            try
            {
                client.updateLoan(loan)
            }
            catch
            {
                case e: LibraryException => e.printStackTrace()
            }
        }
    }

    private def isUpdate(response: ResponseType): Boolean =
    {
        response == ResponseType.UPDATE_LOAN || response == ResponseType.NEW_MESSAGE || response == ResponseType
            .UPDATE_BOOK
    }

    private class ReaderThread extends Runnable
    {
        def run()
        {
            while (!isFinished)
            {
                try
                {
                    var inputLine: String = null
                    var obj: JsonObject = null

                    var in: BufferedReader = new BufferedReader(input)
                    breakable
                    {
                        while ((inputLine = in.readLine()) != null)
                        {
                            obj = new JsonParser().parse(inputLine).getAsJsonObject
                            break
                        }
                    }

                    if (obj != null)
                    {
                        println("[Client] Response received: " + obj.toString)
                        val responseType: ResponseType = ResponseType.withName(obj.get("type").getAsString)
                        var jdata: String = null

                        if (responseType == ResponseType.GET_BOOKS_AVAILABLE
                            || responseType == ResponseType.GET_BORROWED_BY
                            || responseType == ResponseType.GET_ALL_LOANS
                            || responseType == ResponseType.SEARCH_TITLE)
                        {
                            jdata = obj.getAsJsonArray("data").toString
                        }
                        else if (obj.has("data") && responseType != ResponseType.ERROR)
                        {
                            jdata = obj.getAsJsonObject("data").toString
                        }
                        else if (obj.has("data"))
                        {
                            jdata = obj.get("data").getAsString
                        }

                        if (isUpdate(responseType))
                        {
                            handleUpdate(responseType, jdata)
                        }
                        else
                        {
                            try
                            {
                                val response: Response = new Response.Builder().`type`(responseType.toString).data(jdata).build
                                responses.put(response)
                            }
                            catch
                            {
                                case e: InterruptedException => e.printStackTrace()
                            }
                        }
                    }
                }
                catch
                {
                    case e: Exception =>
                        println("Reading error")
                        if (!isFinished && !connection.isClosed)
                        {
                            e.printStackTrace()
                            println("Server timed out")
                            System.exit(1)
                        }
                    case e: ClassNotFoundException => println("Reading error " + e)
                }
            }
        }
    }

}
