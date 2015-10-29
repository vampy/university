package model.expression;

/**
 * The type Operation.
 */
public class Operation
{
    /**
     * The constant ADD.
     */
    public static final int ADD      = 1;
    /**
     * The constant SUBTRACT.
     */
    public static final int SUBTRACT = 2;
    /**
     * The constant MULTIPLY.
     */
    public static final int MULTIPLY = 3;
    /**
     * The constant DIVIDE.
     */
    public static final int DIVIDE   = 4;

    /**
     * Operation to string.
     *
     * @param op the op
     * @return the string
     */
    public static String operationToString(int op)
    {
        switch (op)
        {
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            default:
                return "";
        }
    }
}