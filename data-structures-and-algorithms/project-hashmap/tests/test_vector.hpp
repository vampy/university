#ifndef TEST_VECTOR_H_
#define TEST_VECTOR_H_
#include <cassert>
#include "../vector.hpp"

static void testVectorInit()
{
    unsigned int capacity = 1024;
    DynamicArray<int>* test = new DynamicArray<int>(capacity);

    assert(test->getLength() == 0);
    assert(test->getCapacity() == capacity);

    delete test;
}

static void testVectorCRUD()
{
    DynamicArray<unsigned int>* test = new DynamicArray<unsigned int>;

    unsigned int length = 0, test_length = 1200, i;

    // add
    for (i = test_length; i > 0; i--)
    {
        length++;
        test->add(i);
        assert(test->getLength() == length);
    }

    // get/set
    for (i = 0; i < test_length; i++)
    {
        assert(test->get(i) == test_length - i);
        test->set(i, i);
        assert(test->get(i) == i);
    }

    // insert, replace at position
    for (i = 0; i < test_length; i += 50)
    {
        // length++;
        test->insert(i, i * i);
        assert(test->getLength() == length);
        assert(test->get(i) == i * i);
    }

    assert(test_length == length);
    // remove
    for (i = 0; i < test_length; i++)
    {
        length--;
        test->remove(test_length - i - 1);
        assert(test->getLength() == length);
    }

    delete test;
}

void testVector()
{
    testVectorInit();
    testVectorCRUD();
}

#endif // TEST_VECTOR_H_
