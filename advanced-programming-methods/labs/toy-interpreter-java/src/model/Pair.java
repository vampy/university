package model;

/**
 * The type Pair.
 */
public class Pair
{
    private String key;
    private int    value;

    /**
     * Instantiates a new Pair.
     *
     * @param key   the key
     * @param value the value
     */
    public Pair(String key, int value)
    {
        this.value = value;
        this.key = key;
    }

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Setter for property 'value'.
     *
     * @param value Value to set for property 'value'.
     */
    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * Getter for property 'key'.
     *
     * @return Value for property 'key'.
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Setter for property 'key'.
     *
     * @param key Value to set for property 'key'.
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("%s => %d", key, value);
    }
}
