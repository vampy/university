using System;

namespace Model
{
    /// <summary>
    /// The type Comp statement.
    /// </summary>
    [Serializable()]
    public class CompStatement : Statement
    {
        private Statement first;

        private Statement second;

        /// <summary>
        /// Instantiates a new Comp statement.
        /// </summary>
        /// <param name="first"> the first </param>
        /// <param name="second"> the second </param>
        public CompStatement(Statement first, Statement second)
        {
            this.first = first;
            this.second = second;
        }

        /// <summary>
        /// Getter for property 'first'.
        /// </summary>
        /// <returns> Value for property 'first'. </returns>
        public virtual Statement First
        {
            get
            {
                return first;
            }
            set
            {
                this.first = value;
            }
        }


        /// <summary>
        /// Getter for property 'second'.
        /// </summary>
        /// <returns> Value for property 'second'. </returns>
        public virtual Statement Second
        {
            get
            {
                return second;
            }
            set
            {
                this.second = value;
            }
        }


        /// <inheritdoc />
        public override string ToString()
        {
            return string.Format("{0}; {1}", first.ToString(), second.ToString());
        }

        /// <inheritdoc />
        public override Statement CloneDeep()
        {
            return new CompStatement(first.CloneDeep(), second.CloneDeep());
        }
    }

}

