#ifndef DYNAMIC_ARRAY_H_
#define DYNAMIC_ARRAY_H_
#include <stdexcept>

#include "util.hpp"

class DynamicArrayException : public std::runtime_error
{
};
class DynamicArrayException;

template <typename TElement> class DynamicArray
{
public:
    DynamicArray(int initial_capacity = 32)
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
        for (size_t i = 0; i < from.length; i++)
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
        for (size_t i = 0; i < second.length; i++)
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
     * Adds an element in vector
     * TElement - element to add
     */
    void add(TElement element)
    {
        this->resize();

        this->data[this->length] = element;
        this->length++;
    }

    /**
     * Returns element from vector from given position
     * size_t position - the position
     * returns TElement from position
     */
    TElement get(size_t position)
    {
        this->validatePosition(position);
        return this->data[position];
    }

    void remove(size_t position)
    {
        this->validatePosition(position);
        for (size_t i = position; i < this->length - 1; i++)
        {
            this->data[i] = this->data[i + 1];
        }
        this->length--;
    }

    /**
     * returns the number the elements
     */
    size_t getLength() const { return this->length; }

    TElement* getInternalData() { return this->data; }

protected:
    /**
     * Resize the internal data if necessary
     */
    void resize()
    {
        if (this->length >= this->capacity)
        {
            // create mew buffer
            this->capacity *= 2;
            TElement* temp_data = new TElement[this->capacity];

            for (size_t i = 0; i < this->length; i++)
            {
                temp_data[i] = this->data[i];
            }

            // delete old
            delete[] this->data;
            this->data = temp_data;
            printDebug("DynamicArray resized");
        }
    }

    /**
     * Validate current position throw exception if invalid
     */
    void validatePosition(size_t position)
    {
        if (position >= this->length)
        {
            throw("DynamicArray: Position out of range");
        }
    }

    TElement* data;
    size_t length;
    size_t capacity;
};

#endif // DYNAMIC_ARRAY_H_
