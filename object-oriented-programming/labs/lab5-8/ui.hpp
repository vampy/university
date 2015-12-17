#ifndef UI_H_
#define UI_H_

#include <string>
using namespace std;

#include "controller.hpp"

//#define UI_CONVERT_AND_CHECK_ID(_id_str, _id_int)               \
//    if(!stringToInt(_id_str, _id_int))                          \
//    {                                                           \
//        isError = true;                                         \
//        this->printToStdOut("Error: Id is not an int");         \
//    }                                                           \
//    else if(_id_int < 0)                                        \
//    {                                                           \
//        isError = true;                                         \
//        this->printToStdOut("Error: Id can not be negative.");  \
//    }

class UI
{
public:
    UI(Controller*, string);
    void run();

protected:
    Controller *controller;
    string startupMessage;

    static void showMenu();

    void printIngredients() const;
    void readIngredient();
    void removeIngredient();
    void modifyName();
    void modifyProducer();
    void modifyQuantity();

    void filterIngredientsQuantity();
    void filterByName();

    void sortByProducer();
    void sortByName();
    void sortByQuantity();

    static void printToStdOut(string);

    void convertAndValidateId(bool&, string&, int&) const;
    void convertAndValidateQuantity(bool&, string&, int&) const;

    void undo();

};

#endif // UI_H_
