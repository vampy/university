package model.statement;

public class LockUnlockStatement extends Statement
{
    private int nr;

    public LockUnlockStatement(int nr)
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
        return String.format("lock_unlock(%d)", nr);
    }

    @Override
    public Statement cloneDeep()
    {
        return new LockUnlockStatement(nr);
    }
}
