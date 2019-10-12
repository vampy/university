package model.statement;


public class NullStatement extends Statement
{
    @Override
    public String toString()
    {
        return "";
    }

    @Override
    public Statement cloneDeep()
    {
        return new NullStatement();
    }
}
