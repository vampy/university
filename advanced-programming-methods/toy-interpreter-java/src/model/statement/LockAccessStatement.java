package model.statement;

public class LockAccessStatement extends Statement
{
    private int nr;

    public LockAccessStatement(int nr)
    {
        this.nr = nr;
    }

    /**
     * Getter for property 'nr'.
     *
     * @return Value for property 'nr'.
     */
    public int getNr()
    {
        return nr;
    }

    @Override
    public String toString()
    {
        return String.format("lock_access(%d)", nr);
    }

    @Override
    public Statement cloneDeep()
    {
        return new LockAccessStatement(nr);
    }
}
