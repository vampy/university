package model;

/**
 * The type Out.
 */
public class Out implements IList
{
    private int size = 0;
    private String[] queue;

    /**
     * Constructs a new Out.
     */
    public Out()
    {
        queue = new String[Constants.MAX_SIZE];
    }

    /**
     * {@inheritDoc}
     */
    public void add(String value)
    {
        queue[size++] = value;
    }

    /**
     * {@inheritDoc}
     */
    public String getAll()
    {
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < size; i++)
        {
            b.append(queue[i]);
            b.append("\n");
        }

        return b.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getLast()
    {
        if (size == 0)
        {
            return "";
        }

        return queue[size - 1];
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        queue = new String[Constants.MAX_SIZE];
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        String out = "";
        if (size == 0)
        {
            return out;
        }

        for (int i = 0; i < size - 1; i++)
        {
            out += queue[i] + ", ";
        }
        out += queue[size - 1];

        return out;
    }
}
