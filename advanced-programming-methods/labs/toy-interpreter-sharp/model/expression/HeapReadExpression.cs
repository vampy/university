using System;
using Exceptions;

namespace Model
{
    [Serializable()]
    public class HeapReadExpression : Expression
    {
        private string varName;

        public HeapReadExpression(string varName)
        {
            this.varName = varName;
        }

        /// <inheritdoc />
        public override int Eval(IDictionary table, IHeap heap)
        {
            int address;
            try
            {
                address = table.Get(varName);
            }
            catch (DictionaryException)
            {
                throw new ExpressionException(string.Format("Variable = '{0}' is not defined", varName));
            }

            try
            {
                return heap.Read(address);
            }
            catch (HeapException)
            {
                throw new ExpressionException(string.Format("Address = {0} is out of bounds", address));
            }
        }

        /// <summary> {@inheritDoc} </summary>
        public override string ToString()
        {
            return string.Format("r({0})", varName);
        }

        /// <summary> {@inheritDoc} </summary>
        public override Expression CloneDeep()
        {
            return new HeapReadExpression(varName);
        }
    }
}

