using System;
using Exceptions;

namespace Model
{
    /// <summary>
    /// The interface I dictionary.
    /// </summary>
    public interface IDictionary
    {
        /// <summary>
        /// Get int.
        /// </summary>
        /// <param name="key"> the key </param>
        /// <returns> the int </returns>
        /// <exception cref="DictionaryException"> the dictionary exception </exception>
        int Get(string key);

        /// <summary>
        /// Associates the specified value with the specified key in this map.Insert a key in the dictionary
        /// </summary>
        /// <param name="key"> the key </param>
        /// <param name="value"> the value </param>
        /// <exception cref="DictionaryException"> the dictionary exception </exception>
        void Put(string key, int value);

        /// <summary>
        /// Gets all.
        /// </summary>
        /// <returns> the all </returns>
        string AllSave {get;}

        IDictionary Clone();

        /// <summary>
        /// Clear void.
        /// </summary>
        void Clear();
    }

}

