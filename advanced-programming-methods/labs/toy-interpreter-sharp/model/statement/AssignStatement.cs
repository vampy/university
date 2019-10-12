using System;

namespace Model
{
    /// <summary>
    /// The type Assign statement.
    /// </summary>
    [Serializable()]
    public class AssignStatement : Statement
    {
        private readonly string varName;

        private readonly Expression expression;

        /// <summary>
        /// Instantiates a new Assign statement.
        /// </summary>
        /// <param name="varName"> the var name </param>
        /// <param name="expression"> the expression </param>
        public AssignStatement(string varName, Expression expression)
        {
            this.varName = varName;
            this.expression = expression;
        }

        /// <summary>
        /// Getter for property 'expression'.
        /// </summary>
        /// <returns> Value for property 'expression'. </returns>
        public Expression Expression
        {
            get
            {
                return expression;
            }
        }


        /// <summary>
        /// Getter for property 'varName'.
        /// </summary>
        /// <returns> Value for property 'varName'. </returns>
        public string VarName
        {
            get
            {
                return varName;
            }
        }
            
        /// <inheritdoc />
        public override string ToString()
        {
            return string.Format("{0} = {1}", varName, expression.ToString());
        }

        /// <inheritdoc />
        public override Statement CloneDeep()
        {
            return new AssignStatement(varName, expression.CloneDeep());
        }
    }
}

