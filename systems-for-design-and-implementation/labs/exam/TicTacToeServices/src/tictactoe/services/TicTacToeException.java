package tictactoe.services;


public class TicTacToeException extends Exception
{
    public TicTacToeException()
    {
    }

    public TicTacToeException(String message)
    {
        super(message);
    }

    public TicTacToeException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
