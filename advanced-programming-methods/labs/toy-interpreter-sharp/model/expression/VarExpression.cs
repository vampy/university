using System;
using Exceptions;

namespace Model
{
    /// <summary>
    /// The type Var expression.
    /// </summary>
    [Serializable()]
    public class VarExpression : Expression
    {
        private readonly string varName;

        /// <summary>
        /// Instantiates a new Var expression.
        /// </summary>
        /// <param name="varName"> the var name </param>
        public VarExpression(string varName)
        {
            this.varName = varName;
        }

        /// <inheritdoc />
        public override int Eval(IDictionary table, IHeap heap)
        {
            try
            {
                return table.Get(varName);
            }
            catch (DictionaryException)
            {
                throw new ExpressionException(string.Format("Variable = '{0}' is not defined", varName));
            }
        }

        /// <inheritdoc />
        public override string ToString()
        {
            return varName;
        }

        /// <inheritdoc />
        public override Expression CloneDeep()
        {
            return new VarExpression(varName);
        }
    }

}

