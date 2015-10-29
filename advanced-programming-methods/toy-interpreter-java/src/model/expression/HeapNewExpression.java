package model.expression;

import exceptions.ExpressionException;
import model.IDictionary;
import model.IHeap;

public class HeapNewExpression extends Expression
{
    private Expression value;

    public HeapNewExpression(Expression value)
    {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int eval(IDictionary table, IHeap heap) throws ExpressionException
    {
        return heap.allocate(value.eval(table, heap));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("new(%s)", value.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression cloneDeep()
    {
        return new HeapNewExpression(value.cloneDeep());
    }
}
