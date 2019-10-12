package model;

import model.statement.Statement;

import java.util.Stack;

/**
 * The type Program state.
 */
public class ProgramState extends ProgramStateObservable implements java.io.Serializable
{
    private boolean isRunning = false;
    private Stack<IDictionary> table;
    private IList              out;
    private IStack             stack;
    private IHeap              heap;
    private Statement          program;

    private int ID = -1; // id of the current thread

    /**
     * Instantiates a new Program state.
     *
     * @param table the table
     * @param out   the out
     * @param stack the stack
     */
    public ProgramState(Stack<IDictionary> table, IList out, IStack stack, IHeap heap)
    {
        this.table = table;
        this.out = out;
        this.stack = stack;
        this.heap = heap;
    }

    /**
     * Getter for property 'ID'.
     *
     * @return Value for property 'ID'.
     */
    public int getID()
    {
        return ID;
    }

    /**
     * Setter for property 'ID'.
     *
     * @param ID Value to set for property 'ID'.
     */
    public void setID(int ID)
    {
        this.ID = ID;
    }

    /**
     * Setter for property 'currentProgram'.
     *
     * @param currentProgram Value to set for property 'currentProgram'.
     */
    public void setProgram(Statement currentProgram)
    {
        this.program = currentProgram;
    }

    /**
     * Start void.
     */
    public boolean start()
    {
        if (program == null)
        {
            return false;
        }
        if (isRunning)
        {
            return false;
        }

        stack.push(program.cloneDeep());
        isRunning = true;
        notifyObservers(stack, TYPE_STACK);

        return true;
    }

    /**
     * Stop void.
     */
    public void stop(boolean clearAll)
    {
        isRunning = false;

        if (clearAll)
        {
            out.clear();
            heap.clear();
            stack.clear();
            table.peek().clear();
        }
    }

    /**
     * Getter for property 'programFinished'.
     *
     * @return Value for property 'programFinished'.
     */
    public boolean isProgramFinished()
    {
        return stack.isEmpty();
    }

    /**
     * Getter for property 'stack'.
     *
     * @return Value for property 'stack'.
     */
    public IStack getStack()
    {
        return stack;
    }

    /**
     * Getter for property 'table'.
     *
     * @return Value for property 'table'.
     */
    public Stack<IDictionary> getTable()
    {
        return table;
    }

    /**
     * Getter for property 'heap'.
     *
     * @return Value for property 'heap'.
     */
    public IHeap getHeap()
    {
        return heap;
    }

    /**
     * Getter for property 'out'.
     *
     * @return Value for property 'out'.
     */
    public IList getOut()
    {
        return out;
    }

    public void assignVar(String key, int value)
    {
        table.peek().put(key, value);
        notifyObservers(table, TYPE_TABLE);
    }

    public void addToOutput(String value)
    {
        out.add(value);
        notifyObservers(out, TYPE_OUT);
    }

    public Statement popStack()
    {
        Statement pop = stack.pop();
        notifyObservers(stack, TYPE_STACK);

        return pop;
    }

    public void pushStack(Statement statement)
    {
        stack.push(statement);
        notifyObservers(stack, TYPE_STACK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "ProgramState {" +
            "\n\tID    = " + ID +
            "\n\ttable = " + table +
            "\n\tout   = " + out +
            "\n\tstack = " + stack +
            "\n\theap  = " + heap +
            "\n}";
    }
}
