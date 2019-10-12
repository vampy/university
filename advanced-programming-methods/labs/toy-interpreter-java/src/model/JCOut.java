package model;

import java.util.ArrayList;

/**
 * The type Out.
 */
public class JCOut implements IList
{
    private ArrayList<String> list = new ArrayList<String>();

    /**
     * {@inheritDoc}
     */
    public void add(String value)
    {
        list.add(value);
    }

    /**
     * {@inheritDoc}
     */
    public String getLast()
    {
        int size = list.size();
        if (size == 0)
        {
            return "";
        }

        return list.get(size - 1);
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        list.clear();
    }

    /**
     * {@inheritDoc}
     */
    public String getAll()
    {
        StringBuilder b = new StringBuilder();

        for (String s : list)
        {
            b.append(s);
            b.append("\n");
        }

        return b.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        String out = "";
        int size = list.size();
        if (size == 0)
        {
            return out;
        }

        for (int i = 0; i < size - 1; i++)
        {
            out += list.get(i) + ", ";
        }
        out += list.get(size - 1);

        return out;
    }
}
