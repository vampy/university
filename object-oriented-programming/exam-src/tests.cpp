#ifndef TESTS_H
#define TESTS_H

#include "taskcontroller.h"

void testTask()
{
    Task a(0, "", 0);
    assert(a.getId() == 0);
    assert(a.getName() == "");
    assert(a.getHours() == 0);

    a.setId(2);
    assert(a.getId() == 2);

    a.setName("Name");
    assert(a.getName() == "Name");

    a.setHours(55);
    assert(a.getHours() == 55);

    Task b(0, "", 0);
    assert(b.getId() == 0);
    assert(b.getName() == "");
    assert(b.getHours() == 0);

    b.setId(1232);
    assert(b.getId() == 1232);

    b.setName("BBBB");
    assert(b.getName() == "BBBB");

    b.setHours(9999);
    assert(b.getHours() == 9999);
}

void testRepositoryAndController()
{
    TaskRepository repository("tests.dat");
    assert(repository.getFilename() == "tests.dat");

    repository.add(new Task(1, "Task1", 5));

    auto tasks = repository.getTasks();

    assert(tasks->at(0)->getId() == 1);
    assert(tasks->at(0)->getName() == "Task1");
    assert(tasks->at(0)->getHours() == 5);
}

void testAll()
{
    qDebug() << "Testing started";
    testTask();
    testRepositoryAndController();
    qDebug() << "Testing ended";
}

#endif // TESTS_H
