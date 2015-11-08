#ifndef TEST_MAP_VECTOR_H_
#define TEST_MAP_VECTOR_H_
#include <cassert>
#include <string>
#include "../map_vector.hpp"

using namespace std;

static void testMapVectorInit()
{
    auto test = new MapVector::Map<string, int>;

    assert(test->isEmpty() == true);
    assert(test->containsKey("A") == false);

    assert(test->getLength() == 0);

    delete test;
}

static void testMapVectorCRUD()
{
    auto test = new MapVector::Map<string, int>;

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

void testMapVector()
{
    testMapVectorInit();
    testMapVectorCRUD();
}

#endif // TEST_MAP_VECTOR_H_
