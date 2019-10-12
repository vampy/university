using System;

namespace Model
{
    /// <summary>
    /// The type Pair.
    /// </summary>
    public class Pair
    {
        private string key;
        private int value;

        /// <summary>
        /// Instantiates a new Pair.
        /// </summary>
        /// <param name="key"> the key </param>
        /// <param name="value"> the value </param>
        public Pair(string key, int value)
        {
            this.value = value;
            this.key = key;
        }

        /// <summary>
        /// Getter for property 'value'.
        /// </summary>
        /// <returns> Value for property 'value'. </returns>
        public virtual int Value
        {
            get
            {
                return value;
            }
            set
            {
                this.value = value;
            }
        }


        /// <summary>
        /// Getter for property 'key'.
        /// </summary>
        /// <returns> Value for property 'key'. </returns>
        public virtual string Key
        {
            get
            {
                return key;
            }
            set
            {
                this.key = value;
            }
        }
            
        public override string ToString()
        {
            return string.Format("{0} => {1:D}", key, value);
        }
    }
}

