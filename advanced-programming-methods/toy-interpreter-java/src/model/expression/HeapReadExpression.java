package model.expression;

import exceptions.DictionaryException;
import exceptions.ExpressionException;
import exceptions.HeapException;
import model.IDictionary;
import model.IHeap;

public class HeapReadExpression extends Expression
{
    private String varName;

    /**
     * Instantiates a new Var expression.
     *
     * @param varName the var name
     */
    public HeapReadExpression(String varName)
    {
        this.varName = varName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int eval(IDictionary table, IHeap heap) throws ExpressionException
    {
        int address;
        try
        {
            address = table.get(varName);
        }
        catch (DictionaryException e)
        {
            throw new ExpressionException(
                String.format("Variable = '%s' is not defined", varName)
            );
        }

        try
        {
            return heap.read(address);
        }
        catch (HeapException e)
        {
            throw new ExpressionException(
                String.format("Address = %d is out of bounds", address)
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("r(%s)", varName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression cloneDeep()
    {
        return new HeapReadExpression(varName);
    }
}
