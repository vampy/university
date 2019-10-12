using System;

namespace Model
{
    /// <summary>
    /// The interface I list.
    /// </summary>
    public interface IList
    {
        /// <summary>
        /// Add void.
        /// </summary>
        /// <param name="value"> the value </param>
        void Add(string value);

        /// <summary>
        /// Gets last.
        /// </summary>
        /// <returns> the last </returns>
        string Last {get;}

        /// <summary>
        /// Gets all.
        /// </summary>
        /// <returns> the all </returns>
        string All {get;}

        /// <summary>
        /// Clear void.
        /// </summary>
        void Clear();
    }
}
