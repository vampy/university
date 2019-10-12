package tictactoe.services;

import tictactoe.model.Move;
import tictactoe.model.User;

public interface ITicTacToeServer
{
    void startGame(User user) throws TicTacToeException;

    void move(Move move) throws TicTacToeException;

    User login(User user, ITicTacToeClient client) throws TicTacToeException;
    void logout(User user, ITicTacToeClient client) throws TicTacToeException;
}
