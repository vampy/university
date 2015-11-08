#ifndef MAP_HASH_H_
#define MAP_HASH_H_

#include <vector>
#include <string>

#include "util.hpp"
#include "map_adt.hpp"

using namespace std;

namespace MapHash
{
template <typename TValue>
class Map;

/*
 * Define an entry for the map hash
 * which has key and value
*/
template <typename TValue>
class Entry
{
public:
    Entry(){}
    Entry(string key, TValue value)
    {
        this->key = key;
        this->value = value;
    }

    string key;
    TValue value;
};

template <typename TValue>
class Iterator
{
protected:
    Map<TValue> *map;
    unsigned int currentBucket; // hold the index of the bucket
    unsigned int currentPointerInBucket; // hold the index the item in the bucket
    unsigned int nrEntries; // hold the total count

public:
    /*
     * Iterrator constructor
     * @param map the map
    */
    Iterator(Map<TValue> *map)
    {
        this->map = map;
        currentBucket = 0;
        currentPointerInBucket = 0;
        nrEntries = 0;
    }

    /*
     * Check if we have more elements in the map
     * @return bool
    */
    bool hasNext()
    {
        return nrEntries < map->nrEntries;
    }

    /*
     * Move the interal pointer futher into the map, return the current entry
     * @return Entry<TValue>*
    */
    const Entry<TValue>* next()
    {
        Entry<TValue> *entry;

        // we are already in a bucket, check if next element if to be found
        if(currentPointerInBucket < map->buckets->at(currentBucket)->size())
        {
            // get next entry
            entry =  map->buckets->at(currentBucket)->at(currentPointerInBucket);
        }
        else
        {
            // move to the first non empty bucket
            do
            {
                currentBucket++;
            } while(map->buckets->at(currentBucket)->size() == 0);

            // get first element of the bucket
            currentPointerInBucket = 0;
            entry = map->buckets->at(currentBucket)->at(currentPointerInBucket);
        }

        // update counters, move forward in bucket, and in nrEntries
        currentPointerInBucket++;
        nrEntries++;

        return entry;
    }
};

template <typename TValue>
class Map
{
friend class MapHash::Iterator<TValue>;
public:
    /*
     * Initial buckets size
    */
    const unsigned int INTIAL_SIZE = 128;

    /*
     * The map constructor
    */
    Map()
    {
        this->initBuckets();
    }

    ~Map()
    {
        this->deleteBuckets();
    }

    /*
     * Get a value by the key string
     * @param string key
     * @return TValue the value associated with that key
    */
    TValue get(string key)
    {
        unsigned int hash_index = this->hashIndex(key);
        long bucket_index = this->findInBucket(buckets->at(hash_index), key);
        if(bucket_index == -1)
        {
            printDebug("Key does not exist");
            throw "Key does not exist";
        }

        return buckets->at(hash_index)->at(bucket_index)->value;
    }

    /*
     * Create a new key with the value, or replace the current key with the new value
     * if that key already exists
     * @param string key
     * @param TValue value
    */
    void put(string key, TValue value)
    {
        this->rehash();

        unsigned int hash_index = this->hashIndex(key);
        long bucket_index = this->findInBucket(buckets->at(hash_index), key);
        if(bucket_index == -1) // create, the key was not found in the bucket
        {
            //printDebug("HashMap: put() create value");
            nrEntries++;
            buckets->at(hash_index)->push_back(new Entry<TValue>(key, value));

            return;
        }

        // update, key was found
        //printDebug("HashMap: put() update value");
        buckets->at(hash_index)->at(bucket_index)->value = value;
    }

    /*
     * Remove a map entry based on the key. If the key does not exist, do nothing.
     * @param string key
    */
    void remove(string key)
    {
        this->rehash();

        unsigned int hash_index = this->hashIndex(key);
        auto bucket = buckets->at(hash_index);
        long bucket_index = this->findInBucket(bucket, key);
        if(bucket_index != -1) // found
        {
            printDebug("HashMap: remove()");
            nrEntries--;
            delete bucket->at(bucket_index);
            bucket->erase(bucket->begin() + bucket_index);
        }
    }

    /*
     * Checks if the key is in the map
     * @param string key
     * @return bool
    */
    bool containsKey(string key)
    {
        return this->findInBucket(buckets->at(this->hashIndex(key)), key) != -1;
    }

