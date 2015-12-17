#ifndef REPOSITORY_H_
#define REPOSITORY_H_
#include "vector.hpp"
#include "list.hpp"
#include "ingredient.hpp"
#include "repository_filters.hpp"

class Repository
{
protected:
    virtual int getIndexByID(unsigned int) = 0;

    DynamicArray<Ingredient*> *array;
    ArrayDoubleList<Ingredient*> *list;
    
public:
    virtual void addIngredient(Ingredient*) = 0;

    
    virtual void removeIngredient(unsigned int) = 0;

    
    virtual void filterBy(const RepositoryFilter&) = 0;

    
    virtual void sortBy(repositorySortFunc) = 0;

    
    virtual bool exists(unsigned int) = 0;

    
    virtual Ingredient* getById(const unsigned int) = 0;

    virtual bool undo() = 0;

    virtual void printToStdOut() const = 0;
};

class RepositoryException : public exception
{
public:
    RepositoryException(string m="exception!") : msg(m) {}
    ~RepositoryException() throw() {}
    const char* what() const throw() { return msg.c_str(); }

protected:
    string msg;
};

#endif // REPOSITORY_H_
