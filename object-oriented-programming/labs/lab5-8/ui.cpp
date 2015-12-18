#include <iostream>
#include <string>
#include <cstdlib>
#include <iomanip>

#include "ui.hpp"
#include "controller.hpp"
#include "util.hpp"

using namespace std;

UI::UI(Controller* controller, string message = "")
{
    this->controller = controller;
    this->startupMessage = message;
}

void UI::showMenu()
{
    cout << endl
         << endl
         << "Chose an option: " << endl
         << "\t1 - Add an ingredient" << endl
         << "\t2 - Modify an ingredients name" << endl
         << "\t3 - Modify an ingredients quantity" << endl
         << "\t4 - Modify an ingredients producer" << endl
         << "\t5 - Remove an ingredient" << endl
         << endl
         << "\t6 - Filter ingredients by quantity" << endl
         << "\t7 - Filter ingredients by name" << endl
         << endl
         << "\t8 - Undo last modification" << endl
         << "\t9 - Show all ingredients" << endl
         << endl
         << "\t10 - Sort by producer" << endl
         << "\t11 - Sort by name" << endl
         << "\t12 - Sort by quantity" << endl
         << endl
         << "\t99 - Show this help menu" << endl
         << "\t0 - Exit" << endl;
}

void UI::printToStdOut(string error)
{
    cout << endl
         << error << endl;
}

void UI::convertAndValidateId(bool& isError, string& id_str, int& id_int) const
{
    if (!stringToInt(id_str, id_int))
    {
        isError = true;
        printError("Id is not an int");
    }
    else if (id_int < 0)
    {
        isError = true;
        printError("Id can not be negative");
    }
}

void UI::convertAndValidateQuantity(bool& isError, string& quantity_str, int& quantity_int) const
{
    if (!stringToInt(quantity_str, quantity_int))
    {
        isError = true;
        printError("Quantity is not an int");
    }
    else if (quantity_int < 0)
    {
        isError = true;
        printError("Quantity can not be negative.");
    }
}

void UI::readIngredient()
{
    string name, producer, id_str, quantity_str;
    int id_int, quantity_int;
    bool isError = false;

    // read
    cout << "Add ingredient: " << endl;
    cout << "Id: ";
    getline(cin, id_str);

    cout << "Quantity: ";
    getline(cin, quantity_str);

    cout << "Name: ";
    getline(cin, name);

    cout << "Producer: ";
    getline(cin, producer);

    // validate
    this->convertAndValidateId(isError, id_str, id_int);
    this->convertAndValidateQuantity(isError, quantity_str, quantity_int);

    // all good ?
    if (!isError)
    {
        bool isAdded = this->controller->addIngredient(id_int, quantity_int, name, producer);
        if (isAdded)
            printSuccess("Ingredient added");
        else
            printError("Ingredient with that ID already exists");
    }
}

void UI::removeIngredient()
{
    string id_str;
    int id_int;
    bool isError = false;

    // read
    cout << "Remove ingredient: " << endl;
    cout << "Ingredient ID: ";
    getline(cin, id_str);

    // validate
    this->convertAndValidateId(isError, id_str, id_int);

    // all good ?
    if (!isError)
    {
        bool isRemoved = this->controller->removeIngredient(id_int);
        if (isRemoved)
            printSuccess("Ingredient removed");
        else
            printError("Ingredient with that ID does not exist");
    }
}

void UI::modifyName()
{
    string name, id_str;
    int id_int;
    bool isError = false;

    // read
    cout << "Modify name" << endl;
    cout << "Ingredient ID: ";
    getline(cin, id_str);

    cout << "New name: ";
    getline(cin, name);

    // convert
    this->convertAndValidateId(isError, id_str, id_int);

    // all good ?
    if (!isError)
    {
        bool exists = this->controller->changeName(id_int, name);
        if (exists)
            printSuccess("Name updated");
        else
            printError("Id does not exist");
    }
}

void UI::modifyProducer()
{
    string producer, id_str;
    int id_int;
    bool isError = false;

    // read
    cout << "Modify producer" << endl;
    cout << "Ingredient ID: ";
    getline(cin, id_str);

    cout << "New producer: ";
    getline(cin, producer);

    // convert
    this->convertAndValidateId(isError, id_str, id_int);

    // all good ?
    if (!isError)
    {
        bool exists = this->controller->changeProducer(id_int, producer);
        if (exists)
            printSuccess("Producer updated");
        else
            printError("Id does not exist");
    }
}

