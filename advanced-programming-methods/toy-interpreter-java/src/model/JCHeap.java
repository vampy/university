package model;

import exceptions.HeapException;

import java.util.ArrayList;
import java.util.List;

public class JCHeap implements IHeap
{
    private List<Integer> heap = new ArrayList<Integer>(256);

    private void checkBounds(int address)
    {
        if (address < 0 || address >= heap.size())
        {
            throw new HeapException("Address is out of bounds");
        }
    }

    @Override
    public int allocate(int value)
    {
        int address = heap.size();
        heap.add(value);

        return address;
    }

    @Override
    public int read(int address) throws HeapException
    {
        this.checkBounds(address);

        return heap.get(address);
    }

    @Override
    public void write(int address, int value) throws HeapException
    {
        this.checkBounds(address);

        heap.set(address, value);
    }

    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        heap.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        String out = "{";
        int size = heap.size();
        if (size == 0)
        {
            return "{}";
        }

        for (int i = 0; i < size - 1; i++)
        {
            out += String.format("%d -> %d,", i, heap.get(i));
        }
        out += String.format("%d -> %d}", size - 1, heap.get(size - 1));

        return out;
    }
}
