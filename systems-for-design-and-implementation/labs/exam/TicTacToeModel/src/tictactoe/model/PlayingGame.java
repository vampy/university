package tictactoe.model;

import java.util.ArrayList;

public class PlayingGame
{
    public int player_x;
    public int player_o;

    // index from 0 to 9
    public ArrayList<String> table;
    public int winner = -1;

    public PlayingGame(int player_x, int player_o)
    {
        this.player_x = player_x;
        this.player_o = player_o;
        table = new ArrayList<>();
        for (int i = 0; i < 9; i ++)
        {
            table.add("_");
        }
    }

    public int getWinner()
    {
        return winner;
    }

    public boolean checkWinner()
    {
        for (int i = 0; i < 3; i ++)
        {
            if (table.get(i).equals("_"))
            {
                continue;
            }

            // check row
            if (table.get(i).equals(table.get(i + 1)) && table.get(i + 1).equals(table.get(i + 2)))
            {
                if (table.get(i).equals("X"))
                {
                    winner = player_x;
                }
                else
                {
                    winner = player_o;
                }
                return true;
            }

            // check column
            if (table.get(i).equals(table.get(i + 3)) && table.get(i + 3).equals(table.get(i + 6)))
            {
                if (table.get(i).equals("X"))
                {
                    winner = player_x;
                }
                else
                {
                    winner = player_o;
                }
                return true;
            }
        }

        // check diagonal
        if (!table.get(0).equals("_") && table.get(0).equals(table.get(4)) && table.get(4).equals(table.get(8)))
        {
            if (table.get(0).equals("X"))
            {
                winner = player_x;
            }
            else
            {
                winner = player_o;
            }
            return true;
        }

        if (!table.get(2).equals("_") && table.get(2).equals(table.get(4)) && table.get(4).equals(table.get(6)))
        {
            if (table.get(2).equals("X"))
            {
                winner = player_x;
            }
            else
            {
                winner = player_o;
            }
            return true;
        }

        // check if draw
        for (String cell : table)
        {
            if (cell.equals("_"))
            {
                return false;
            }
        }

        System.out.println("WTF??");
        return true;
    }

    public void moveX(int cell)
    {
        table.set(cell, "X");
    }

    public void moveO(int cell)
    {
        table.set(cell, "O");
    }

    public int getPlayer_x()
    {
        return player_x;
    }

    public void setPlayer_x(int player_x)
    {
        this.player_x = player_x;
    }

    public int getPlayer_o()
    {
        return player_o;
    }

    public void setPlayer_o(int player_o)
    {
        this.player_o = player_o;
    }

    public ArrayList<String> getTable()
    {
        return table;
    }

    public void setTable(ArrayList<String> table)
    {
        this.table = table;
    }
}
