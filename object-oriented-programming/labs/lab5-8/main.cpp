/*
 * P7. Bakery
 * Create an application to manage the stock of ingredients (name, provider ,quantity) in a bakery.
 * 1. Add a new ingredient
 * 2. Modify  ingredient
 * 3. Remove ingredient
 * 4. Filter
 *      1. By quantity
 *      2. By name
 * 5. Undo the last operation
 *      - undo – the last operation that has modified the list of ingredients is cancelled
*/

#include <iostream>
#include <string>

#include "vector.hpp"
#include "list.hpp"
#include "mem_repository.hpp"
#include "file_repository.hpp"
#include "controller.hpp"
#include "ui.hpp"
#include "tests.hpp"

using namespace std;

int main()
{
    // run tests
    runTests();

    // actual program
    auto repository = new FileRepository();
    auto controller = new Controller(repository);
    UI ui(controller, "Bakery program");

    //    controller->addIngredient(1, 10, "Rom", "RomRomania");
    //    controller->addIngredient(2, 30, "Salam", "Carmolimp");
    //    controller->addIngredient(3, 20, "Varza", "Carmolimp");
    //    controller->addIngredient(4, 2, "Cheese", "Dalia");

    ui.run();

    // clean up
    delete controller;
    delete repository;

    return 0;
}
