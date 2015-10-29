package model.expression;

import exceptions.ExpressionException;
import model.IDictionary;
import model.IHeap;
import view.Console;

public class ReadExpression extends Expression
{
    @Override
    public int eval(IDictionary table, IHeap heap) throws ExpressionException
    {
        return Console.getInt("Integer for ToyLanguage: ");
    }

    @Override
    public String toString()
    {
        return "read()";
    }

    @Override
    public Expression cloneDeep()
    {
        return new ReadExpression();
    }
}
