package model.expression;

import exceptions.ExpressionException;
import model.IDictionary;
import model.IHeap;

/**
 * The type Const expression.
 */
public class ConstExpression extends Expression
{
    private int value;

    /**
     * Instantiates a new Const expression.
     *
     * @param value the value
     */
    public ConstExpression(int value)
    {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int eval(IDictionary table, IHeap heap) throws ExpressionException
    {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return Integer.toString(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression cloneDeep()
    {
        return new ConstExpression(value);
    }
}
