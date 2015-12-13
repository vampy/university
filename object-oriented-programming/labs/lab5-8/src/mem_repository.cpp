#include <algorithm>
#include <cassert>
#include <string>
#include <cstring>
#include <cstdlib>

#include "mem_repository.hpp"
#include "vector.hpp"
#include "ingredient.hpp"
#include "util.hpp"

MemRepository::MemRepository()
{
    this->array = new DynamicArray<Ingredient*>;
    this->list = new ArrayDoubleList<Ingredient*>;
    this->undo_array_length = 0;
    this->undo_array_max = 0;

    printDebug("Repository constructed");
}

MemRepository::~MemRepository()
{
    // free data
    this->deleteArray();
    this->deleteList();

    // free undo
    this->deleteUndo();

    printDebug("Repository Destroyed");
}

void MemRepository::deleteArray()
{
    this->deleteInternalArray(this->array);
    delete this->array;
}

void MemRepository::deleteInternalArray(DynamicArray<Ingredient*> *toDelete)
{
    for(int i = 0; i < toDelete->getLength(); i++)
    {
        Ingredient *to_delete = toDelete->get(i);
        if(to_delete != NULL)
        {
            delete to_delete;
            to_delete = NULL;
        }
        else
        {
            printDebug("deleteInternalArray -> NULL POINTER FOUND");
        }
    }
}

void MemRepository::deleteInternalList(ArrayDoubleList<Ingredient*> *toDelete)
{
    for(int i = 0; i < toDelete->getLength(); i++)
    {
        Ingredient *to_delete = toDelete->get(i);
        if(to_delete != NULL)
        {
            delete to_delete;
            to_delete = NULL;
        }
        else
        {
            printDebug("deleteInternalList -> NULL POINTER FOUND");
        }
    }
}

void MemRepository::deleteList()
{
    this->deleteInternalList(this->list);
    delete this->list;
}

void MemRepository::deleteUndo()
{
    // free undo
//    cout << "undo_length: " << this->undo_array_length << endl;
    for (int i = 0; i < this->undo_array_length; i++)
    {
        // delete in array
        this->deleteInternalArray(this->undo_array[i]);

        if(this->undo_array[i] != NULL)
        {
            delete this->undo_array[i];
            this->undo_array[i] = NULL;
        }
        else
        {
            printDebug("RARE CASE OF NULL POINTER FOUND UNDO ARRAY-----------");
        }

    }
}

int MemRepository::getIndexByID(unsigned int id)
{
//    for(int i = 0; i < this->array->getLength(); i++)
//    {
//        if(this->array->get(i)->getId() == id) // index matches id
//        {
//            return i;
//        }
//    }

    auto iterator = this->list->getIterator();
    unsigned int i = 0;
    //printDebug("getIndexByID -> " + iterator->toString());
    while(iterator->hasNext())
    {
        //printDebug("getIndexByID -> i = " + to_string(i));
        if(iterator->next()->getId() == id)
        {
            return i;
        }
        i++;
    }

    return -1;
}

Ingredient* MemRepository::getById(const unsigned int id)
{
    return this->list->get(this->getIndexByID(id));
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
    this->list->add(ingredient);
}

void MemRepository::removeIngredient(unsigned int id)
{
    this->addToUndo();
    int index = this->getIndexByID(id);
    if(index == -1)
    {
        throw "Id does not exist";
    }
    // remove from memory and array
    delete this->list->get(index);
    this->list->remove(index);

//    delete this->array->get(index);
//    this->array->remove(index);
}

void MemRepository::filterBy(const RepositoryFilter& filter)
{
    this->addToUndo();
    auto new_list = new ArrayDoubleList<Ingredient*>;
    auto iterator = this->list->getIterator();

    while(iterator->hasNext())
    {
        Ingredient *ingredient = iterator->next();
        if(filter.isValid(ingredient))
        {
            new_list->add(ingredient);
        }
        else
        {
            delete ingredient;
        }
    }

    delete this->list;
    this->list = new_list;

//    DynamicArray<Ingredient*> *new_array = new DynamicArray<Ingredient*>;
//    for(int i = 0; i < this->array->getLength(); i++)
//    {
//        Ingredient *ingredient = this->array->get(i);
//        if(filter.isValid(ingredient))
//        {
//            new_array->add(ingredient);
//        }
//        else
//        {
//            delete ingredient; // remove
//        }
//    }

//    delete this->array;
//    this->array = new_array;
}

