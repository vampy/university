#ifndef REPOSITORY_FILTERS_H_
#define REPOSITORY_FILTERS_H_
#include <string>
#include "ingredient.hpp"

using namespace std;

class RepositoryFilter
{
public:
    /**
     * filter out product
     * true if the filter match the product
     * false otherwise
     */
    virtual bool isValid(const Ingredient*) const = 0;

    virtual ~RepositoryFilter() {}
};

class FilterNameEqual : public RepositoryFilter
{
protected:
    string name;

public:
    FilterNameEqual(string n) : name(n) {}
    bool isValid(const Ingredient* ingredient) const { return ingredient->getName() == this->name; }
};

class FilterQuantityGreater : public RepositoryFilter
{
protected:
    unsigned int quantity;

public:
    FilterQuantityGreater(unsigned int q) : quantity(q) {}
    bool isValid(const Ingredient* ingredient) const { return ingredient->getQuantity() > this->quantity; }
};

class FilterQuantityEqual : public RepositoryFilter
{
protected:
    unsigned int quantity;

public:
    FilterQuantityEqual(unsigned int q) : quantity(q) {}
    bool isValid(const Ingredient* ingredient) const { return ingredient->getQuantity() == this->quantity; }
};

class FilterQuantityLess : public RepositoryFilter
{
protected:
    unsigned int quantity;

public:
    FilterQuantityLess(unsigned int q) : quantity(q) {}
    bool isValid(const Ingredient* ingredient) const { return ingredient->getQuantity() < this->quantity; }
};

typedef bool (*repositorySortFunc)(const Ingredient*, const Ingredient*);

#endif // REPOSITORY_FILTERS_H_
