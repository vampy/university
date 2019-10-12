package tictactoe.network.rpcprotocol;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tictactoe.model.GameWinner;
import tictactoe.model.Move;
import tictactoe.model.Opponent;
import tictactoe.model.User;
import tictactoe.services.ITicTacToeClient;
import tictactoe.services.ITicTacToeServer;
import tictactoe.services.TicTacToeException;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Used by the client
public class TicTacToeServerRPCProxy extends JsonSerialization implements ITicTacToeServer
{
    private String host;
    private int    port;

    private ITicTacToeClient client;

    private Reader             input;
    private Writer             output;
    private Socket             connection;
    private Opponent opponent;
    private PrintWriter writer;

    private          BlockingQueue<Response> responses;
    private volatile boolean                 isFinished;

    public TicTacToeServerRPCProxy(String host, int port)
    {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public User login(User user, ITicTacToeClient client) throws TicTacToeException
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
            throw new TicTacToeException(fromJSON((String) response.getData(), String.class));
        }

        return null;
    }

    @Override
    public void logout(User user, ITicTacToeClient client) throws TicTacToeException
    {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();

        if (response.getType() == ResponseType.ERROR)
        {
            throw new TicTacToeException(fromJSON((String) response.getData(), String.class));
        }
    }

    @Override
    public void startGame(User user) throws TicTacToeException
    {
        Request req = new Request.Builder().type(RequestType.START_GAME).data(user).build();
        sendRequest(req);
//        Response response = readResponse();
//
//        if (response.getType() == ResponseType.ERROR)
//        {
//            throw new TicTacToeException(fromJSON((String) response.getData(), String.class));
//        }

        // wait for pair
//        opponent = fromJSON((String) response.getData(), Opponent.class);
//        System.out.println(opponent);
    }

    @Override
    public void move(Move move) throws TicTacToeException
    {
        Request req = new Request.Builder().type(RequestType.MOVE).data(move).build();
        sendRequest(req);

        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
        {
            throw new TicTacToeException(fromJSON((String) response.getData(), String.class));
        }
    }

    public Opponent getOpponent()
    {
        return opponent;
    }

    private void sendRequest(Request request) throws TicTacToeException
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
            throw new TicTacToeException("Error sending object " + e);
        }
    }

    private Response readResponse() throws TicTacToeException
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

    private void initializeConnection() throws TicTacToeException
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
            throw new TicTacToeException(e.getMessage());
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
        if (type == ResponseType.GAME_STARTED)
        {
            Opponent opponent = fromJSON(data, Opponent.class);
            try
            {
                client.gameStarted(opponent);
            }
            catch (TicTacToeException e)
            {
                e.printStackTrace();
            }

        }
        else if (type == ResponseType.GRID_UPDATED)
        {
            Move move = fromJSON(data, Move.class);
//            try
            {
                client.gridUpdated(move);
            }
//            catch (TicTacToeException e)
            {
//                e.printStackTrace();
            }
        }
        else if (type == ResponseType.GAME_FINISHED)
        {
            GameWinner winner = fromJSON(data, GameWinner.class);

            try
            {
                client.gameFinished(winner.getWinner_id());
            }
            catch (TicTacToeException e)
            {
                e.printStackTrace();
            }
        }
//        if (type == ResponseType.UPDATE_BOOK)
//        {
//            Book book = fromJSON(data, Book.class);
//            try
//            {
//                // Call the TerminalController::updateBook.
//                client.updateBook(book);
//            }
//            catch (LibraryException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        else if (type == ResponseType.UPDATE_LOAN)
//        {
//            BookLoan loan =  fromJSON(data, BookLoan.class);
//            try
//            {
//                // Call the TerminalController::updateLoan.
//                client.updateLoan(loan);
//            }
//            catch (LibraryException e)
//            {
//                e.printStackTrace();
//            }
//        }
    }

    private boolean isUpdate(ResponseType response)
    {
        return response == ResponseType.GAME_STARTED || response == ResponseType.GRID_UPDATED || response == ResponseType.GAME_FINISHED;
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
//                        if (type.name().startsWith("GET") || type == ResponseType.SEARCH_TITLE)
//                            jdata = object.getAsJsonArray("data").toString();
//                        else
                        if (object.has("data") && type != ResponseType.ERROR)
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
