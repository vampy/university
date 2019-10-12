using System;

namespace Model
{
    public class LockExitStatement : Statement
    {
        private int nr;
   
        public LockExitStatement(int nr)
        {
            this.nr = nr;
        }

        public int GetNr()
        {
            return nr;
        }

        /// <inheritdoc />
        public override string ToString()
        {
            return string.Format("lockExit({0})", nr);
        }

        /// <inheritdoc />
        public override Statement CloneDeep()
        {
            return new LockExitStatement(nr);
        }
    }
}

