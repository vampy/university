package model.statement;

import model.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class CallProcStatement extends Statement
{
    private String           name;
    private List<Expression> expressions;

    public CallProcStatement(String name, List<Expression> expressions)
    {
        this.name = name;
        this.expressions = expressions;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Getter for property 'expressions'.
     *
     * @return Value for property 'expressions'.
     */
    public List<Expression> getExpressions()
    {
        return expressions;
    }

    @Override
    public String toString()
    {
        return String.format("call %s(%s)", name, expressions.toString());
    }

    @Override
    public Statement cloneDeep()
    {
        List<Expression> clone = new ArrayList<Expression>(expressions.size());
        for (Expression expr: expressions)
        {
            clone.add(expr.cloneDeep());
        }

        return new CallProcStatement(name, clone);
    }
}
