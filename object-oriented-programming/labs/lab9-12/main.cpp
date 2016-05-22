// Create graphical user interface for the application using Qt
#include <QApplication>

#include "controller.hpp"
#include "file_repository.hpp"
#include "list.hpp"
#include "mainwindow.h"
#include "mem_repository.hpp"
#include "tests.hpp"
#include "vector.hpp"

using namespace std;

int main(int argc, char* argv[])
{
    // run tests
    runTests();

    // actual program
    auto repository = new FileRepository();
    // repository->toString();
    auto controller = new Controller(repository);
    controller->addIngredient(1, 10, "Rom", "RomRomania");
    controller->addIngredient(2, 30, "Salam", "Carmolimp");
    controller->addIngredient(3, 20, "Varza", "Carmolimp");
    controller->addIngredient(4, 2, "Cheese", "Dalia");

    QApplication app(argc, argv);
    MainWindow window;
    window.setController(controller);
    window.show();

    int returnCode = app.exec();

    // clean up
    delete controller;
    delete repository;

    return returnCode;
}
