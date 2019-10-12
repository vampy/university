package tictactoe.model;

import java.io.Serializable;

/**
 * Created by dan on 6/2/16.
 */
public class Opponent implements Serializable
{
    private int id;

    // X or O
    private String mark;

    public Opponent(int id, String mark)
    {
        this.id = id;
        this.mark = mark;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getMark()
    {
        return mark;
    }

    public void setMark(String mark)
    {
        this.mark = mark;
    }

    @Override
    public String toString()
    {
        return "Opponent{" +
            "id=" + id +
            ", mark='" + mark + '\'' +
            '}';
    }
}
