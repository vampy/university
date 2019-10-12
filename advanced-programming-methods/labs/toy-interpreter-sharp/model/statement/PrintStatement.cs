using System;

namespace Model
{
    /// <summary>
    /// The type Print statement.
    /// </summary>
    [Serializable()]
    public class PrintStatement : Statement
    {
        private readonly Expression expression;

        /// <summary>
        /// Instantiates a new Print statement.
        /// </summary>
        /// <param name="expression"> the expression </param>
        public PrintStatement(Expression expression)
        {
            this.expression = expression;
        }

        /// <summary>
        /// Getter for property 'expression'.
        /// </summary>
        /// <returns> Value for property 'expression'. </returns>
        public virtual Expression Expression
        {
            get
            {
                return expression;
            }
        }

        /// <inheritdoc />
        public override string ToString()
        {
            return string.Format("Print({0})", expression);
        }

        /// <inheritdoc />
        public override Statement CloneDeep()
        {
            return new PrintStatement(expression.CloneDeep());
        }
    }

}
