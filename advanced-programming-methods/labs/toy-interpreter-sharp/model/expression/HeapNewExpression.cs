using System;

namespace Model
{
    [Serializable()]
    public class HeapNewExpression : Expression
    {
        private int value;

        public HeapNewExpression(int value)
        {
            this.value = value;
        }

        /// <inheritdoc />
        public override int Eval(IDictionary table, IHeap heap)
        {
            return heap.Allocate(value);
        }

        /// <summary> {@inheritDoc} </summary>
        public override string ToString()
        {
            return string.Format("new({0})", value);
        }

        /// <summary> {@inheritDoc} </summary>
        public override Expression CloneDeep()
        {
            return new HeapNewExpression(value);
        }
    }
}

