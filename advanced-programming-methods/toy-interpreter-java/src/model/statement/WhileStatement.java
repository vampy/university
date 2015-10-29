package model.statement;

import model.expression.Expression;

public class WhileStatement extends Statement
{
    private Expression condition;
    private Statement body;

    public WhileStatement(Expression condition, Statement body)
    {
        this.condition = condition;
        this.body = body;
    }

    /**
     * Getter for property 'condition'.
     *
     * @return Value for property 'condition'.
     */
    public Expression getCondition()
    {
        return condition;
    }

    /**
     * Getter for property 'body'.
     *
     * @return Value for property 'body'.
     */
    public Statement getBody()
    {
        return body;
    }

    @Override
    public String toString()
    {
        return String.format("while(%s) %s ", condition.toString(), body.toString());
    }

    @Override
    public Statement cloneDeep()
    {
        return new WhileStatement(condition.cloneDeep(), body.cloneDeep());
    }
}
