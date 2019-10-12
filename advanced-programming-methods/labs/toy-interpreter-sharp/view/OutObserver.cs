using System;
using Model;

namespace View
{
    public class OutObserver : IProgramStateObserver
    {
        public void Update(object arg)
        {
            Console.WriteLine("OBSERVER: Out modified");
            Console.WriteLine(arg);
        }
    }
}

