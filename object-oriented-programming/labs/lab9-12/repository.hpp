#ifndef REPOSITORY_H_
#define REPOSITORY_H_
#include <vector>
#include "ingredient.hpp"
#include "repository_filters.hpp"

class Repository
{
protected:
    virtual int getIndexByID(const unsigned int) const = 0;

    vector<Ingredient*>* array;

public:
    virtual ~Repository() {}

    virtual void addIngredient(Ingredient*) = 0;

    virtual void removeIngredient(unsigned int) = 0;

    virtual unsigned int getLength() const = 0;

    virtual void filterBy(const RepositoryFilter&) = 0;

    virtual void sortBy(repositorySortFunc) = 0;

    virtual bool exists(unsigned int) = 0;

    virtual Ingredient* getById(const unsigned int) const = 0;

    virtual Ingredient* getByIndex(const unsigned int) const = 0;

    virtual bool undo() = 0;

    virtual void toString() const = 0;

    virtual void clear() = 0;

    virtual void addToUndo() = 0;
};

class RepositoryException : public exception
{
public:
    RepositoryException(string m = "exception!") : msg(m) {}
    ~RepositoryException() throw() {}
    const char* what() const throw() { return msg.c_str(); }

protected:
    string msg;
};

#endif // REPOSITORY_H_
