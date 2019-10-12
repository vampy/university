using System;

namespace Model
{
    /// <summary>
    /// The type Program state.
    /// </summary>
    [Serializable()] 
    public class ProgramState : ProgramStateObservable
    {
        private bool isRunning = false;
        private readonly IDictionary table;
        private readonly IList @out;
        private readonly IStack stack;
        private readonly IHeap heap;
        private readonly ILockTable lockTable;
        private Statement program;
        private int ID = -1;

        /**
     * Getter for property 'ID'.
     *
     * @return Value for property 'ID'.
     */
        public int GetID()
        {
            return ID;
        }

        /**
     * Setter for property 'ID'.
     *
     * @param ID Value to set for property 'ID'.
     */
        public void SetID(int ID)
        {
            this.ID = ID;
        }

        /// <summary>
        /// Instantiates a new Program state.
        /// </summary>
        /// <param name="table"> the table </param>
        /// <param name="out"> the out </param>
        /// <param name="stack"> the stack </param>
        public ProgramState(IDictionary table, IList @out, IStack stack, IHeap heap, ILockTable lockTable)
        {
            this.table = table;
            this.@out = @out;
            this.stack = stack;
            this.heap = heap;
            this.lockTable = lockTable;
        }

        public void SetProgram(Statement program)
        {
            this.program = program;
        }

        /// <summary>
        /// Start void.
        /// </summary>
        public virtual bool Start()
        {
            if (isRunning)
            {
                return false;
            }
            if (program == null)
            {
                return false;
            }

            stack.Push(program.CloneDeep());
            isRunning = true;
            NotifyObservers(stack, ProgramStateObservable.TYPE_STACK);

            return true;
        }

        /// <summary>
        /// Stop void.
        /// </summary>
        public void Stop(bool clearAll)
        {
            isRunning = false;

            if (clearAll)
            {
                @out.Clear();
                heap.Clear();
                table.Clear();
                stack.Clear();
            }
        }

        /// <summary>
        /// Getter for property 'programFinished'.
        /// </summary>
        /// <returns> Value for property 'programFinished'. </returns>
        public bool ProgramFinished
        {
            get
            {
                return stack.Empty;
            }
        }

        /// <summary>
        /// Getter for property 'stack'.
        /// </summary>
        /// <returns> Value for property 'stack'. </returns>
        public IStack Stack
        {
            get
            {
                return stack;
            }
        }

        /// <summary>
        /// Getter for property 'table'.
        /// </summary>
        /// <returns> Value for property 'table'. </returns>
        public IDictionary Table
        {
            get
            {
                return table;
            }
        }

        /// <summary>
        /// Getter for property 'out'.
        /// </summary>
        /// <returns> Value for property 'out'. </returns>
        public IList Out
        {
            get
            {
                return @out;
            }
        }

        public ILockTable LockTable
        {
            get
            {
                return lockTable;
            }
        }

        public IHeap Heap
        {
            get
            {
                return heap;
            }
        }

        public void AssignVar(string key, int value)
        {
            Table.Put(key, value);
            NotifyObservers(Table, TYPE_TABLE);
        }

        public void AddToOutput(string value)
        {
            Out.Add(value);
            NotifyObservers(Out, TYPE_OUT);
        }

        public Statement PopStack()
        {
            Statement pop = Stack.Pop();
            NotifyObservers(Stack, TYPE_STACK);

            return pop;
        }

        public void PushStack(Statement statement)
        {
            Stack.Push(statement);
            NotifyObservers(Stack, TYPE_STACK);
        }

        public override string ToString()
        {
            return "ProgramState {" 
                + "\n\tID    = " + ID
                + "\n\ttable = " + table 
                + "\n\tout   = " + @out 
                + "\n\tstack = " + stack 
                + "\n\theap  = " + heap 
                + "\n\tlock  = " + lockTable
                + "\n}";
        }
    }
}