void MemRepository::sortBy(repositorySortFunc func)
{
    this->addToUndo();
    auto temp_array = new DynamicArray<Ingredient*>;
    this->copyListToArray(this->list, temp_array);

    Ingredient **data = temp_array->getInternalData();
    std::sort(data, data + temp_array->getLength(), func);

    
    
    
    
    
    this->deleteList();
    this->list = new ArrayDoubleList<Ingredient*>;

    this->copyArrayToList(temp_array, this->list);
    this->deleteInternalArray(temp_array);
    delete temp_array;
//    this->printToStdOut();
}

void MemRepository::copyListToArray(ArrayDoubleList<Ingredient*> *fromList, DynamicArray<Ingredient*> *toArray)
{
    // copy all elements
    for(int i = 0; i < fromList->getLength(); i++)
    {
        Ingredient *old_ingredient = fromList->get(i);
        Ingredient *new_ingredient = new Ingredient(old_ingredient->getId(), old_ingredient->getQuantity(),
                                                    old_ingredient->getName(), old_ingredient->getProducer());
        toArray->add(new_ingredient);
    }
}

void MemRepository::copyArrayToList(DynamicArray<Ingredient*> *fromArray, ArrayDoubleList<Ingredient*> *toList)
{
    // copy all elements
    for(int i = 0; i < fromArray->getLength(); i++)
    {
        Ingredient *old_ingredient = fromArray->get(i);
        Ingredient *new_ingredient = new Ingredient(old_ingredient->getId(), old_ingredient->getQuantity(),
                                                    old_ingredient->getName(), old_ingredient->getProducer());
        printDebug("Adding");
        toList->add(new_ingredient);
    }
}

void MemRepository::addToUndo()
{
    // create new array
    auto new_array = new DynamicArray<Ingredient*>;

    // copy all elements
//    for(int i = 0; i < this->array->getLength(); i++)
//    {
//        Ingredient *old_ingredient = this->array->get(i);
//        Ingredient *new_ingredient = new Ingredient(old_ingredient->getId(), old_ingredient->getQuantity(),
//                                                    old_ingredient->getName(), old_ingredient->getProducer());
//        new_array->add(new_ingredient);
//    }
    this->copyListToArray(this->list, new_array);

//    for(int i = 0; i < new_array->getLength(); i++)
//    {
//        new_array->get(i)->printToStdOut();
//    }

    // add to stack
    this->undo_array[this->undo_array_length] = new_array;
    this->undo_array_length++;
    this->undo_array_max = max(this->undo_array_length, this->undo_array_max);
    assert(this->undo_array_length <= 127);
}

bool MemRepository::undo()
{
    if(this->undo_array_length == 0)
    {
        return false;
    }

    // restore from stack array
    //this->deleteArray();
    //this->array = this->undo_array[this->undo_array_length - 1];

    // list undo
    this->deleteList();
    this->list = new ArrayDoubleList<Ingredient*>;
    this->copyArrayToList(this->undo_array[this->undo_array_length - 1], this->list);

    // because of our new container we must delete it from the undo array manually
    this->deleteInternalArray(this->undo_array[this->undo_array_length - 1]);
    delete this->undo_array[this->undo_array_length - 1];

    this->undo_array_length--;

    return true;
}

void MemRepository::printToStdOut() const
{
    cout << setw(4) << "ID" << setw(15) << "Quantity"
         << setw(30) << "Name" << setw(30) << "Producer" << endl << endl;
//    for(int i = 0; i < this->list->getLength(); i++)
//    {
//        this->list->get(i)->printToStdOut();
//    }
    auto iterator = this->list->getIterator();
    while(iterator->hasNext())
    {
        iterator->next()->printToStdOut();
    }
}

