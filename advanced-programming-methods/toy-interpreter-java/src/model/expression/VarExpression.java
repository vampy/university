package model.expression;

import exceptions.DictionaryException;
import exceptions.ExpressionException;
import model.IDictionary;
import model.IHeap;

/**
 * The type Var expression.
 */
public class VarExpression extends Expression
{
    private String varName;

    /**
     * Instantiates a new Var expression.
     *
     * @param varName the var name
     */
    public VarExpression(String varName)
    {
        this.varName = varName;
    }

    /** {@inheritDoc} */
    @Override
    public int eval(IDictionary table, IHeap heap) throws ExpressionException
    {
        try
        {
            return table.get(varName);
        }
        catch (DictionaryException e)
        {
            throw new ExpressionException(
                String.format("Variable = '%s' is not defined", varName)
            );
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return varName;
    }

    /** {@inheritDoc} */
    @Override
    public Expression cloneDeep()
    {
        return new VarExpression(varName);
    }
}
