#include <QApplication>

#include "mytasks.h"
#include "tests.cpp"

int main(int argc, char* argv[])
{
    testAll();
    QApplication app(argc, argv);

    auto repository = new TaskRepository();
    auto controller = new TaskController(repository);
    auto window     = new MyTasks();
    window->show();
    window->setController(controller);
    window->updateView();

    int returnCode = app.exec();

    delete window;
    delete controller;
    delete repository;
    return returnCode;
}
