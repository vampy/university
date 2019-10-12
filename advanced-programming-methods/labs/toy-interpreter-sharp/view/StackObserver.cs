using System;
using Model;

namespace View
{
    public class StackObserver : IProgramStateObserver
    {
        public void Update(object arg)
        {
            Console.WriteLine("OBSERVER: Stack modified");
            Console.WriteLine(arg);
        }
    }
}

