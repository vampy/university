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
