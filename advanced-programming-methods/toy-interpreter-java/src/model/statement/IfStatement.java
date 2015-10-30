package model.statement;

import model.expression.Expression;

/**
 * The type If statement.
 */
public class IfStatement extends Statement
{
    private Expression condition;

    private Statement consequent;

    private Statement alternative;

    /**
     * Instantiates a new If statement.
     *
     * @param condition   the condition
     * @param consequent  the consequent
     * @param alternative the alternative
     */
    public IfStatement(Expression condition, Statement consequent, Statement alternative)
    {
        this.condition = condition;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public Expression getCondition()
    {
        return condition;
    }

    /**
     * Gets consequent.
     *
     * @return the consequent
     */
    public Statement getConsequent()
    {
        return consequent;
    }

    /**
     * Gets alternative.
     *
     * @return the alternative
     */
    public Statement getAlternative()
    {
        return alternative;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "IfStatement{" +
            "condition=" + condition +
            ", consequent=" + consequent +
            ", alternative=" + alternative +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement cloneDeep()
    {
        return new IfStatement(condition.cloneDeep(), consequent.cloneDeep(), alternative.cloneDeep());
    }
}
