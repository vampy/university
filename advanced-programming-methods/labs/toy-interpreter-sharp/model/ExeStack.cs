using System;
using Exceptions;

namespace Model
{
    /// <summary>
    /// The type Exe stack.
    /// </summary>
    public class ExeStack : IStack
    {
        private int size;
        private Statement[] elements;

        /// <summary> Constructs a new ExeStack. </summary>
        public ExeStack()
        {
            elements = new Statement[Constants.MAX_SIZE];
        }
            
        private void ensureBounds()
        {
            if (size > Constants.MAX_SIZE)
            {
                throw new StackException("Exe Stack out of bounds");
            }
        }

        /// <inheritdoc />
        public Statement Top()
        {
            ensureBounds();

            return elements[size - 1];
        }

        /// <inheritdoc />
        public Statement Pop()
        {
            ensureBounds();

            return elements[--size];
        }

        /// <inheritdoc />
        public void Push(Statement element)
        {
            ensureBounds();
            elements[size++] = element;
        }

        public void Clear()
        {
            elements = new Statement[Constants.MAX_SIZE];
            size = 0;
        }
        public string AllSave
        {
            get
            {
                return "";
            }
        }

        /// <inheritdoc />
        public bool Empty
        {
            get
            {
                return size == 0;
            }
        }
            
        public override string ToString()
        {
            if (size == 0)
            {
                return "|";
            }
                
            string @out = "|";
            for (int i = size - 1; i > 0; i--)
            {
                @out += elements[i] + "|";
            }
            @out += elements[0].ToString();

            return @out;
        }
    }
}

