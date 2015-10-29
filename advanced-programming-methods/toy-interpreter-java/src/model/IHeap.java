package model;

import exceptions.HeapException;

/**
 * The interface I heap.
 */
public interface IHeap extends java.io.Serializable
{
    /**
     * Allocate int.
     *
     * @param value the value
     * @return the int
     */
    public int allocate(int value);

    /**
     * Read the value from an address
     *
     * @param address the address
     * @return the int
     * @throws HeapException the heap exception
     */
    public int read(int address) throws HeapException;

    public void write(int address, int value) throws HeapException;

    /**
     * Clear void.
     */
    public void clear();
}