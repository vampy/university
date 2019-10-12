using System;

namespace Model
{
    /// <summary>
    /// The type Const expression.
    /// </summary>
    [Serializable()]
    public class ConstExpression : Expression
    {
        private readonly int value;

        /// <summary>
        /// Instantiates a new Const expression.
        /// </summary>
        /// <param name="value"> the value </param>
        public ConstExpression(int value)
        {
            this.value = value;
        }

        /// <inheritdoc />
        public override int Eval(IDictionary table, IHeap heap)
        {
            return this.value;
        }

        /// <summary> {@inheritDoc} </summary>
        public override string ToString()
        {
            return Convert.ToString(value);
        }

        /// <summary> {@inheritDoc} </summary>
        public override Expression CloneDeep()
        {
            return new ConstExpression(value);
        }
    }

}
