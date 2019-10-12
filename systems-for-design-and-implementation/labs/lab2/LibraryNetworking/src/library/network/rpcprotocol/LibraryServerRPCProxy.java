package library.network.rpcprotocol;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import library.model.Book;
import library.model.BookLoan;
import library.model.User;
import library.network.dto.BorrowDTO;
import library.network.dto.TupleInt;
import library.services.ILibraryClient;
import library.services.ILibraryServer;
import library.services.LibraryException;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Used by the client
public class LibraryServerRPCProxy extends JsonSerialization implements ILibraryServer
{
    private String host;
    private int    port;

    private ILibraryClient client;

    private Reader             input;
    private Writer             output;
    private Socket             connection;
    private PrintWriter writer;

    private          BlockingQueue<Response> responses;
    private volatile boolean                 isFinished;

    public LibraryServerRPCProxy(String host, int port)
    {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public User login(User user, ILibraryClient client) throws LibraryException
    {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);

        Response response = readResponse();

        if (response.getType() == ResponseType.OK)
        {
            this.client = client;
            return fromJSON((String) response.getData(), User.class);
        }

        if (response.getType() == ResponseType.ERROR)
        {
            closeConnection();
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }

        return null;
    }

    @Override
    public void logout(User user, ILibraryClient client) throws LibraryException
    {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }
    }

    @Override
    public List<Book> getAvailableBooks() throws LibraryException
    {
        Request req = new Request.Builder().type(RequestType.GET_BOOKS_AVAILABLE).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }

        // I like to live dangerously

        return convertToCustomList((String) response.getData(), Book.class);
    }

    @Override
    public List<BookLoan> getAllLoans() throws LibraryException
    {
        Request req = new Request.Builder().type(RequestType.GET_ALL_LOANS).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }

        // I like to live dangerously
        return convertToCustomList((String) response.getData(), BookLoan.class);
    }

    @Override
    public List<Book> getBorrowedBy(User user) throws LibraryException
    {
        Request req = new Request.Builder().type(RequestType.GET_BORROWED_BY).data(user).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }

        // I like to live dangerously
        return convertToCustomList((String) response.getData(), Book.class);
    }

    @Override
    public List<Book> searchByTitle(String title) throws LibraryException
    {
        Request req = new Request.Builder().type(RequestType.SEARCH_TITLE).data(title).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }

        // I like to live dangerously
        return convertToCustomList((String) response.getData(), Book.class);
    }

    @Override
    public void borrowBook(Book book, User user) throws LibraryException
    {
        BorrowDTO borrow = new BorrowDTO(book, user);
        Request req = new Request.Builder().type(RequestType.BORROW_BOOK).data(borrow).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }
    }

    @Override
    public void returnBook(int book_id, int user_id) throws LibraryException
    {
        TupleInt borrow = new TupleInt(book_id, user_id);
        Request req = new Request.Builder().type(RequestType.RETURN_BOOK).data(borrow).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new LibraryException(fromJSON((String) response.getData(), String.class));
        }
    }

    private void sendRequest(Request request) throws LibraryException
    {
        try
        {
            String requestJSON = toJSON(request, Request.class);
            System.out.println("[Client] Sending request: " + requestJSON);
            writer.println(requestJSON);
            writer.flush();
        }
        catch (Exception e)
        {
            throw new LibraryException("Error sending object " + e);
        }
    }

    private Response readResponse() throws LibraryException
    {
        Response response = null;
        try
        {
            response = responses.take();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws LibraryException
    {
        try
        {
            connection = new Socket(host, port);

            input = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            output = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer = new PrintWriter(output);

            isFinished = false;
            startReader();
        }
        catch (ConnectException e)
        {
            throw new LibraryException(e.getMessage());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void closeConnection()
    {
        isFinished = true;
        try
        {
            input.close();
            output.close();
            connection.close();
            client = null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Start the thread that reads from the server
    private void startReader()
    {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(ResponseType type, String data)
    {
        if (type == ResponseType.UPDATE_BOOK)
        {
            Book book = fromJSON(data, Book.class);
            try
            {
                // Call the TerminalController::updateBook.
                client.updateBook(book);
            }
            catch (LibraryException e)
            {
                e.printStackTrace();
            }
        }
        else if (type == ResponseType.UPDATE_LOAN)
        {
            BookLoan loan =  fromJSON(data, BookLoan.class);
            try
            {
                // Call the TerminalController::updateLoan.
                client.updateLoan(loan);
            }
            catch (LibraryException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(ResponseType response)
    {
        return response == ResponseType.UPDATE_LOAN || response == ResponseType.NEW_MESSAGE || response == ResponseType.UPDATE_BOOK;
    }

    private class ReaderThread implements Runnable
    {
        public void run()
        {
            while (!isFinished)
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
                        System.out.println("[Client] Response received: " + object.toString());

                        ResponseType type = ResponseType.valueOf(object.get("type").getAsString());

                        String jdata = null;
                        if (type.name().startsWith("GET") || type== ResponseType.SEARCH_TITLE)
                            jdata = object.getAsJsonArray("data").toString();
                        else if (object.has("data") && type != ResponseType.ERROR)
                            jdata = object.getAsJsonObject("data").toString();
                        else if (object.has("data"))
                            jdata = object.get("data").getAsString();

                        // Handle update
                        if (isUpdate(type))
                        {
                            handleUpdate(type, jdata);
                        }
                        else
                        {
                            try
                            {
                                Response response = new Response.Builder().type(type).data(jdata).build();
                                responses.put(response);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Reading error");
                    if (!isFinished && !connection.isClosed())
                    {
                        e.printStackTrace();
                        System.out.println("Server timed out");
                        System.exit(1);
                    }
                }
            }
        }
    }
}
