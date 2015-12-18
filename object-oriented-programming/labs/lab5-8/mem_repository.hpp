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

    virtual ~MemRepository();

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

    /**
     * Filter the repository by quantity greater than
     * unsigned int quantity
     */
    Ingredient* getById(const unsigned int);

    /**
     * return true if last opertion was undone, false if nothing to undo
     */
    bool undo();

    /**
     * return true if id exists, false otherwise
     */
    bool exists(unsigned int);

    void printToStdOut() const;

    void addToUndo();

protected:
    int getIndexByID(unsigned int);

    void copyListToArray(ArrayDoubleList<Ingredient*>*, DynamicArray<Ingredient*>*);
    void copyArrayToList(DynamicArray<Ingredient*>*, ArrayDoubleList<Ingredient*>*);

    void deleteArray();
    void deleteInternalArray(DynamicArray<Ingredient*>*);
    void deleteList();
    void deleteInternalList(ArrayDoubleList<Ingredient*>*);
    void deleteUndo();

    DynamicArray<Ingredient*>* undo_array[128];
    unsigned int undo_array_length;
    unsigned int undo_array_max;
};

#endif // MEM_REPOSITORY_H_
