#ifndef MAP_ADT_H_
#define MAP_ADT_H_

template <typename TKey, typename TValue>
class MapADT
{
public:
    virtual TValue get(TKey key) = 0;

    virtual void put(TKey key, TValue value) = 0;

    virtual void remove(TKey key) = 0;

    virtual bool containsKey(TKey key) = 0;

    virtual bool isEmpty() const = 0;

    virtual unsigned int getLength() const = 0;

    virtual     void clear() = 0;
};

#endif // MAP_ADT_H_
