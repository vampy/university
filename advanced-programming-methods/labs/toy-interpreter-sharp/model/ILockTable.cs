using System;

namespace Model
{
    public interface ILockTable
    {
        void Set(int a, int b);

        int Get(int a);
    }
}
