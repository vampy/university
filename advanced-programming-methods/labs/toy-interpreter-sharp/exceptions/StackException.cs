using System;

namespace Exceptions
{
    public class StackException : Exception
    {
        public StackException(string message) : base(message)
        {
        }
    }
}