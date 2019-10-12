package model.statement;

/**
 * The type Statement.
 */
public abstract class Statement implements java.io.Serializable
{
    abstract public String toString();

    /**
     * Clone deep.
     *
     * @return the statement
     */
    abstract public Statement cloneDeep();
}
