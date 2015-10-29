package model;

import model.statement.Statement;

import java.util.ListIterator;
import java.util.Stack;

/**
 * The type Exe stack.
 */
public class JCExeStack implements IStack
{
    private Stack<Statement> stack;

    /** Constructs a new ExeStack. */
    public JCExeStack()
    {
        stack = new Stack<Statement>();
    }

    /** {@inheritDoc} */
    public Statement top()
    {
        return stack.peek();
    }

    /** {@inheritDoc} */
    public Statement pop()
    {
        return stack.pop();
    }

    /** {@inheritDoc} */
    public void push(Statement element)
    {
        stack.push(element);
    }

    /** {@inheritDoc} */
    public boolean isEmpty()
    {
        return stack.empty();
    }

    /**
     * {@inheritDoc}
     */
    public String getAllSave()
    {
        if (stack.empty())
        {
            return "";
        }

        StringBuilder str = new StringBuilder();
        ListIterator li = stack.listIterator(stack.size());
        while (li.hasPrevious())
        {
            Statement top = (Statement) li.previous();
            str.append(top.toString());
            str.append("\n");
        }

        return str.toString();
    }

    public void clear()
    {
        stack.clear();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (stack.size() == 0)
        {
            return "|";
        }

        String out = "|";
        int current = 0, size = stack.size() - 1;
        for(Statement statement : stack)
        {
            out += statement.toString();
            if (current != size)
            {
                out += "|";
            }
            current++;
        }

        return out;
    }
}
