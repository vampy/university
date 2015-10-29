package model.statement;

public class ForkStatement extends Statement
{
    private Statement program;

    public ForkStatement(Statement program)
    {
        this.program = program;
    }

    /**
     * Getter for property 'program'.
     *
     * @return Value for property 'program'.
     */
    public Statement getProgram()
    {
        return program;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("fork(%s)", program.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement cloneDeep()
    {
        return new ForkStatement(program.cloneDeep());
    }
}
