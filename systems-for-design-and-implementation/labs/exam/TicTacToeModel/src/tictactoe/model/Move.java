package tictactoe.model;

import java.io.Serializable;

/**
 * Created by dan on 6/2/16.
 */
public class Move implements Serializable
{
    // otherwise is O
    private int player_id;
    private boolean is_x;

    // from 0 to 8
    private int cell;

    public Move(int player_id, boolean is_x, int cell)
    {
        this.player_id = player_id;
        this.is_x = is_x;
        this.cell = cell;
    }

    public int getPlayer_id()
    {
        return player_id;
    }

    public void setPlayer_id(int player_id)
    {
        this.player_id = player_id;
    }

    public boolean is_x()
    {
        return is_x;
    }

    public void setIs_x(boolean is_x)
    {
        this.is_x = is_x;
    }

    public int getCell()
    {
        return cell;
    }

    public void setCell(int cell)
    {
        this.cell = cell;
    }
}
