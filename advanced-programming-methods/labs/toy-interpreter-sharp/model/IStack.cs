using Exceptions;

namespace Model
{
    /// <summary>
    /// The interface I stack.
    /// </summary>
    public interface IStack
    {
        /// <summary>
        /// Top statement.
        /// </summary>
        /// <returns> the statement </returns>
        /// <exception cref="StackException"> the stack exception </exception>
        Statement Top();

        /// <summary>
        /// Pop statement.
        /// </summary>
        /// <returns> the statement </returns>
        /// <exception cref="StackException"> the stack exception </exception>
        Statement Pop();

        /// <summary>
        /// Push void.
        /// </summary>
        /// <param name="element"> the element </param>
        /// <exception cref="StackException"> the stack exception </exception>
        void Push(Statement element);

        void Clear();

        /// <summary>
        /// Getter for saving the stack into a file
        /// </summary>
        /// <value>All save.</value>
        string AllSave {get;}

        /// <summary>
        /// Is empty.
        /// </summary>
        /// <returns> the boolean </returns>
        bool Empty {get;}
    }
}
    