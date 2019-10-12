using System;

namespace Model
{
    public interface IHeap
    {
        /// <summary>
        /// Allocate int.
        /// </summary>
        /// <param name="value">Value.</param>
        int Allocate(int value);

        /// <summary>
        /// Read the value from an address
        /// </summary>
        /// <param name="address">Address.</param>
        int Read(int address);

        void Clear();
    }
}
   