using System;
using System.Collections;
using System.Collections.Generic;

namespace Model
{
    [Serializable()]
    public class ProgramStateObservable
    {
        private List<IProgramStateObserver> StackObservers = new List<IProgramStateObserver>();
        private List<IProgramStateObserver> OutObservers = new List<IProgramStateObserver>();
        private List<IProgramStateObserver> TableObservers = new List<IProgramStateObserver>();

        public const int TYPE_STACK = 1;
        public const int TYPE_TABLE = 2;
        public const int TYPE_OUT   = 3;

        public void RegisterObserver(IProgramStateObserver observer, int type)
        {
            switch (type)
            {
                case TYPE_STACK:
                    StackObservers.Add(observer);
                    break;
                case TYPE_TABLE:
                    TableObservers.Add(observer);
                    break;
                case TYPE_OUT:
                    OutObservers.Add(observer);
                    break;
                default:
                    Console.WriteLine("ERROR: INVALID TYPE registerObserver");
                    break;
            }
        }

        public void NotifyObservers(Object arg, int type)
        {
            List<IProgramStateObserver> observers;
            switch (type)
            {
                case TYPE_STACK:
                    observers = StackObservers;
                    break;
                case TYPE_TABLE:
                    observers = TableObservers;
                    break;
                case TYPE_OUT:
                    observers = OutObservers;
                    break;
                default:
                    Console.WriteLine("ERROR: INVALID TYPE registerObserver");
                    return;
            }

            foreach (IProgramStateObserver observer in observers)
            {
                observer.Update(arg);
            }
        }
    }
}

