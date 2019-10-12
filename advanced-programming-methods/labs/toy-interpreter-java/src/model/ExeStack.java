package model;

import exceptions.StackException;
import model.statement.Statement;

/**
 * The type Exe stack.
 */
public class ExeStack implements IStack
{
    private int         size;
    private Statement[] elements;

    /**
     * Constructs a new JCExeStack.
     */
    public ExeStack()
    {
        elements = new Statement[Constants.MAX_SIZE];
    }

    private void ensureBounds() throws StackException
    {
        if (size > Constants.MAX_SIZE)
        {
            throw new StackException("Exe Stack out of bounds");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Statement top() throws StackException
    {
        ensureBounds();

        return elements[size - 1];
    }

    /**
     * {@inheritDoc}
     */
    public Statement pop() throws StackException
    {
        ensureBounds();

        return elements[--size];
    }

    /**
     * {@inheritDoc}
     */
    public void push(Statement element) throws StackException
    {
        ensureBounds();
        elements[size++] = element;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return size == 0;
    }

    public String getAllSave()
    {
        return "NOT IMPLEMENTED\n";
    }

    public void clear()
    {
        elements = new Statement[Constants.MAX_SIZE];
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        if (size == 0)
        {
            return "|";
        }

        String out = "|";
        for (int i = size - 1; i > 0; i--)
        {
            out += elements[i].toString() + "|";
        }
        out += elements[0].toString();

        return out;
    }
}
