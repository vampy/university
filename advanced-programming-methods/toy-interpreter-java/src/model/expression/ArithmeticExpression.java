package model.expression;

import exceptions.ExpressionException;
import model.IDictionary;
import model.IHeap;

/**
 * The type Arithmetic expression.
 */
public class ArithmeticExpression extends Expression
{
    private Expression left;
    private Expression right;
    private int        operator;

    /**
     * Instantiates a new Arithmetic expression.
     *
     * @param left     the left
     * @param right    the right
     * @param operator the operator
     */
    public ArithmeticExpression(Expression left, Expression right, int operator)
    {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int eval(IDictionary table, IHeap heap) throws ExpressionException
    {
        int evalLeft = left.eval(table, heap);
        int evalRight = right.eval(table, heap);

        switch (operator)
        {
            case Operation.ADD:
                return evalLeft + evalRight;

            case Operation.SUBTRACT:
                return evalLeft - evalRight;

            case Operation.DIVIDE:
                return evalLeft / evalRight;

            case Operation.MULTIPLY:
                return evalLeft * evalRight;

            default:
                throw new ExpressionException("The operator is invalid");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("%s %s %s", left.toString(), Operation.operationToString(operator), right.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression cloneDeep()
    {
        return new ArithmeticExpression(left.cloneDeep(), right.cloneDeep(), operator);
    }
}
