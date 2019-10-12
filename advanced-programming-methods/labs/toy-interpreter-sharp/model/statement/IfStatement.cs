using System;

namespace Model
{
    /// <summary>
    /// The type If statement.
    /// </summary>
    [Serializable()]
    public class IfStatement : Statement
    {
        private readonly Expression condition;

        private readonly Statement consequent;

        private readonly Statement alternative;

        /// <summary>
        /// Instantiates a new If statement.
        /// </summary>
        /// <param name="condition"> the condition </param>
        /// <param name="consequent"> the consequent </param>
        /// <param name="alternative"> the alternative </param>
        public IfStatement(Expression condition, Statement consequent, Statement alternative)
        {
            this.condition = condition;
            this.consequent = consequent;
            this.alternative = alternative;
        }

        /// <summary>
        /// Gets condition.
        /// </summary>
        /// <returns> the condition </returns>
        public Expression Condition
        {
            get
            {
                return condition;
            }
        }

        /// <summary>
        /// Gets consequent.
        /// </summary>
        /// <returns> the consequent </returns>
        public Statement Consequent
        {
            get
            {
                return consequent;
            }
        }

        /// <summary>
        /// Gets alternative.
        /// </summary>
        /// <returns> the alternative </returns>
        public Statement Alternative
        {
            get
            {
                return alternative;
            }
        }

        /// <inheritdoc />
        public override string ToString()
        {
            return "IfStatement{" + "condition=" + condition + ", consequent=" + consequent + ", alternative=" + alternative + '}';
        }

        /// <inheritdoc />
        public override Statement CloneDeep()
        {
            return new IfStatement(condition.CloneDeep(), consequent.CloneDeep(), alternative.CloneDeep());
        }
    }
}