    /*
     * Get the map itertor. You must eliberate the iterator yourself from memory with delete.
     * @return MapHash::Iterator<TValue>*
    */
    MapHash::Iterator<TValue>* getIterator()
    {
        return new MapHash::Iterator<TValue>(this);
    }

    /*
     * Checks if the map is empty
     * @return bool
    */
    bool isEmpty() const
    {
        return nrEntries == 0;
    }

    /*
     * Get the number of entries in the map
     * @return unsigned int
    */
    unsigned int getLength() const
    {
        return nrEntries;
    }

    /*
     * Clear all the entries in the map
    */
    void clear()
    {
        this->deleteBuckets();
        this->initBuckets();
    }

protected:
    /*
     * Compute the hash of a string
     * @param string s
     * @return int
    */
    int hash(string s)
    {
        const long MULTIPLIER = -1664117991L;
        unsigned long hashcode = 0;
        auto length = s.length();
        for (int i = 0; i < length; i++)
        {
            hashcode = hashcode * MULTIPLIER + s[i];
        }

        return hashcode & ((unsigned) -1 >> 1);
    }

    /*
     * Compute the bucket index based on the key, using the hash function
     * @param string key
     * @return unsigned int
    */
    unsigned int hashIndex(string key)
    {
        return this->hash(key) % buckets->size();
    }

    /*
     * Rehash our map if the loadfactor is appropriate
    */
    void rehash()
    {
        float loadFactor = (float)nrEntries / buckets->size();

        if(loadFactor > 0.8) // increase buckets
        {
            unsigned int old_size = buckets->size();

            // create new buckets
            auto new_buckets = new vector<vector<Entry<TValue>*>*>;
            unsigned int new_size = old_size * 2, i, j;
            for(i = 0; i < new_size; i++)
            {
                new_buckets->push_back(new vector<Entry<TValue>*>);
            }

            printDebug("HashMap: rehash UP -> old_size = " + to_string(old_size) + ", new_size = " + to_string(new_size));
            // rehash for new buckets
            for(i = 0; i < old_size; i++)
            {
                unsigned int old_bucket_size = buckets->at(i)->size();
                for(j = 0; j < old_bucket_size; j++)
                {
                    Entry<TValue> *entry = buckets->at(i)->at(j);
                    int new_hash_index = this->hash(entry->key) % new_buckets->size();

                    new_buckets->at(new_hash_index)->push_back(entry);
                }
                delete buckets->at(i); // delete indiviudal bucket
            }
            delete buckets; // delete bucket vector

            this->buckets = new_buckets;
        }

        if(loadFactor < 0.1 && buckets->size() != INTIAL_SIZE) // do not rehash down our initial size, decrease buckets
        {
            printDebug("HashMap: rehash DOWN");
        }
    }

    /*
     * Find the index of a key in a bucket
     * @param vector<Entry<TValue>*>* bucket where to find our key
     * @param string key
     * @return long the index in the bucket or -1 otherwise
    */
    long findInBucket(vector<Entry<TValue>*> *bucket, string key)
    {
        unsigned int size = bucket->size(), i;
        for(i = 0; i < size; i++)
        {
            if(bucket->at(i)->key == key)
            {
                return i;
            }
        }

        return -1;
    }

    /*
     * Init the internal data structure
    */
    void initBuckets()
    {
        buckets = new vector<vector<Entry<TValue>*>*>;
        for(unsigned int i = 0; i < INTIAL_SIZE; i++)
        {
            buckets->push_back(new vector<Entry<TValue>*>);
        }

        nrEntries = 0;
    }

    /*
     * Delete interal data structure aka all the entries
    */
    void deleteBuckets()
    {
        unsigned int buckets_size, bucket_size, i, j;

        buckets_size = buckets->size();
        for(i = 0; i < buckets_size; i++)
        {
            bucket_size = buckets->at(i)->size();
            for(j = 0; j < bucket_size; j++)
            {
                delete buckets->at(i)->at(j); // delete map entry
            }
            delete buckets->at(i); // delete indiviudal bucket
        }
        delete buckets; // delete bucket vector
    }

    vector<vector<Entry<TValue>*>*> *buckets;
    unsigned int nrEntries;
};
}

#endif // MAP_HASH_H_
