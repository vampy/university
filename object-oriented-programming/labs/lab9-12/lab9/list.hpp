#ifndef DOUBLE_LINKED_LIST_H_
#define DOUBLE_LINKED_LIST_H_
#include <stdexcept>
#include <string>
#include <cstdlib>

#include "util.hpp"

// forward declare
template <typename TElement>
class ArrayDoubleListIterator;

template <typename TElement>
class ArrayDoubleListNode;

template <typename TElement>
class ArrayDoubleList;


template <typename TElement>
class ArrayDoubleListNode
{
public:
    ArrayDoubleListNode()
    {
        this->next = -1;
        this->previous = -1;
    }

    std::string toString()
    {
        return to_string(this->previous) + " " + to_string(this->next);
    }

    int previous;
    int next;
    TElement info;
};

template <typename TElement>
class ArrayDoubleListIterator
{
public:
    ArrayDoubleListIterator(ArrayDoubleList<TElement> *list, int current = 0)
    {
        this->current = current;
        this->list = list;
    }

    std::string toString()
    {
        return "ArrayDoubleListIterator -> current=" + to_string(this->current);
    }

    bool hasNext()
    {
        return this->current + 1 <= this->list->getLength();
    }

    void resetToHead()
    {
        //printDebug("resetToHead");
        this->current = 0;
    }

    TElement next()
    {
        int curIndex = this->current;
        // increase internal pointer
        this->current++;
        //printDebug(this->toString());

        // get current element
        //printDebug("ArrayDoubleListIterator -> curIndex = " + to_string(curIndex));
        return this->list->get(curIndex);
    }

protected:
    int current;
    ArrayDoubleList<TElement> *list;
};

template <typename TElement>
class ArrayDoubleList
{
public:
    ArrayDoubleList(int initial_capacity = 32)
    {
        if(initial_capacity <= 0) // reset to default if initial capacity is small
        {
            initial_capacity = 256;
        }
        this->capacity = initial_capacity;
        this->length = 0;

        // alocate memory
        this->data = new ArrayDoubleListNode<TElement>*[this->capacity];
        for(int i = 0; i < this->capacity; i++)
        {
            this->data[i] = new ArrayDoubleListNode<TElement>;
        }

        this->iterator = new ArrayDoubleListIterator<TElement>(this);
        printDebug("ArrayDoubleList constructed");
    }

    ~ArrayDoubleList()
    {
        // clean nodes
        for(int i = 0; i < this->capacity; i++)
        {
            if(this->data[i] == NULL)
            {
                printDebug("ArrayDoubleList: DELETE POINTER FOUND");
                continue;
            }
            //printDebug(this->data[i]->toString());
            delete this->data[i];
        }

        delete [] this->data;
        delete this->iterator;
        printDebug("ArrayDoubleList destroyed");
    }

    /**
    * Adds an element at the end of ArrayDoubleList
    * TElement - element to add
    */
    void add(TElement element)
    {
        this->resize();

        auto addNode = this->data[this->length];
        
        addNode->previous = this->length;
        if(this->length == 0)
        {
            addNode->previous = -1;
        }
        addNode->next = this->length + 1;
        addNode->info = element;
        
        this->length++;
    }

    /**
    * Returns element from vector from given position
    * int position - the position
    * returns TElement from position
    */
    TElement get(int position)
    {
        //printDebug("get -> " + to_string(position));
        this->validatePosition(position);
        return this->data[position]->info;
    }

    /**
    * Remove element from position
    * Throw exception if position is invalid
    */
    void remove(int position)
    {
        // TODO fix prev and next
        this->validatePosition(position);

        delete this->data[position];
        for(int i = position; i < this->length - 1; i++)
        {
            // retain for copying
//            int prev = this->data[i]->previous;
//            int next = this->data[i]->next;

            this->data[i] = this->data[i + 1];

            // copy to current node
//            this->data[i]->previous = prev;
//            this->data[i]->next = next;
        }
        this->data[this->length - 1] = new ArrayDoubleListNode<TElement>;

        // remove last node
        //printDebug("ArrayDoubleList: " + to_string(this->length - 1));

        this->length--;
    }

    /**
    * returns a list iterator
    */
    ArrayDoubleListIterator<TElement> *getIterator() const
    {
        this->iterator->resetToHead();
        return this->iterator;
    }

    /**
    * returns the number the elements
    */
    unsigned int getLength() const
    {

        return this->length;
    }

    void sort(bool (*sortCompareFunction)(const TElement, const TElement))
    {
        throw ("NOT IMPLEMENTED");
    }

protected:
    /**
    * Resize the internal data if necessary
    */
    void resize()
    {
        if(this->length >= this->capacity) // resize condition
        {
            // create new buffer
            this->capacity *= 2;
            ArrayDoubleListNode<TElement> **temp_data = new ArrayDoubleListNode<TElement>*[this->capacity];

            for(int i = 0; i < this->length; i++)
            {
                temp_data[i] = this->data[i];
            }

            // delete old
            delete[] this->data;
            this->data = temp_data;
            printDebug("ArrayDoubleList resized");
        }
    }

    /**
    * Validate current position throw exception if invalid
    */
    void validatePosition(long position)
    {
        if(position < 0 || position >= this->length)
        {
            //'printDebug("validatePosition -> position = " + to_string(position));
            throw ("ArrayDoubleList: Position out of range");
        }
    }

    ArrayDoubleListNode<TElement> first;
    ArrayDoubleListNode<TElement> last;

    ArrayDoubleListNode<TElement> **data;
    ArrayDoubleListIterator<TElement> *iterator;
    unsigned int length;
    unsigned int capacity;
};

#endif // DOUBLE_LINKED_LIST_H_
