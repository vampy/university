using System;

namespace Exceptions
{
    public class ExpressionException : Exception
    {
        public ExpressionException(string message) : base(message)
        {
        }
    }
}