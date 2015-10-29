package model.statement;

import model.expression.Expression;

/**
 * The type Print statement.
 */
public class PrintStatement extends Statement
{
    private Expression expression;

    /**
     * Instantiates a new Print statement.
     *
     * @param expression the expression
     */
    public PrintStatement(Expression expression)
    {
        this.expression = expression;
    }

    /**
     * Getter for property 'expression'.
     *
     * @return Value for property 'expression'.
     */
    public Expression getExpression()
    {
        return expression;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("Print(%s)", expression.toString());
    }

    /** {@inheritDoc} */
    @Override
    public Statement cloneDeep()
    {
        return new PrintStatement(expression.cloneDeep());
    }
}
