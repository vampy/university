#ifndef MAP_VECTOR_H_
#define MAP_VECTOR_H_

#include "vector.hpp"

namespace MapVector
{
template <typename TKey, typename TValue>
class Map;

template <typename TKey, typename TValue>
class Entry
{
public:
    Entry() {}
    Entry(TKey key, TValue value)
    {
        this->key = key;
        this->value = value;
    }

    TKey key;
    TValue value;
};

template <typename TKey, typename TValue>
class Iterator
{
protected:
    Map<TKey, TValue>* map;
    unsigned int currentPointer;

public:
    /**
     * Iterrator constructor
     * @param map the map
     */
    Iterator(Map<TKey, TValue>* map)
    {
        this->map = map;
        currentPointer = 0;
    }

    /**
     * Check if we have more elements in the map
     * @return bool
     */
    bool hasNext() { return currentPointer < map->getLength(); }

    /**
     * Move the interal pointer futher into the map, return the current entry
     * @return Entry<TValue>*
     */
    const Entry<TKey, TValue>* next()
    {
        Entry<TKey, TValue>* entry = map->mapElements->get(currentPointer);
        currentPointer++;

        return entry;
    }
};

template <typename TKey, typename TValue>
class Map
{
    friend class MapVector::Iterator<TKey, TValue>;

public:
    /**
     * The map constructor
     */
    Map() { this->initInternalData(); }

    ~Map() { this->deleteInternalData(); }

    /**
     * Get a value by the key string
     * @param string key
     * @return TValue the value associated with that key
     */
    TValue get(TKey key)
    {
        long index = this->findKey(key);
        if (index == -1)
        {
            printDebug("Key is not in the map");
            throw "Key is not in the map";
        }

        return mapElements->get(index)->value;
    }

    /**
     * Create a new key with the value, or replace the current key with the new value
     * if that key already exists
     * @param string key
     * @param TValue value
     */
    void put(TKey key, TValue value)
    {
        long index = this->findKey(key);
        if (index == -1) // key does not exist
        {
            mapElements->add(new Entry<TKey, TValue>(key, value));

            return;
        }

        // key exists
        mapElements->get(index)->value = value;
    }

    /**
     * Remove a map entry based on the key. If the key does not exist, do nothing.
     * @param string key
     */
    void remove(TKey key)
    {
        long index = this->findKey(key);
        if (index != -1) // found
        {
            delete mapElements->get(index);
            mapElements->remove(index);
        }
    }

    /**
     * Checks if the key is in the map
     * @param string key
     * @return bool
     */
    bool containsKey(TKey key) { return (this->findKey(key) != -1); }

    /**
     * Get the map itertor. You must eliberate the iterator yourself from memory with delete.
     * @return MapHash::Iterator<TValue>*
     */
    MapVector::Iterator<TKey, TValue>* getIterator() { return new MapVector::Iterator<TKey, TValue>(this); }

    /**
     * Checks if the map is empty
     * @return bool
     */
    bool isEmpty() const { return (mapElements->getLength() == 0); }

    /**
     * Get the number of entries in the map
     * @return unsigned int
     */
    unsigned int getLength() const { return mapElements->getLength(); }

    /**
     * Clear all the entries in the map
     */
    void clear()
    {
        this->deleteInternalData();
        this->initInternalData();
    }

protected:
    DynamicArray<Entry<TKey, TValue>*>* mapElements;

    /**
     * Find the index of a key in the vector elements
     * @param string key
     * @return long the index in the bucket or -1 otherwise
     */
    long findKey(TKey key)
    {
        for (unsigned int i = 0; i < mapElements->getLength(); i++)
        {
            if (mapElements->get(i)->key == key)
            {
                return i;
            }
        }

        return -1;
    }

    void initInternalData() { mapElements = new DynamicArray<Entry<TKey, TValue>*>; }

    void deleteInternalData()
    {
        for (unsigned int i = 0; i < mapElements->getLength(); i++)
        {
            delete mapElements->get(i);
        }
        delete mapElements;
        mapElements = NULL;
    }
};
}

#endif // MAP_VECTOR_H_
