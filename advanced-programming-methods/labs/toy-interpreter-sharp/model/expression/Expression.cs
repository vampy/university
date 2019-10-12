using System;
using Exceptions;

namespace Model
{
    /// <summary>
    /// The type Expression.
    /// </summary>
    [Serializable()]
    public abstract class Expression
    {
        /// <summary>
        /// Eval int.
        /// </summary>
        /// <param name="table"> the table </param>
        /// <returns> the int </returns>
        /// <exception cref="ExpressionException"> the expression exception </exception>
        public abstract int Eval(IDictionary table, IHeap heap);

        public override abstract string ToString();

        /// <summary>
        /// Clone deep.
        /// </summary>
        /// <returns> the expression </returns>
        public abstract Expression CloneDeep();
    }
}

