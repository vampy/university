using System;
using Exceptions;

namespace Model
{
    /// <summary>
    /// The type Sym table.
    /// </summary>
    public class SymTable : IDictionary
    {
        private Pair[] pairs;
        private int size = 0;

        /// 
        /// <param name="key">
        /// @return </param>
        /// <exception cref="DictionaryException"> </exception>
        private Pair FindPair(string key)
        {
            for (int i = 0; i < size; i++)
            {
                Pair pair = pairs[i];
                if (pair.Key.Equals(key))
                {
                    return pair;
                }
            }

            throw new DictionaryException("Pair not found");
        }

        public string AllSave
        {
            get
            {
                return "";
            }
        }

        /// <summary> Constructs a new SymTable. </summary>
        public SymTable()
        {
            pairs = new Pair[Constants.MAX_SIZE];
        }

        /// <inheritdoc />
        public int Get(string key)
        {
            Pair pair;

            try
            {
                pair = FindPair(key);
            }
            catch (DictionaryException)
            {
                throw new DictionaryException(string.Format("Key = '{0}' does not exist", key));
            }

            return pair.Value;
        }

        /// <inheritdoc />
        public void Put(string key, int value)
        {
            Pair pair = null;

            try
            {
                pair = FindPair(key);
            }
            catch (DictionaryException)
            {

            }

            if (pair != null)
            {
                pair.Value = value;
            }
            else
            {
                Pair pairi = new Pair(key, value);

                pairs[size++] = pairi;
            }
        }

        public IDictionary Clone()
        {
            IDictionary dictionary = new SymTable();

            for (int i = 0; i < size; i++)
            {
                Pair pair = pairs[i];
                dictionary.Put(pair.Key, pair.Value);
            }

            return dictionary;
        }

        /// <inheritdoc />
        public void Clear()
        {
            pairs = new Pair[Constants.MAX_SIZE];
            size = 0;
        }
            
        public override string ToString()
        {
            if (size == 0)
            {
                return "{}";
            }

            string @out = "{";

            for (int i = 0; i < size - 1; i++)
            {
                @out += pairs[i] + ", ";
            }
            @out += pairs[size - 1] + "}";

            return @out;
        }
    }
}

