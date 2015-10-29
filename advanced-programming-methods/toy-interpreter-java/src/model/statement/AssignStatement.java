package model.statement;

import model.expression.Expression;

/**
 * The type Assign statement.
 */
public class AssignStatement extends Statement
{
    private String varName;

    private Expression expression;

    /**
     * Instantiates a new Assign statement.
     *
     * @param varName the var name
     * @param expression the expression
     */
    public AssignStatement(String varName, Expression expression)
    {
        this.varName = varName;
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

    /**
     * Getter for property 'varName'.
     *
     * @return Value for property 'varName'.
     */
    public String getVarName()
    {
        return varName;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return String.format("%s = %s", varName, expression.toString());
    }

    /** {@inheritDoc} */
    @Override
    public Statement cloneDeep()
    {
        return new AssignStatement(varName, expression.cloneDeep());
    }
}
