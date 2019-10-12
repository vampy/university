package library.network.rpcprotocol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import library.model.Book;
import library.model.BookLoan;
import library.model.User;
import library.network.dto.BorrowDTO;
import library.network.dto.TupleInt;
import library.services.ILibraryClient;
import library.services.ILibraryServer;
import library.services.LibraryException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Used by the server, see LibraryRPCConcurrentServer::createWorker
// This will run in a thread
public class LibraryClientRPCWorker extends JsonSerialization implements Runnable, ILibraryClient
{
    private static Response OK_RESPONSE = new Response.Builder().type(ResponseType.OK).build();
    private          ILibraryServer     server;
    private          Socket             connection;
    private          Reader             input;
    private          Writer             output;
    private PrintWriter writer;
    private volatile boolean            isConnected;

    public LibraryClientRPCWorker(ILibraryServer server, Socket connection)
    {
        this.server = server;
        this.connection = connection;

        try
        {
            input = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            output = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer = new PrintWriter(output);
//            output.flush();

            isConnected = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while (isConnected)
        {
            try
            {
                String inputLine;
                JsonObject object = null;

                BufferedReader in = new BufferedReader(input);
                while ((inputLine = in.readLine()) != null) {
                    //System.out.println("inputLine: " + inputLine);
                    object = new JsonParser().parse(inputLine).getAsJsonObject();
                    break;
                }

                if (object != null)
                {
                    Response response = handleRequest(object);

                    if (response != null)
                    {
                        sendResponse(response);
                    }
                    else
                    {
                        System.out.println("NULLLL RESPONSE");
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println("Argggghh, IOException happened in RPCWorker");
                e.printStackTrace();

                // anyone home?
                System.out.println("Socket closed. TIMED OUT");
                isConnected = false;
            }

            try
            {
                // sleep 1 second
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // Close all the stuff
        try
        {
            if (input != null)
            {
                input.close();
            }
            if (output != null)
            {
                output.close();
            }
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error " + e);
        }
    }

    // Will be called in LibraryServerImpl, on login the server implementation keeps track of all
    // workers, and the receiver socket will write the message to the output
    @Override
    public void updateBook(Book book) throws LibraryException
    {
        Response resp = new Response.Builder().type(ResponseType.UPDATE_BOOK).data(book).build();
        System.out.println("Book updated " + book);
        try
        {
            sendResponse(resp);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLoan(BookLoan loan) throws LibraryException
    {
        Response resp = new Response.Builder().type(ResponseType.UPDATE_LOAN).data(loan).build();
        System.out.println("Book loan " + loan);
        try
        {
            sendResponse(resp);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Response handleRequest(JsonObject object)
    {
        System.out.println("[Server] Handling request: " + object.toString());

        RequestType type = RequestType.valueOf(object.get("type").getAsString());
        String jdata = null;

        if (object.has("data")) {
            if (type == RequestType.SEARCH_TITLE) {
                jdata = object.getAsJsonPrimitive("data").toString();
            }
            else {
                jdata = object.getAsJsonObject("data").toString();
            }
        }

        if (type == RequestType.LOGIN)
        {
            // java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to library.model.User
            System.out.println("Login request");
            User user = fromJSON(jdata, User.class);
            try
            {
                // return full user
                User fullUser = server.login(user, this);
                return new Response.Builder().type(ResponseType.OK).data(fullUser).build();
            }
            catch (LibraryException e)
            {
                isConnected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.LOGOUT)
        {
            System.out.println("Logout request");
            User user = fromJSON(jdata, User.class);
            try
            {
                server.logout(user, this);
                isConnected = false;
                return OK_RESPONSE;

            }
            catch (LibraryException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.GET_BOOKS_AVAILABLE)
        {
            System.out.println("Get all available books request ...");
            try
            {
                List<Book> books = server.getAvailableBooks();
                return new Response.Builder().type(ResponseType.GET_BOOKS_AVAILABLE).data(books).build();
            }
            catch (LibraryException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.GET_ALL_LOANS)
        {
            System.out.println("Get all loans request ...");
            try
            {
                List<BookLoan> books = server.getAllLoans();
                return new Response.Builder().type(ResponseType.GET_ALL_LOANS).data(books).build();
            }
            catch (LibraryException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }

        }
        else if (type == RequestType.SEARCH_TITLE)
        {
            System.out.println("Search title request ...");
            String title = fromJSON(jdata, String.class);
            try
            {
                List<Book> books = server.searchByTitle(title);
                return new Response.Builder().type(ResponseType.SEARCH_TITLE).data(books).build();
            }
            catch (LibraryException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.GET_BORROWED_BY)
        {
            System.out.println("Get borrowed by request ...");
            User user = fromJSON(jdata, User.class);
            try
            {
                List<Book> books = server.getBorrowedBy(user);
                return new Response.Builder().type(ResponseType.GET_BORROWED_BY).data(books).build();
            }
            catch (LibraryException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.BORROW_BOOK)
        {
            System.out.println("Borrow book request ...");
            BorrowDTO borrow = fromJSON(jdata, BorrowDTO.class);
            try
            {
                server.borrowBook(borrow.getBook(), borrow.getUser());
                return new Response.Builder().type(ResponseType.BORROW_BOOK).build();
            }
            catch (LibraryException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.RETURN_BOOK)
        {
            System.out.println("Return book request ...");
            TupleInt borrow = fromJSON(jdata, TupleInt.class);
            try
            {
                server.returnBook(borrow.getFirst(), borrow.getSecond());
                return new Response.Builder().type(ResponseType.BORROW_BOOK).build();
            }
            catch (LibraryException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return null;
    }

    private void sendResponse(Response response) throws IOException
    {
        String json = toJSON(response, Response.class);
        System.out.println("[Server] Sending response: " + json);

        writer.println(json);
        writer.flush();
        //output.write(json);
        //output.write(System.getProperty("line.separator"));
        //output.flush();
    }
}
