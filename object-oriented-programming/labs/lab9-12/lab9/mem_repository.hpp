#ifndef MEM_REPOSITORY_H_
#define MEM_REPOSITORY_H_
#include "vector.hpp"
#include "list.hpp"
#include "ingredient.hpp"
#include "repository.hpp"

class MemRepositoryException : public RepositoryException
{

};

class MemRepository : public Repository
{
public:
    MemRepository();

    ~MemRepository();

    /**
    * Add an ingredient
    * Ingredient *ing - The ingredient to add
    */
    virtual void addIngredient(Ingredient*);

    /**
    * Remove an ingredient by id
    * unsigned int id - the ingredient with id to remove
    */
    virtual void removeIngredient(unsigned int);

    virtual unsigned int getLength() const;

    /**
    * Filter the repository by a custom filter
    * string name
    */
    virtual void filterBy(const RepositoryFilter&);

    /**
    * Sort the repository by function
    * string name
    */
    virtual void sortBy(repositorySortFunc);

    Ingredient* getById(const unsigned int) const;

    virtual Ingredient* getByIndex(const unsigned int) const;

    /**
    * return true if last opertion was undone, false if nothing to undo
    */
    bool undo();

    /**
    * return true if id exists, false otherwise
    */
    bool exists(unsigned int);

    void toString() const;

    virtual void clear();

    void addToUndo();

protected:
    int getIndexByID(const unsigned int) const;

    void deleteArray();
    void deleteInternalArray(vector<Ingredient*> *toDelete);
    void deleteUndo();

    vector<vector<Ingredient*>*> *undoVector;
};

#endif // MEM_REPOSITORY_H_
