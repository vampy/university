using Exceptions;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System;

namespace Model
{
    [Serializable()]
    public class GHeap : IHeap
    {
        private List<int> heap = new List<int>();
    
        public int Allocate(int value)
        {
            int address = heap.Count;
            heap.Add(value);

            return address;
        }

        public int Read(int address)
        {
            if (address < 0 || address >= heap.Count)
            {
                throw new HeapException("Address is out of bounds");
            }

            return heap[address];
        }

        public void Clear()
        {
            heap.Clear();
        }

        public override string ToString()
        {
            string ret = "{";
            int size = heap.Count;
            if (size == 0)
            {
                return "{}";
            }

            for (int i = 0; i < size - 1; i++)
            {
                ret += string.Format("{0} -> {1},", i, heap[i]);
            }
            ret += string.Format("{0} -> {1}}}", size - 1, heap[size - 1]);

            return ret;
        }
    }
}

