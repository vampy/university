using System;
using Exceptions;

namespace Model
{
    public class LockTable : ILockTable
    {
        private const int SIZE = 3;
        private int[] table = new int[3];

        public LockTable()
        {
            for (int i = 0; i < SIZE; i++)
            {
                table[i] = 0;
            }
        }

        private void checkBounds(int number)
        {
            if (number < 0 || number >= SIZE)
            {
                throw new Exception("Lock index is Out of bounds");
            }
        }

        public void Set(int number, int value)
        {
            table[number] = value;
        }

        public int Get(int number)
        {
            return table[number];
        }

        public override string ToString()
        {
            string ret = "";

            for (int i = 0; i < SIZE; i++)
            {
                @ret += table[i].ToString() + ", ";
            }

            return ret;
        }
    }
}

