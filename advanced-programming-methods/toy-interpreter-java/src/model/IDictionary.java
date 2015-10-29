package model;

import exceptions.DictionaryException;

/**
 * The interface I dictionary.
 */
public interface IDictionary extends java.io.Serializable
{
    /**
     * Get int.
     *
     * @param key the key
     * @return the int
     * @throws DictionaryException the dictionary exception
     */
    public int get(String key) throws DictionaryException;

    public String getAllSave();

    /**
     * Associates the specified value with the specified key in this map.Insert a key in the dictionary
     *
     * @param key the key
     * @param value the value
     * @throws DictionaryException the dictionary exception
     */
    public void put(String key, int value);

    public IDictionary clone();

    /**
     * Clear void.
     */
    public void clear();
}