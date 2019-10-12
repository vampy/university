package tictactoe.network.dto;

import java.io.Serializable;


public class TupleInt implements Serializable
{
    private int first;
    private int second;

    public TupleInt(int first, int second)
    {
        this.first = first;
        this.second = second;
    }

    public int getFirst()
    {
        return first;
    }

    public void setFirst(int first)
    {
        this.first = first;
    }

    public int getSecond()
    {
        return second;
    }

    public void setSecond(int second)
    {
        this.second = second;
    }

    @Override
    public String toString()
    {
        return "TupleInt{" +
            "first=" + first +
            ", second=" + second +
            '}';
    }
}
