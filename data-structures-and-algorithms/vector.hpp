#ifndef DYNAMIC_ARRAY_H_
#define DYNAMIC_ARRAY_H_
#include <stdexcept>
#include <string>

#include "util.hpp"

class DynamicArrayException : public std::runtime_error
{
};
class DynamicArrayException;

template <typename TElement>
class DynamicArray
{
public:
    /**
     * DynamicArray consructor
     * @param int initial_capacity - the initial build capacity, to allocate
     */
    DynamicArray(int initial_capacity = 128)
    {
        if (initial_capacity <= 0) // reset to default if initial capacity is small
        {
            initial_capacity = 256;
        }
        this->capacity = initial_capacity;
        this->data = new TElement[this->capacity];
        this->length = 0;
        printDebug("DynamicArray constructed");
    }

    DynamicArray(const DynamicArray& from)
    {
        this->capacity = from.capacity;
        this->length = from.length;
        this->data = new TElement[this->capacity];
        for (int i = 0; i < from.length; i++)
        {
            this->data[i] = from.data[i];
        }
    }

    DynamicArray& operator=(const DynamicArray& second)
    {
        if (this == &second)
        {
            return *this; // protect against self assignment (a = a)
        }
        // delete old data
        delete[] this->data;

        // copy data
        this->capacity = second.capacity;
        this->length = second.length;
        this->data = new TElement[this->capacity];
        for (int i = 0; i < second.length; i++)
        {
            this->data[i] = second.data[i];
        }

        // return current object value
        return *this;
    }

    ~DynamicArray()
    {
        delete[] this->data;
        printDebug("DynamicArray destroyed");
    }

    /**
     * Adds an element in the vector, to the end
     * TElement - element to add
     */
    void add(TElement element)
    {
        this->resize();

        this->data[this->length] = element;
        this->length++;
    }

    /**
     * Insert an alement at a given position
     * @param int position - the position
     * @param TElement element
     */
    void insert(unsigned int position, TElement element)
    {
        this->resize();

        // move to the right
        for (unsigned int i = this->length; i > position; i--)
        {
            this->data[i] = this->data[i - 1];
        }
        // this->length++;
        this->data[position] = element;
    }

    /**
     * Get element from vector from a given position
     * @param int position - the position
     * @return TElement the element we are searching for
     */
    TElement get(unsigned int position)
    {
        this->validatePosition(position);
        return this->data[position];
    }

    /**
     * Set the given position with the value of element
     * @param int position - the position we want to replace
     * @param TElement elemnt - the new element
     */
    void set(unsigned int position, TElement element)
    {
        this->validatePosition(position);
        this->data[position] = element;
    }

    /**
     * Remove element at a given position
     * @param int position - the position we want to replace
     */
    void remove(int position)
    {
        this->validatePosition(position);

        // move to the left minus the last one
        for (unsigned int i = position; i < this->length; i++)
        {
            this->data[i] = this->data[i + 1];
        }
        this->length--;
    }

    /**
     * @return unsigned int - the number the elements
     */
    unsigned int getLength() const { return this->length; }

    /**
     * @return unsigned int - the allocated capacity
     */
    unsigned int getCapacity() const { return this->capacity; }

    /**
     * @return TElement* - the internal array representation
     */
    TElement* getInternalData() const { return this->data; }

protected:
    /**
     * Resize the internal data if necessary
     * If the length is larger or equal to the capacity
     */
    void resize()
    {
        if (this->length >= this->capacity)
        {
            // create mew buffer
            this->capacity *= 2;
            TElement* temp_data = new TElement[this->capacity];

            for (unsigned int i = 0; i < this->length; i++)
            {
                temp_data[i] = this->data[i];
            }

            // delete old
            delete[] this->data;
            this->data = temp_data;
            printDebug("DynamicArray resized to " + std::to_string(this->capacity));
        }
    }

    /**
     * Validate current position throw exception if invalid
     * @param unsigned int position
     * @throws DynamicArrayException
     */
    void validatePosition(unsigned int position)
    {
        if (position < 0 || position >= this->length)
        {
            printError("DynamicArray: Position out of range, position = " + std::to_string(position));
            throw("DynamicArray: Position out of range");
        }
    }

    TElement* data;
    unsigned int length;
    unsigned int capacity;
};

#endif // DYNAMIC_ARRAY_H_
