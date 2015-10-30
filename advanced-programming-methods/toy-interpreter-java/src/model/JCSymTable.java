package model;

import exceptions.DictionaryException;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Sym table.
 */
public class JCSymTable implements IDictionary
{
    private HashMap<String, Integer> table = new HashMap<String, Integer>();

    /**
     * {@inheritDoc}
     */
    public int get(String key) throws DictionaryException
    {
        if (!table.containsKey(key))
        {
            throw new DictionaryException(String.format("Key = '%s' does not exist", key));
        }

        return table.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public void put(String key, int value)
    {
        table.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        table.clear();
    }

    @Override
    public IDictionary clone()
    {
        IDictionary dictionary = new JCSymTable();

        for (Map.Entry<String, Integer> entry : table.entrySet())
        {
            String key = entry.getKey();
            int value = entry.getValue();

            dictionary.put(key, value);
        }

        return dictionary;
    }

    public String getAllSave()
    {
        if (table.size() == 0)
        {
            return "";
        }

        String out = "";
        for (Map.Entry<String, Integer> entry : table.entrySet())
        {
            out += String.format("%s --> %d\n", entry.getKey(), entry.getValue());
        }

        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        if (table.size() == 0)
        {
            return "{}";
        }

        String out = "{";
        int current = 0, size = table.size() - 1;
        for (Map.Entry<String, Integer> entry : table.entrySet())
        {
            out += String.format("%s => %d", entry.getKey(), entry.getValue());

            if (current != size)
            {
                out += ", ";
            }
            current++;
        }
        out += "}";

        return out;
    }
}
