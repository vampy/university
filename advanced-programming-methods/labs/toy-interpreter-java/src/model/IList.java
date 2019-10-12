package model;

/**
 * The interface I list.
 */
public interface IList extends java.io.Serializable
{
    /**
     * Add void.
     *
     * @param value the value
     */
    public void add(String value);

    /**
     * Gets last.
     *
     * @return the last
     */
    public String getLast();

    /**
     * Gets all.
     *
     * @return the all
     */
    public String getAll();

    /**
     * Clear void.
     */
    public void clear();
}
