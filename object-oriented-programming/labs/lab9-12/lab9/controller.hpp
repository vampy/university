#ifndef CONTROLLER_H_
#define CONTROLLER_H_

#include <string>
using namespace std;

#include "mem_repository.hpp"


class Controller
{
public:
    Controller(Repository*);


    /**
    * Add ingredient
    *
    * unsigned int id - the id
    * unsigned int - quantity
    * string name - the name
    * string producer - the producer
    *
    * return true if ingredient added, false if id exists
    */
    bool addIngredient(unsigned int id, unsigned int quantity, string name, string producer);

    /**
    * Remove ingredient by id
    *
    * unsigned int id - the id to remove
    *
    * return true if ingredient removed, false if id exists
    */
    bool removeIngredient(unsigned int);

    /**
    * Undo the last operation
    *
    * return true if last operation undone, false if nothing to undo
    */
    bool undo();

    /**
    * Update an ingredient
    *
    * unsigned int idToUpdate - the id of the ingredient
    * string name - new name
    * string producer - new producer
    * unsigned int quantity - new quantity
    *
    * return true if name changed, false if id does not exist
    */
    bool updateIngredient(unsigned int idToUpdate, string name, string producer, unsigned int quantity);

    /**
    * Filter by quantity
    *
    * unsigned int quantity
    * unsigned int filter_type
    */
    void filterByQuantity(unsigned int, int);

    /**
    * Filter by name
    *
    * ustring name
    */
    void filterByName(string);

    /**
    * Filter by name
    *
    * string name
    */
    void filterByProducer(string);

    /**
    * Sort by producer
    *
    * bool reverse - sort in descending order
    */
    void sortByProducer(bool);

    /**
    * Sort by producer
    *
    * bool reverse - sort in descending order
    */
    void sortByName(bool);

    /**
    * Sort by quantity
    *
    * bool reverse - sort in descending order
    */
    void sortByQuantity(bool);

    /**
    * Get the repository
    * return true if quantity changed, false if id does not exist
    */
    Repository *getRepository();

    bool idExists(unsigned int id);

protected:
    Repository *repository;
};

#endif // CONTROLLER_H_
