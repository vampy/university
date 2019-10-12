using System;
using Exceptions;
using System.Collections;
using System.Collections.Generic;

namespace Model
{
    /// <summary>
    /// The type Sym table.
    /// </summary>
    [Serializable()]
    public class GSymTable : IDictionary
    {
        private Dictionary<string, int> table = new Dictionary<string, int>();

        /// <summary>
        /// {@inheritDoc} </summary>
        public  int Get(string key)
        {
            if (!table.ContainsKey(key))
            {
                throw new DictionaryException(string.Format("Key = '{0}' does not exist", key));
            }

            return table[key];
        }

        public string AllSave
        {
            get
            {
                if (table.Count == 0)
                {
                    return "";
                }

                string @out = "";
                foreach(KeyValuePair<string, int> entry in table)
                {
                    @out += string.Format("{0} --> {1:D}\n", entry.Key, entry.Value);
                }

                return @out;
            }
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public virtual void Put(string key, int value)
        {
            table[key] = value;
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public virtual void Clear()
        {
            table.Clear();
        }

        public IDictionary Clone()
        {
            IDictionary dictionary = new GSymTable();

            foreach(KeyValuePair<string, int> entry in table)
            {
                dictionary.Put(entry.Key, entry.Value);
            }

            return dictionary;
        }

        /// <summary>
        /// {@inheritDoc} </summary>
        public override string ToString()
        {
            if (table.Count == 0)
            {
                return "{}";
            }

            string @out = "{";
            int current = 0, size = table.Count - 1;
            foreach(KeyValuePair<string, int> entry in table)
            {
                @out += string.Format("{0} => {1:D}", entry.Key, entry.Value);

                if (current != size)
                {
                    @out += ", ";
                }
                current++;
            }
            @out += "}";

            return @out;
        }
    }
}

