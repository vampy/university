using System;
using Exceptions;
using System.Collections.Generic;

namespace Model
{
    /// <summary>
    /// The type Exe stack.
    /// </summary>
    [Serializable()]
    public class GExeStack : IStack
    {
        private Stack<Statement> stack = new Stack<Statement>();

        /// <summary>
        /// {@inheritDoc} </summary>
        public Statement Top()
        {
            return stack.Peek();
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public Statement Pop()
        {
            return stack.Pop();
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public void Push(Statement element)
        {
            stack.Push(element);
        }

        public void Clear()
        {
            stack.Clear();
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public bool Empty
        {
            get
            {
                return stack.Count == 0;
            }
        }

        public string AllSave
        {
            get
            {
                string @out = "";
                foreach (Statement statement in stack)
                {
                    @out += statement.ToString();
                }
                
                return @out;
            }
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public override string ToString()
        {
            if (stack.Count == 0)
            {
                return "|";
            }

            string @out = "|";
            int current = 0, size = stack.Count - 1;
            foreach (Statement statement in stack)
            {
                @out += statement.ToString();
                if (current != size)
                {
                    @out += "|";
                }
                current++;
            }

            return @out;
        }
    }
}

