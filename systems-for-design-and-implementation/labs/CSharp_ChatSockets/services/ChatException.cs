using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace chat.services
{
    public class ChatException : Exception
    {
        public ChatException():base() { }

        public ChatException(String msg) : base(msg) { }

        public ChatException(String msg, Exception ex) : base(msg, ex) { }

    }
}
