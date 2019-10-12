using System;

namespace Model
{
    /// <summary>
    /// The type Operation.
    /// </summary>
    public static class Operation
    {
        /// <summary>
        /// The constant ADD.
        /// </summary>
        public const int ADD = 1;

        /// <summary>
        /// The constant SUBTRACT.
        /// </summary>
        public const int SUBTRACT = 2;

        /// <summary>
        /// The constant MULTIPLY.
        /// </summary>
        public const int MULTIPLY = 3;

        /// <summary>
        /// The constant DIVIDE.
        /// </summary>
        public const int DIVIDE = 4;

        /// <summary>
        /// Operation to string.
        /// </summary>
        /// <param name="op"> the op </param>
        /// <returns> the string </returns>
        public static string OperationToString(int op)
        {
            switch (op)
            {
                case ADD:
                    return "+";
                case SUBTRACT:
                    return "-";
                case MULTIPLY:
                    return "*";
                case DIVIDE:
                    return "/";
                default:
                    return "";
            }
        }
    }

}

