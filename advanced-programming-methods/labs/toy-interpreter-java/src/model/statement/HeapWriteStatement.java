package model.statement;

import model.expression.Expression;

public class HeapWriteStatement extends Statement
{
    private String     varName;
    private Expression value;

    /**
     * Getter for property 'varName'.
     *
     * @return Value for property 'varName'.
     */
    public String getVarName()
    {
        return varName;
    }

    /**
     * Getter for property 'value'.
     *
     * @return Value for property 'value'.
     */
    public Expression getValue()
    {
        return value;
    }

    public HeapWriteStatement(String varName, Expression value)
    {
        this.varName = varName;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return String.format("w(%s, %s)", varName, value.toString());
    }

    @Override
    public Statement cloneDeep()
    {
        return new HeapWriteStatement(varName, value.cloneDeep());
    }
}
