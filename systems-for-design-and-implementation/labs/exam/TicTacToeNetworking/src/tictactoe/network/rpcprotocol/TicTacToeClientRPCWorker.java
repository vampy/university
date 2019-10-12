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
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// Used by the server, see LibraryRPCConcurrentServer::createWorker
// This will run in a thread
public class TicTacToeClientRPCWorker extends JsonSerialization implements Runnable, ITicTacToeClient
{
    private static Response OK_RESPONSE = new Response.Builder().type(ResponseType.OK).build();
    private          ITicTacToeServer server;
    private          Socket           connection;
    private          Reader           input;
    private          Writer           output;
    private          PrintWriter      writer;
    private volatile boolean          isConnected;

    public TicTacToeClientRPCWorker(ITicTacToeServer server, Socket connection)
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

    @Override
    public void gameStarted(Opponent opponent)
    {
        Response req = new Response.Builder().type(ResponseType.GAME_STARTED).data(opponent).build();
        System.out.println("Game started");
        try
        {
            sendResponse(req);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("This should never happen");
            assert false;
        }
    }

    @Override
    public void gameFinished(int winner_id) throws TicTacToeException
    {
        Response req = new Response.Builder().type(ResponseType.GAME_FINISHED).data(new GameWinner(winner_id)).build();
        System.out.println("Game finished");
        try
        {
            sendResponse(req);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void gridUpdated(Move move)
    {
        Response req = new Response.Builder().type(ResponseType.GRID_UPDATED).data(move).build();
        System.out.println("grid updated");
        try
        {
            sendResponse(req);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //    @Override
//    public void move(Move move) throws TicTacToeException
//    {
//
//    }

    // Will be called in LibraryServerImpl, on login the server implementation keeps track of all
    // workers, and the receiver socket will write the message to the output
//    @Override
//    public void updateBook(Book book) throws LibraryException
//    {
//        Response resp = new Response.Builder().type(ResponseType.UPDATE_BOOK).data(book).build();
//        System.out.println("Book updated " + book);
//        try
//        {
//            sendResponse(resp);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void updateLoan(BookLoan loan) throws LibraryException
//    {
//        Response resp = new Response.Builder().type(ResponseType.UPDATE_LOAN).data(loan).build();
//        System.out.println("Book loan " + loan);
//        try
//        {
//            sendResponse(resp);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }

    private Response handleRequest(JsonObject object)
    {
        System.out.println("[Server] Handling request: " + object.toString());

        RequestType type = RequestType.valueOf(object.get("type").getAsString());
        String jdata = null;

        if (object.has("data")) {
            jdata = object.getAsJsonObject("data").toString();
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
            catch (TicTacToeException e)
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
            catch (TicTacToeException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.START_GAME)
        {
            System.out.println("Start game request");
            User user = fromJSON(jdata, User.class);

            try
            {
                server.startGame(user);
            }
            catch (TicTacToeException e)
            {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        else if (type == RequestType.MOVE)
        {
            System.out.println("Move request");
            Move move = fromJSON(jdata, Move.class);

            try
            {
                server.move(move);
                return OK_RESPONSE;
            }
            catch (TicTacToeException e)
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
