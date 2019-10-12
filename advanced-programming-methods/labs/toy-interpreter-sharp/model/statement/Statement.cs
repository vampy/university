using System;

namespace Model
{
    /// <summary>
    /// The type Statement.
    /// </summary>
    [Serializable()]
    public abstract class Statement
    {
        public override abstract string ToString();

        /// <summary>
        /// Clone deep.
        /// </summary>
        /// <returns> the statement </returns>
        public abstract Statement CloneDeep();
    }
}