using System;
using System.Text;

namespace Model
{
    /// <summary>
    /// The type Out.
    /// </summary>
    public class Out : IList
    {
        private int size = 0;
        private string[] queue;

        /// <summary> Constructs a new Out. </summary>
        public Out()
        {
            queue = new string[Constants.MAX_SIZE];
        }

        /// <inheritdoc />
        public virtual void Add(string value)
        {
            queue[size++] = value;
        }

        /// <inheritdoc />
        public string All
        {
            get
            {
                StringBuilder b = new StringBuilder();
            
                for (int i = 0; i < size; i++)
                {
                    b.Append(queue[i]);
                    b.Append("\n");
                }

                return b.ToString();
            }
        }

        /// <inheritdoc />
        public string Last
        {
            get
            {
                if (size == 0)
                {
                    return "";
                }

                return queue[size - 1];
            }
        }

        /// <inheritdoc />
        public void Clear()
        {
            queue = new string[Constants.MAX_SIZE];
            size = 0;
        }
            
        public override string ToString()
        {
            string @out = "";
            if (size == 0)
            {
                return @out;
            }

            for (int i = 0; i < size - 1; i++)
            {
                @out += queue[i] + ", ";
            }
            @out += queue[size - 1];

            return @out;
        }
    }

}

