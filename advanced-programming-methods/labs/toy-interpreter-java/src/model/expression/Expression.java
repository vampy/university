package model.expression;

import exceptions.ExpressionException;
import model.IDictionary;
import model.IHeap;

/**
 * The type Expression.
 */
public abstract class Expression implements java.io.Serializable
{
    /**
     * Eval int.
     *
     * @param table the table
     * @param heap  the heap
     * @return the int
     * @throws ExpressionException the expression exception
     */
    abstract public int eval(IDictionary table, IHeap heap) throws ExpressionException;

    abstract public String toString();

    /**
     * Clone deep.
     *
     * @return the expression
     */
    abstract public Expression cloneDeep();
}
