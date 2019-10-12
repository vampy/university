using System;

namespace Model
{
    public interface IProgramStateObserver
    {
        void Update(object arg);
    }
}
