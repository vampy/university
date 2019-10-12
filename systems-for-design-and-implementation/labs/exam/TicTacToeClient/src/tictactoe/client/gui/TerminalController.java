package tictactoe.client.gui;

import tictactoe.model.Move;
import tictactoe.model.Opponent;
import tictactoe.model.User;
import tictactoe.services.ITicTacToeClient;
import tictactoe.services.ITicTacToeServer;
import tictactoe.services.TicTacToeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TerminalController implements ITicTacToeClient
{
    private ITicTacToeServer server;
    private Terminal terminal;
    private User             user;

    public TerminalController(ITicTacToeServer server)
    {
        this.server = server;
    }

    public User getUser()
    {
        return user;
    }

    public void setTerminal(Terminal terminal)
    {
        this.terminal = terminal;
    }

    public void login(String username, String password) throws TicTacToeException
    {
        User userL = new User(username, password);

        try
        {
            Method method = server.getClass().getMethod("login", User.class, ITicTacToeClient.class);

            if (method != null)
            {
                user = (User) method.invoke(server, userL, this);
                reset();
            }
        }
        catch (SecurityException|NoSuchMethodException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
        {
            e.printStackTrace();
        }

//        user = server.login(userL, this);
//
//        reset();
    }

    public void logout()
    {
        try
        {
            server.logout(user, this);
        }
        catch (TicTacToeException e)
        {
            System.out.println("Logout error " + e);
        }
    }

    @Override
    public void gameStarted(Opponent opponent) throws TicTacToeException
    {
        System.out.println("Terminal controller game started");
        System.out.println(opponent);
        terminal.setOpponent(opponent);
    }

    @Override
    public void gameFinished(int winner_id) throws TicTacToeException
    {
        terminal.gameFinished(winner_id);
    }

    @Override
    public void gridUpdated(Move move)
    {
        terminal.gridUpdated(move);
    }

    public void startGame()
    {
        try
        {
            server.startGame(user);
            System.out.println("After TerminalController::startGame");
        }
        catch (TicTacToeException e)
        {
            e.printStackTrace();
        }
    }

    public void move(int index) throws TicTacToeException
    {
        server.move(new Move(user.getId(), terminal.getOpponent().getMark().equals("O"), index));
    }

    public void reset() throws TicTacToeException
    {
    }
}