void UI::modifyQuantity()
{
    string quantity_str, id_str;
    int id_int, quantity_int;
    bool isError = false;

    // read
    cout << "Modify quantity" << endl;
    cout << "Ingredient ID: ";
    getline(cin, id_str);

    cout << "New quantity: ";
    getline(cin, quantity_str);

    // validate
    this->convertAndValidateId(isError, id_str, id_int);
    this->convertAndValidateQuantity(isError, quantity_str, quantity_int);

    // all good ?
    if (!isError)
    {
        bool exists = this->controller->changeQuantity(id_int, quantity_int);
        if (exists)
            printSuccess("Quantity updated");
        else
            printError("Id does not exist");
    }
}

void UI::filterByName()
{
    string name;

    // read
    cout << "Filter by name" << endl;
    cout << "Name: ";
    getline(cin, name);

    // filter and print
    this->controller->filterByName(name);
    this->printIngredients();
}

void UI::filterIngredientsQuantity()
{
    string quantity_str, filter_type_str;
    int quantity_int, filter_type_int;
    bool isError = false;

    // read type of filter
    cout << "Filter by quantity" << endl;
    cout << "Choose type (-1 = less, 0 = equal, 1 = greater): ";
    getline(cin, filter_type_str);

    // read
    cout << "Quantity: ";
    getline(cin, quantity_str);

    // validate
    this->convertAndValidateQuantity(isError, quantity_str, quantity_int);

    if (!stringToInt(filter_type_str, filter_type_int))
    {
        isError = true;
        printError("Filter type is not an int");
    }
    else if (filter_type_int != -1 && filter_type_int != 0 && filter_type_int != 1)
    {
        isError = true;
        printError("Filter type is not valid. Please choose from -1, 0 or 1");
    }

    // filter
    if (!isError)
    {
        this->controller->filterByQuantity(quantity_int, filter_type_int);
        this->printIngredients();
    }
}

void UI::sortByProducer()
{
    string order_str;
    cout << "Sort descending [y/n] (enter to default to no): ";
    getline(cin, order_str);

    if (order_str == "y")
    {
        this->controller->sortByProducer(true);
        printSuccess("Sorted by producer descending");
    }
    else
    {
        this->controller->sortByProducer(false);
        printSuccess("Sorted by producer ascending");
    }
}

void UI::sortByName()
{
    string order_str;
    cout << "Sort descending [y/n] (enter to default to no): ";
    getline(cin, order_str);

    if (order_str == "y")
    {
        this->controller->sortByName(true);
        printSuccess("Sorted by name descending");
    }
    else
    {
        this->controller->sortByName(false);
        printSuccess("Sorted by name ascending");
    }
}

void UI::sortByQuantity()
{
    string order_str;
    cout << "Sort descending [y/n] (enter to default to no): ";
    getline(cin, order_str);

    if (order_str == "y")
    {
        this->controller->sortByQuantity(true);
        printSuccess("Sorted by quantity descending");
    }
    else
    {
        this->controller->sortByQuantity(false);
        printSuccess("Sorted by quantity ascending");
    }
}

void UI::undo()
{
    bool undone = this->controller->undo();

    if (undone)
        printSuccess("Undone");
    else
        printWarning("Nothing to undo");
}

void UI::printIngredients() const
{
    cout << "All ingredients" << endl;
    this->controller->getRepository()->printToStdOut();
}

void UI::run()
{
    string optionStr;
    int optionInt;

    // initial message
    cout << this->startupMessage << endl;
    this->showMenu();

    while (true)
    {
        cout << "Command: ";
        getline(cin, optionStr);

        if (stringToInt(optionStr, optionInt))
        {
            switch (optionInt)
            {
            case 1: // add an ingredient
                this->readIngredient();
                break;
            case 2: // modify name
                this->modifyName();
                break;
            case 3: // modify quantity
                this->modifyQuantity();
                break;
            case 4: // modify producer
                this->modifyProducer();
                break;
            case 5: // remove ingredient
                this->removeIngredient();
                break;
            case 6: // Filter ingredients by quantity
                this->filterIngredientsQuantity();
                break;
            case 7: // Filter ingredients filtered by name
                this->filterByName();
                break;
            case 8: // undo
                this->undo();
                break;
            case 9: // show all
                this->printIngredients();
                break;
            case 10: // sort by producer
                this->sortByProducer();
                break;
            case 11: // sort by name
                this->sortByName();
                break;
            case 12: // sort by quantity
                this->sortByQuantity();
                break;
            case 0: // quit
                cout << "Quiting..." << endl;
                return;
                break;
            case 99: // show this menu
                this->showMenu();
                break;
            default:
                cout << "Invalid option Please try again" << endl;
                break;
            }
        }
        else
        {
            cout << "Invalid option please try again" << endl;
        }
    }
}
