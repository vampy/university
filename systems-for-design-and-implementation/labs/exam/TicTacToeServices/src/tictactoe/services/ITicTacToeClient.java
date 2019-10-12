package tictactoe.services;

import tictactoe.model.Move;
import tictactoe.model.Opponent;

public interface ITicTacToeClient
{
    void gameStarted(Opponent opponent) throws TicTacToeException;
    void gameFinished(int winner_id) throws TicTacToeException;


    void gridUpdated(Move move);
//    void move(Move move) throws TicTacToeException;

//    void updateBook(Book book) throws LibraryException;
//
//    // used by librarian
//    void updateLoan(BookLoan loan) throws LibraryException;
}
