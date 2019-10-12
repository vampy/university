using System.Collections;
using System.Collections.Generic;
using System.Text;
using System;

namespace Model
{
    /// <summary>
    /// The type Out.
    /// </summary>
    [Serializable()]
    public class GOut : IList
    {
        private List<string> list = new List<string>();
    
        /// <summary>
        /// {@inheritDoc} </summary>
        public void Add(string value)
        {
            list.Add(value);
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public string All
        {
            get
            {
                StringBuilder b = new StringBuilder();

                foreach (string s in list)
                {
                    b.Append(s);
                    b.Append("\n");
                }

                return b.ToString();
            }
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public string Last
        {
            get
            {
                int size = list.Count;
                if (size == 0)
                {
                    return "";
                }

                return list[size - 1];
            }
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public void Clear()
        {
            list.Clear();
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public override string ToString()
        {
            string @out = "";
            int size = list.Count;
            if (size == 0)
            {
                return @out;
            }

            for (int i = 0; i < size - 1; i++)
            {
                @out += list[i] + ", ";
            }
            @out += list[size - 1];

            return @out;
        }
    }
}