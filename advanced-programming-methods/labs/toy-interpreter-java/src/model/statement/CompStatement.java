package model.statement;

/**
 * The type Comp statement.
 */
public class CompStatement extends Statement
{
    private Statement first;

    private Statement second;

    /**
     * Instantiates a new Comp statement.
     *
     * @param first  the first
     * @param second the second
     */
    public CompStatement(Statement first, Statement second)
    {
        this.first = first;
        this.second = second;
    }

    /**
     * Getter for property 'first'.
     *
     * @return Value for property 'first'.
     */
    public Statement getFirst()
    {
        return first;
    }

    /**
     * Getter for property 'second'.
     *
     * @return Value for property 'second'.
     */
    public Statement getSecond()
    {
        return second;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("%s; %s", first.toString(), second.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement cloneDeep()
    {
        return new CompStatement(first.cloneDeep(), second.cloneDeep());
    }
}
