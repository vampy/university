package controller;

import exceptions.ExpressionException;
import model.*;
import model.expression.Expression;
import model.statement.*;
import repository.IRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


/**
 * The type Interpreter.
 */
public class Interpreter
{
    /**
     * The Output.
     */
    private String output;

    // Maybe make this into a class like heap, etc
    private List<Integer> lockTable = new ArrayList<Integer>(Collections.nCopies(5, 0));

    /**
     * The Repository.
     */
    private IRepository repository;
    // TODO move to repository

    /**
     * Instantiates a new Interpreter.
     *
     * @param repository the repository
     */
    public Interpreter(IRepository repository)
    {
        this.repository = repository;
    }

    public void setCPObservers(IProgramStateObserver stack, IProgramStateObserver table, IProgramStateObserver out)
    {
        repository.getCP().registerObserver(stack, ProgramStateObservable.TYPE_STACK);
        repository.getCP().registerObserver(table, ProgramStateObservable.TYPE_TABLE);
        repository.getCP().registerObserver(out, ProgramStateObservable.TYPE_OUT);
    }

    /**
     * Getter for property 'output'.
     *
     * @return Value for property 'output'.
     */
    public String getOutput()
    {
        return output;
    }

    private void stepStatement(Statement top)
    {
        try
        {
            ProgramState cp = repository.getCP();

            if (top instanceof AssignStatement)
            {
                AssignStatement assign = (AssignStatement) top;

                int eval = assign.getExpression().eval(cp.getTable().peek(), cp.getHeap());

                cp.assignVar(assign.getVarName(), eval);
            }
            else if (top instanceof CompStatement)
            {
                CompStatement comp = (CompStatement) top;

                cp.pushStack(comp.getSecond());
                cp.pushStack(comp.getFirst());
            }
            else if (top instanceof IfStatement)
            {
                IfStatement ifStatement = (IfStatement) top;

                int eval = ifStatement.getCondition().eval(cp.getTable().peek(), cp.getHeap());

                // condition is true
                if (eval == 1)
                {
                    cp.pushStack(ifStatement.getConsequent());
                }
                else
                {
                    cp.pushStack(ifStatement.getAlternative());
                }
            }
            else if (top instanceof PrintStatement)
            {
                PrintStatement print = (PrintStatement) top;

                int eval = print.getExpression().eval(cp.getTable().peek(), cp.getHeap());

                cp.addToOutput(Integer.toString(eval));
            }
            else if (top instanceof ForkStatement)
            {
                ForkStatement fork = (ForkStatement) top;

                // add a new program and start it, the out and heap are the
                Stack<IDictionary> table = new Stack<IDictionary>();
                table.push(cp.getTable().peek().clone());

                ProgramState program = new ProgramState(table, cp.getOut(), new JCExeStack(), cp.getHeap());
                program.setProgram(fork.getProgram());
                program.start();
                repository.addProgram(program);
            }
            else if (top instanceof HeapWriteStatement)
            {
                HeapWriteStatement write = (HeapWriteStatement) top;

                int address = cp.getTable().peek().get(write.getVarName());
                int value = write.getValue().eval(cp.getTable().peek(), cp.getHeap());
                cp.getHeap().write(address, value);
            }
            else if (top instanceof LockAccessStatement)
            {
                LockAccessStatement lock = (LockAccessStatement) top;
                int nr = lock.getNr();

                if (lockTable.get(nr) == 0) // aquire lock
                {
                    lockTable.set(nr, cp.getID());
                }
                else // block
                {
                    cp.pushStack(lock);
                }
            }
            else if (top instanceof LockUnlockStatement)
            {
                LockUnlockStatement lock = (LockUnlockStatement) top;
                int nr = lock.getNr();

                if (lockTable.get(nr) == cp.getID()) // delete lock
                {
                    lockTable.set(nr, 0);
                }
                // do nothing
            }
            else if (top instanceof WhileStatement)
            {
                WhileStatement wstat = (WhileStatement) top;

                int cond = wstat.getCondition().eval(cp.getTable().peek(), cp.getHeap());
                if (cond != 0) // while is not zero
                {
                    cp.pushStack(wstat);
                    cp.pushStack(wstat.getBody());
                }
            }
            else if (top instanceof DeclareProcStatement)
            {
                DeclareProcStatement proc = (DeclareProcStatement) top;

                repository.getProcTable().add(proc);
            }
            else if (top instanceof CallProcStatement)
            {
                CallProcStatement call = (CallProcStatement) top;
                DeclareProcStatement proc = repository.getProcTable().get(call.getName());
                IDictionary table = new SymTable();

                for (int i = 0; i < call.getExpressions().size(); i++)
                {
                    // assign variables for this procedure
                    String varName = proc.getVariables().get(i);
                    Expression expression = call.getExpressions().get(i);
                    int eval = expression.eval(cp.getTable().peek(), cp.getHeap());

                    table.put(varName, eval);
                }

                // push new symtable
                cp.getTable().push(table);
                cp.pushStack(new ReturnStatement()); // pop from stack
                cp.pushStack(proc.getBody()); // execute body
            }
            else if (top instanceof ReturnStatement)
            {
                cp.getTable().pop(); // remove current stack
            }
            else
            {
                System.out.format("ERROR: Statement(%s) is not of any valid instance\n", top.getClass());
            }
        }
        catch (ExpressionException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void stepStatementConcurrent()
    {
        do
        {
            if (repository.getCP().isProgramFinished())
            {
                repository.stopCP();
            }
            else
            {
                stepStatement(repository.getCP().popStack());
            }
        } while (repository.nextCurrentProgram()); // move current program to next thread until we reset
    }

    /**
     * Complete eval.
     *
     * @return the boolean
     */
    public boolean completeEval()
    {
        if (!repository.hasPrograms())
        {
            return false;
        }

        while (repository.hasPrograms())
        {
            stepStatementConcurrent();
        }
        output = repository.getCP().getOut().getAll();

        return true;
    }

    /**
     * Debug eval.
     *
     * @return the boolean
     */
    public boolean debugEval()
    {
        if (!repository.hasPrograms())
        {
            return false;
        }

        output = getRepositoryString();
        stepStatementConcurrent();

        return true;
    }

    /**
     * Start program. The current loaded one
     */
    public boolean startMain()
    {
        return repository.startMain();
    }

    public void stopMain()
    {
        repository.stopMain();
    }

    /**
     * Setter for property 'currentProgram'.
     *
     * @param statement Value to set for property 'currentProgram'.
     */
    public void setMain(Statement statement)
    {
        repository.setMain(statement);
    }

    public boolean serializeToFile()
    {
        return repository.serializeToFile();
    }

    public boolean serializeFromFile()
    {
        return repository.serializeFromFile();
    }

    public boolean saveToFile()
    {
        return repository.saveToFile();
    }

    /**
     * Getter for property 'programStateString'.
     *
     * @return Value for property 'programStateString'.
     */
    public String getRepositoryString()
    {
        return repository.toString();
    }
}
