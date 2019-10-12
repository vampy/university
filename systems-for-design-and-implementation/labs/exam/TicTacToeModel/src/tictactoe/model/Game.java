package tictactoe.model;

import java.io.Serializable;

public class Game implements Serializable
{
    private int id = -1;
    private int user_1_id = -1;
    private int user_2_id = -2;
    private int result = 0;

    public Game(int id, int user_1_id, int user_2_id, int result)
    {
        this.id = id;
        this.user_1_id = user_1_id;
        this.user_2_id = user_2_id;
        this.result = result;
    }

    public Game(int user_1_id, int user_2_id, int result)
    {
        this.user_1_id = user_1_id;
        this.user_2_id = user_2_id;
        this.result = result;
    }

    public Game(int user_1_id, int user_2_id)
    {
        this.user_1_id = user_1_id;
        this.user_2_id = user_2_id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUser_1_id()
    {
        return user_1_id;
    }

    public void setUser_1_id(int user_1_id)
    {
        this.user_1_id = user_1_id;
    }

    public int getUser_2_id()
    {
        return user_2_id;
    }

    public void setUser_2_id(int user_2_id)
    {
        this.user_2_id = user_2_id;
    }

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "Game{" +
            "id=" + id +
            ", user_1_id=" + user_1_id +
            ", user_2_id=" + user_2_id +
            ", result=" + result +
            '}';
    }
}
