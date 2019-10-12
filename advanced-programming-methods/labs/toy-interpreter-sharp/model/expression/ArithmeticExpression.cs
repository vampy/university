using System;
using Exceptions;

namespace Model
{
    /// <summary>
    /// The type Arithmetic expchression.
    /// </summary>
    [Serializable()]
    public class ArithmeticExpression : Expression
    {
        private readonly Expression left;
        private readonly Expression right;
        private readonly int @operator;

        /// <summary>
        /// Instantiates a new Arithmetic expression.
        /// </summary>
        /// <param name="left"> the left </param>
        /// <param name="right"> the right </param>
        /// <param name="operator"> the operator </param>
        public ArithmeticExpression(Expression left, Expression right, int @operator)
        {
            this.left = left;
            this.right = right;
            this.@operator = @operator;
        }

        /// <inheritdoc />
        public override int Eval(IDictionary table, IHeap heap)
        {
            int evalLeft = left.Eval(table, heap);
            int evalRight = right.Eval(table, heap);

            switch (@operator)
            {
                case Operation.ADD:
                    return evalLeft + evalRight;

                case Operation.SUBTRACT:
                    return evalLeft - evalRight;

                case Operation.DIVIDE:
                    return evalLeft / evalRight;

                case Operation.MULTIPLY:
                    return evalLeft * evalRight;

                default:
                    throw new ExpressionException("The operator is invalid");
            }
        }

        /// <inheritdoc />
        public override string ToString()
        {
            return string.Format("{0} {1} {2}", left, Operation.OperationToString(@operator), right);
        }

        /// <inheritdoc />
        public override Expression CloneDeep()
        {
            return new ArithmeticExpression(left.CloneDeep(), right.CloneDeep(), @operator);
        }
    }
}
