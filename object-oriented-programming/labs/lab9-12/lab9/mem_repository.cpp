#include <algorithm>
#include <cassert>
#include <string>
#include <cstring>
#include <cstdlib>
#include <vector>

#include "mem_repository.hpp"
#include "ingredient.hpp"
#include "util.hpp"

MemRepository::MemRepository()
{
    this->array = new vector<Ingredient*>;
    this->undoVector = new vector<vector<Ingredient*>*>;

    printDebug("Repository constructed");
}

MemRepository::~MemRepository()
{
    // free data
    this->deleteArray();

    // free undo
    this->deleteUndo();

    printDebug("Repository Destroyed");
}

void MemRepository::deleteArray()
{
    this->deleteInternalArray(array);
    delete array; array = NULL;
}

void MemRepository::deleteInternalArray(vector<Ingredient*> *toDelete)
{
    for(unsigned int i = 0; i < toDelete->size(); i++)
    {
        Ingredient *to_delete = toDelete->at(i);
        if(to_delete != NULL)
        {
            delete to_delete; to_delete = NULL;
        }
        else
        {
            printDebug("deleteInternalArray -> NULL POINTER FOUND");
        }
    }
}

void MemRepository::clear()
{
    this->deleteArray();
    this->array = new vector<Ingredient*>;
}

void MemRepository::deleteUndo()
{
    // free undo
    for(unsigned int i = 0; i < undoVector->size(); i++)
    {
        this->deleteInternalArray(this->undoVector->at(i));

        delete undoVector->at(i);
    }

    delete undoVector; undoVector = NULL;
}

int MemRepository::getIndexByID(const unsigned int id) const
{
    for(unsigned int i = 0; i < array->size(); i++)
    {
        if(array->at(i)->getId() == id)
        {
            return i;
        }
    }

    return -1;
}

Ingredient* MemRepository::getById(const unsigned int id) const
{
    return array->at(this->getIndexByID(id));
}

Ingredient* MemRepository::getByIndex(const unsigned int index) const
{
    return array->at(index);
}

bool MemRepository::exists(unsigned int id)
{
    if(this->getIndexByID(id) == -1)
    {
        return false;
    }

    return true;
}

void MemRepository::addIngredient(Ingredient *ingredient)
{
    this->addToUndo();
    array->push_back(ingredient);
}

unsigned int MemRepository::getLength() const
{
    return array->size();
}

void MemRepository::removeIngredient(unsigned int id)
{
    this->addToUndo();
    int index = this->getIndexByID(id);
    if(index == -1)
    {
        printDebug("Id does not exist");
        throw "Id does not exist";
    }

    // remove from memory and array
    delete array->at(index);
    array->erase(array->begin() + index);
}

void MemRepository::filterBy(const RepositoryFilter& filter)
{
    this->addToUndo();
    auto new_vector = new vector<Ingredient*>;

    for(unsigned int i = 0; i < array->size(); i++)
    {
        Ingredient *ingredient = array->at(i);
        if(filter.isValid(ingredient))
        {
            new_vector->push_back(ingredient);
        }
        else
        {
            delete ingredient;
        }
    }
    delete this->array;
    this->array = new_vector;
}

void MemRepository::sortBy(repositorySortFunc func)
{
    this->addToUndo();

    sort(array->begin(), array->end(), func);
}

void MemRepository::addToUndo()
{
    auto undo_vector = new vector<Ingredient*>;

    for(unsigned int i = 0; i < array->size(); i++)
    {
        Ingredient *old_ingredient = array->at(i);
        undo_vector->push_back(new Ingredient(old_ingredient->getId(), old_ingredient->getQuantity(),
                                              old_ingredient->getName(), old_ingredient->getProducer()));
    }

    this->undoVector->push_back(undo_vector);
}

bool MemRepository::undo()
{
    if(undoVector->empty())
    {
        return false;
    }

    this->deleteArray();
    array = undoVector->back();
    undoVector->pop_back();

    return true;
}


void MemRepository::toString() const
{
    cout << setw(4) << "ID" << setw(15) << "Quantity"
         << setw(30) << "Name" << setw(30) << "Producer" << endl << endl;

    for(int i = 0; i < array->size(); i++)
    {
        Ingredient *it = array->at(i);
        cout << setw(4) << it->getId() << setw(15) << it->getQuantity()
             << setw(30) << it->getName() << setw(30) << it->getProducer() << endl << endl;
    }
}

