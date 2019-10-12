using System;

namespace Exceptions
{
    public class DictionaryException : Exception
    {
        public DictionaryException(string message) : base(message)
        {
        }
    }
}