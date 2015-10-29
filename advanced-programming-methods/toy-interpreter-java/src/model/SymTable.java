package model;

import exceptions.DictionaryException;

/**
 * The type Sym table.
 */
public class SymTable implements IDictionary
{
    private Pair[] pairs;
    private int size = 0;

    /**
     *
     * @param key
     * @return
     * @throws DictionaryException
     */
    private Pair findPair(String key) throws DictionaryException
    {
        for (int i = 0; i < size; i++)
        {
            Pair pair = pairs[i];
            if (pair.getKey().equals(key))
            {
                return pair;
            }
        }

        throw new DictionaryException("Pair not found");
    }

    /** Constructs a new SymTable. */
    public SymTable()
    {
        pairs = new Pair[Constants.MAX_SIZE];
    }

    /** {@inheritDoc} */
    public int get(String key) throws DictionaryException
    {
        Pair pair;

        try
        {
            pair = findPair(key);
        }
        catch (DictionaryException e)
        {
            throw new DictionaryException(String.format("Key = '%s' does not exist", key));
        }

        return pair.getValue();
    }

    public String getAllSave()
    {
        return "NOT IMPLEMENTED\n";
    }

    /** {@inheritDoc} */
    public void put(String key, int value)
    {
        Pair foundPair = null;

        try
        {
            foundPair = findPair(key);
        }
        catch (DictionaryException e)
        {
        }

        if (foundPair != null)
        {
            foundPair.setValue(value);
        }
        else
        {
            // key does not already exist
            Pair pair = new Pair(key, value);

            pairs[size++] = pair;
        }
    }

    @Override
    public IDictionary clone()
    {
        IDictionary dictionary = new SymTable();

        for (int i = 0; i < size; i++)
        {
            Pair pair = pairs[i];
            dictionary.put(pair.getKey(), pair.getValue());
        }

        return dictionary;
    }

    /** {@inheritDoc} */
    public void clear()
    {
        pairs = new Pair[Constants.MAX_SIZE];
        size = 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (size == 0)
        {
            return "{}";
        }

        String out = "{";

        for (int i = 0; i < size - 1; i++)
        {
            out += pairs[i].toString() + ", ";
        }
        out += pairs[size - 1].toString() + "}";

        return out;
    }
}
