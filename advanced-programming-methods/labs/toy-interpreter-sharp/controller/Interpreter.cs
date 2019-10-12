using System;
using Exceptions;
using Repository;
using Model;

namespace Controller
{
    /// <summary>
    /// The type Interpreter.
    /// </summary>
    public class Interpreter
    {
        /// <summary>
        /// The Output.
        /// </summary>
        private string output;
        /// <summary>
        /// The Repository.
        /// </summary>
        private IRepository repository;

        /// <summary>
        /// Instantiates a new Interpreter.
        /// </summary>
        /// <param name="repository"> the repository </param>
        public Interpreter(IRepository repository)
        {
            this.repository = repository;
        }

        public void SetCPObservers(IProgramStateObserver stack, IProgramStateObserver table, IProgramStateObserver @out)
        {
            repository.GetCP().RegisterObserver(stack, ProgramStateObservable.TYPE_STACK);
            repository.GetCP().RegisterObserver(table, ProgramStateObservable.TYPE_TABLE);
            repository.GetCP().RegisterObserver(@out, ProgramStateObservable.TYPE_OUT);
        }

        /// <summary>
        /// Getter for property 'output'.
        /// </summary>
        /// <returns> Value for property 'output'. </returns>
        public string Output
        {
            get
            {
                return output;
            }
        }

        private void StepStatement(Statement top)
        {
            try
            {
                ProgramState cp = repository.GetCP();

                if (top is AssignStatement)
                {
                    var assign = (AssignStatement) top;

                    int eval = assign.Expression.Eval(cp.Table, cp.Heap);

                    cp.AssignVar(assign.VarName, eval);
                }
                else if (top is CompStatement)
                {
                    var comp = (CompStatement) top;

                    cp.PushStack(comp.Second);
                    cp.PushStack(comp.First);
                }
                else if (top is IfStatement)
                {
                    var ifStatement = (IfStatement) top;

                    int eval = ifStatement.Condition.Eval(cp.Table, cp.Heap);

                    // condition is true
                    if (eval == 1)
                    {
                        cp.PushStack(ifStatement.Consequent);
                    }
                    else
                    {
                        cp.PushStack(ifStatement.Alternative);
                    }
                }
                else if (top is PrintStatement)
                {
                    var print = (PrintStatement) top;

                    int eval = print.Expression.Eval(cp.Table, cp.Heap);

                    cp.AddToOutput(Convert.ToString(eval));
                }
                else if (top is ForkStatement)
                {
                    var fork = (ForkStatement) top;

                    // add a new program and start it, the out and heap are the same
                    ProgramState program = new ProgramState(cp.Table.Clone(), cp.Out, new GExeStack(), cp.Heap, cp.LockTable);
                    program.SetProgram(fork.GetProgram());
                    program.Start();
                    repository.AddProgram(program);
                }
                else if (top is LockEnterStatement)
                {
                    var enter = (LockEnterStatement) top;

                    int nr = enter.GetNr();
                    if (cp.LockTable.Get(nr) == 0) // aquire lock
                    {
                        cp.LockTable.Set(nr, cp.GetID());
                    }
                    else // block
                    {
                        cp.PushStack(top);
                    }
                }
                else if (top is LockExitStatement)
                {
                    var exit = (LockExitStatement) top;

                    int nr = exit.GetNr();

                    try
                    {
                        if (cp.LockTable.Get(nr) == cp.GetID()) // delete lock
                        {
                            cp.LockTable.Set(nr, 0);
                        }
                    }
                    catch (Exception)
                    {
                        Console.WriteLine("DEBUG: log does not exist");
                        return;
                    }
                }
                else
                {
                    Console.WriteLine("ERROR: Statement({0}) is not of any valid instance", top.GetType());
                }
            }
            catch (ExpressionException e)
            {
                Console.WriteLine("ERROR: " + e.Message);
            }
        }

        private void StepStatementConcurrent()
        {
            do
            {
                if (repository.GetCP().ProgramFinished)
                {
                    repository.StopCP();
                }
                else
                {
                    StepStatement(repository.GetCP().PopStack());
                }
            } while (repository.NextCurrentProgram()); // move current program to next thread until we reset
        }

        /// <summary>
        /// Complete eval.
        /// </summary>
        /// <returns> the boolean </returns>
        public bool CompleteEval() // TODO use Program state
        {
            if (!repository.HasPrograms())
            {
                return false;
            }

            while (repository.HasPrograms())
            {
                StepStatementConcurrent();
            }
            output = repository.GetCP().Out.All;

            return true;
        }

        /// <summary>
        /// Debug eval.
        /// </summary>
        /// <returns> the boolean </returns>
        public bool DebugEval()
        {
            if (!repository.HasPrograms())
            {
                return false;
            }

            StepStatementConcurrent();
            output = GetRepositoryString();

            return true;
        }

        public void SetMain(Statement statement)
        {
            repository.SetMain(statement);
        }

        /// <summary>
        /// Start the main program
        /// </summary>
        public bool StartMain()
        {
            return repository.StartMain();
        }

        public void StopMain()
        {
            repository.StopMain();
        }

        public bool SerializeToFile()
        {
            return repository.SerializeToFile();
        }

        public bool SerializeFromFile()
        {
            return repository.SerializeFromFile();
        }

        public bool SaveToFile() 
        { 
            return repository.SaveToFile(); 
        }

        public string GetRepositoryString()
        {

            return repository.ToString();
        }
    }
}

