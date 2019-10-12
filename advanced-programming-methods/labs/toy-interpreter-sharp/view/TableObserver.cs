using System;
using Model;

namespace View
{
    public class TableObserver : IProgramStateObserver
    {
        public void Update(object arg)
        {
            Console.WriteLine("OBSERVER: Table modified");
            Console.WriteLine(arg);
        }
    }
}

