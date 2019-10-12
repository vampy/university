package repository;

import model.ProcTable;
import model.ProgramState;
import model.statement.Statement;

/**
 * The interface I repository.
 */
public interface IRepository
{

    public ProcTable getProcTable();

    public void addProgram(ProgramState program);

    public void removeProgram(ProgramState program);

    public boolean nextCurrentProgram();

    /**
     * Set the main program
     *
     * @param statement Value to set for property 'CP'.
     */
    public void setMain(Statement statement);

    /**
     * Start the main program. clear the threads list, etc
     */
    public boolean startMain();

    public void stopMain();

    /**
     * Getter for property 'currentProgramState'.
     * Get the current program
     *
     * @return Value for property 'currentProgramState'.
     */
    public ProgramState getCP();

    /**
     * Stop the current program
     */
    public void stopCP();

    public boolean hasPrograms();

    public boolean serializeToFile();

    public boolean serializeFromFile();

    public boolean saveToFile();

    /**
     * {@inheritDoc}
     */
    public String toString();
}
