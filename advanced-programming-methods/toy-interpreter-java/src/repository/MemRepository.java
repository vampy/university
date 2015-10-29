package repository;

import model.ProcTable;
import model.ProgramState;
import model.statement.Statement;

import java.io.*;
import java.util.ArrayList;

/**
 * The type Mem repository.
 */
public class MemRepository implements IRepository
{
    private ArrayList<ProgramState> programs            = new ArrayList<ProgramState>();
    private int                     currentProgramIndex = 0;
    private ProgramState currentProgram;
    private ProcTable procTable = new ProcTable();

    // Automatic program ID set.
    private int availableID = 0;

    private static final String filename_serialized = "program.toy.ser";
    private static final String filename            = "program.toy";

    /**
     * Instantiates a new Mem repository.
     *
     * @param firstProgram the program state
     */
    public MemRepository(ProgramState firstProgram)
    {
        currentProgram = firstProgram;
        setProgramID(currentProgram);
    }

    /**
     * Getter for property 'procTable'.
     *
     * @return Value for property 'procTable'.
     */
    public ProcTable getProcTable()
    {
        return procTable;
    }

    private void setProgramID(ProgramState program)
    {
        if (program.getID() == -1)
        {
            program.setID(availableID);
            availableID++; // next available one
        }
    }

    @Override
    public void addProgram(ProgramState program)
    {
        setProgramID(program);
        programs.add(program);
    }

    @Override
    public void removeProgram(ProgramState program)
    {
        programs.remove(program);
    }

    @Override
    public boolean nextCurrentProgram()
    {
        if (!hasPrograms())
        {
            return false;
        }

        // can execute a next program
        if (currentProgramIndex + 1 < programs.size())
        {
            // move forward
            currentProgramIndex++;
            currentProgram = programs.get(currentProgramIndex);
            return true;
        }

        // reset
        currentProgramIndex = 0;
        currentProgram = programs.get(currentProgramIndex);
        return false;
    }

    /** {@inheritDoc} */
    public void setMain(Statement statement)
    {
        currentProgram.setProgram(statement);
    }

    /** {@inheritDoc} */
    public boolean startMain() // it is called only once
    {
        assert programs.size() == 0;
        assert currentProgramIndex == 0;
        addProgram(currentProgram);

        return currentProgram.start();
    }

    public void stopMain()
    {
        programs.clear();
        currentProgramIndex = 0;

        currentProgram.stop(true);
    }

    /**
     * {@inheritDoc}
     */
    public ProgramState getCP()
    {
        return currentProgram;
    }

    /** {@inheritDoc} */
    public void stopCP() // step every program
    {
        removeProgram(currentProgram);

        currentProgramIndex--; // reduce index, we remove a program
        if (currentProgramIndex < 0)
        {
            currentProgramIndex = 0;
        }

        currentProgram.stop(false);
    }

    public boolean hasPrograms()
    {
        return programs.size() != 0;
    }

    @Override
    public boolean serializeToFile()
    {
        if (currentProgram == null)
        {
            return false;
        }

        try
        {
            FileOutputStream fileOut = new FileOutputStream(filename_serialized);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(currentProgram);

            out.close();
            fileOut.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("ERROR: could not write: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean serializeFromFile()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(filename_serialized);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            currentProgram = (ProgramState) in.readObject();

            in.close();
            fileIn.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("ERROR: could not read: " + e.getMessage());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("ERROR: could not find class: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean saveToFile()
    {
        if (currentProgram == null)
        {
            return false;
        }

        try
        {
            PrintWriter writer = new PrintWriter(filename);

            writer.println("// ExeStack content:");
            writer.println(currentProgram.getStack().getAllSave());

            writer.println("// SymTable content:");
            writer.println(currentProgram.getTable().peek().getAllSave());

            writer.println("// Out content:");
            writer.print(currentProgram.getOut().getAll());

            writer.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("ERROR: could not write: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String toString()
    {
        String out = "";

        if (programs.size() > 1)
        {
            for (int i = 1; i < programs.size(); i++)
            {
                out += String.format("%s\n", programs.get(i));
            }
        }
        out += String.format("currentProgram = %s\n", currentProgram);
        out += String.format("procTable = %s", procTable.toString());

        return out;
    }
}
