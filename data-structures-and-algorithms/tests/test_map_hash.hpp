#ifndef TEST_MAP_HASH_H_
#define TEST_MAP_HASH_H_
#include <cassert>
#include <string>
#include "../map_hash.hpp"

using namespace std;

static void testMapHashInit()
{
    auto test = new MapHash::Map<int>;

    assert(test->isEmpty() == true);
    assert(test->containsKey("A") == false);
    assert(test->getLength() == 0);

    delete test;
}

static void testMapHashCRUD()
{
    auto test = new MapHash::Map<int>;

    // put/get
    assert(test->isEmpty() == true);
    test->put("A", 1);
    assert(test->isEmpty() == false);
    test->put("B", 2);
    test->put("C", 3);
    assert(test->get("A") == 1);
    assert(test->get("B") == 2);
    assert(test->get("C") == 3);

    assert(test->containsKey("Z") == false);
    assert(test->containsKey("a") == false);
    assert(test->containsKey("b") == false);
    assert(test->containsKey("c") == false);

    // remove
    assert(test->containsKey("A") == true);
    test->remove("A");
    assert(test->containsKey("A") == false);
    assert(test->containsKey("B") == true);
    test->remove("B");
    assert(test->containsKey("B") == false);
    assert(test->containsKey("C") == true);
    test->remove("C");
    assert(test->containsKey("C") == false);
    assert(test->isEmpty() == true);

    delete test;
}

void testMapHashIterator()
{
    unsigned int length = 0, test_length = 1200, i;
    auto test = new MapHash::Map<int>;

    for (i = 0; i < test_length; i++)
    {
        length++;
        test->put(to_string(i), i);
        assert(length == test->getLength());
    }

    auto it = test->getIterator();
    length = 0;
    while (it->hasNext())
    {
        auto element = it->next();
        assert(element->key == to_string(element->value));
        length++;
    }
    assert(length == test->getLength());
    delete it;

    delete test;
}

void testMapHash()
{
    testMapHashInit();
    testMapHashCRUD();
    testMapHashIterator();
}

#endif // TEST_MAP_HASH_H_
