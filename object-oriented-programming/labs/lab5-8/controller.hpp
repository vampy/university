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
    bool addIngredient(unsigned int, unsigned int, string, string);

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
    * Change the name of an ingredient
    *
    * unsigned int id - the id of the ingredient
    * string name - new name
    *
    * return true if name changed, false if id does not exist
    */
    bool changeName(unsigned int, string);

    /**
    * Change the producer of an ingredient
    *
    * unsigned int id - the id of the ingredient
    * string producer - new producer
    *
    * return true if producer changed, false if id does not exist
    */
    bool changeProducer(unsigned int, string);

    /**
    * Change the quantity of an ingredient
    *
    * unsigned int id - the id of the ingredient
    * unsigned int quantity - new quantity
    *
    * return true if quantity changed, false if id does not exist
    */
    bool changeQuantity(unsigned int, unsigned int);


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
    * unsigned int name
    */
    void filterByName(string);

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

protected:
    Repository *repository;
};

#endif // CONTROLLER_H_
