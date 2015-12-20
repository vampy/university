#include <cassert>

#include "vector.hpp"
#include "ingredient.hpp"
#include "mem_repository.hpp"
#include "tests.hpp"

/*
* Tests dynamic vector
*/
void testVector()
{
    int ints[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    DynamicArray<int> v;

    size_t nrInts = sizeof(ints) / sizeof(int);

    size_t i;
    for (i = 0; i < nrInts; i++)
    {
        v.add(ints[i]);
    }
    assert(v.getLength() == nrInts);

    for (i = 0; i < v.getLength(); i++)
    {
        int element = v.get(i);
        assert(ints[i] == element);
    }
}

/*
* Test Repository functions
*/
void testRepository()
{
    MemRepository repo;

    repo.addIngredient(new Ingredient(1, 20, "Faina", "Baneasa"));
    repo.addIngredient(new Ingredient(2, 500, "Faina", "Baneasa"));

    assert(repo.exists(0) == false);
    assert(repo.exists(1) == true);
    assert(repo.exists(2) == true);
    assert(repo.getById(1)->getQuantity() == 20);
    assert(repo.getById(2)->getQuantity() == 500);
}

/*
* Test ingredient functions
*/
void testIngredient()
{
    Ingredient ing(1, 20, "Faina", "Baneasa");
    assert(ing.getId() == 1);
    assert(ing.getName() == "Faina");
    assert(ing.getProducer() == "Baneasa");
    assert(ing.getQuantity() == 20);

    ing.setName("Knor");
    ing.setProducer("Margaritar");
    ing.setQuantity(200);

    assert(ing.getName() == "Knor");
    assert(ing.getProducer() == "Margaritar");
    assert(ing.getQuantity() == 200);
}

void runTests()
{
    testVector();
    testRepository();
    testIngredient();
}
