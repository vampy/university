package tictactoe.services.impl;

import tictactoe.model.Move;
import tictactoe.model.Opponent;
import tictactoe.model.PlayingGame;
import tictactoe.model.User;
import tictactoe.persistance.repository.IUserRepository;
import tictactoe.services.ITicTacToeClient;
import tictactoe.services.ITicTacToeServer;
import tictactoe.services.TicTacToeException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Used by the server
public class TicTacToeServerImpl implements ITicTacToeServer
{
    private IUserRepository userRepository;

    private Map<Integer, ITicTacToeClient> loggedClients;

    // all the clients that are waiting to pair
    private ArrayList<Integer> waitingClients;

    // all the current played games
    private ArrayList<PlayingGame> games;

    public TicTacToeServerImpl(IUserRepository userRepository)
    {
        this.userRepository = userRepository;
        this.loggedClients = new ConcurrentHashMap<>();
        this.waitingClients = new ArrayList<>();
        games = new ArrayList<PlayingGame>();
    }

    @Override
    public synchronized User login(User user, ITicTacToeClient client) throws TicTacToeException
    {
        boolean isValid = userRepository.verifyUser(user);
        if (!isValid)
        {
            throw new TicTacToeException("Authentication failed");
        }

        // we get the full user
        User new_user = userRepository.getUserByUsername(user.getUsername());
        if (isLogged(new_user))
        {
            throw new TicTacToeException("User already logged in");
        }
        loggedClients.put(new_user.getId(), client);

        return new_user;
    }

    private boolean isLogged(User user)
    {
        return loggedClients.get(user.getId()) != null;
    }

    @Override
    public synchronized void logout(User user, ITicTacToeClient client) throws TicTacToeException
    {
        ITicTacToeClient localClient = loggedClients.remove(user.getId());
        if (localClient == null)
        {
            throw new TicTacToeException("User " + user.getId() + " is not logged in.");
        }
    }

    @Override
    public void startGame(User user) throws TicTacToeException
    {
        waitingClients.add(user.getId());

        if (waitingClients.size() < 2)
        {
            System.out.println("Not enough clients");
            return;
        }

        // only two clients at a time
        assert waitingClients.size() == 2;

        int user1 = waitingClients.get(0);
        int user2 = waitingClients.get(1);

        ITicTacToeClient first = loggedClients.get(user1);
        ITicTacToeClient second = loggedClients.get(user2);

        first.gameStarted(new Opponent(user2, "X"));
        second.gameStarted(new Opponent(user1, "O"));
        waitingClients.clear();

        // TODO check if already playing
        games.add(new PlayingGame(user2, user1));
    }

    public PlayingGame findplayerPlaying(int id)
    {
        for (PlayingGame game : games)
        {
            if (game.getPlayer_o() == id || game.getPlayer_x() == id)
            {
                return game;
            }
        }

        return null;
    }

    @Override
    public void move(Move move) throws TicTacToeException
    {
        PlayingGame game = findplayerPlaying(move.getPlayer_id());
        assert game != null;

        // announce opposite player
        int announce_player;

        // place move
        if (move.is_x())
        {
            game.moveX(move.getCell());
            announce_player = game.getPlayer_o();
        }
        else
        {
            game.moveO(move.getCell());
            announce_player = game.getPlayer_x();
        }


        // check if game finished
        if (game.checkWinner())
        {
            // TODO update table database
            loggedClients.get(game.getPlayer_o()).gameFinished(game.getWinner());
            loggedClients.get(game.getPlayer_x()).gameFinished(game.getWinner());
        }
        else
        {
            // announce other player of update
            loggedClients.get(announce_player).gridUpdated(move);
        }
    }
}
