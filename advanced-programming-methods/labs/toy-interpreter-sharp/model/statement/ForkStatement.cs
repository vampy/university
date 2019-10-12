using System;

namespace Model
{
    [Serializable()]
    public class ForkStatement : Statement
    {
        private Statement program;

        public ForkStatement(Statement program)
        {
            this.program = program;
        }

        public Statement GetProgram()
        {
            return program;
        }

        /// <inheritdoc />
        public override string ToString()
        {
            return string.Format("fork({0})", program.ToString());
        }

        /// <inheritdoc />
        public override Statement CloneDeep()
        {
            return new ForkStatement(program.CloneDeep());
        }
    }
}

