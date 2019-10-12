package model.statement;

public class ReturnStatement extends Statement
{
    @Override
    public String toString()
    {
        return "RETURN";
    }

    @Override
    public Statement cloneDeep()
    {
        return new ReturnStatement();
    }
}
