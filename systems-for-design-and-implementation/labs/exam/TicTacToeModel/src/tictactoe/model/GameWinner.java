package tictactoe.model;

import java.io.Serializable;

/**
 * Created by dan on 6/2/16.
 */
public class GameWinner implements Serializable
{
    public int winner_id;

    public GameWinner(int winner_id)
    {
        this.winner_id = winner_id;
    }

    public int getWinner_id()
    {
        return winner_id;
    }

    public void setWinner_id(int winner_id)
    {
        this.winner_id = winner_id;
    }
}
