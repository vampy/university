#include "task.h"

Task::Task(int id, QString name, int hours)
{
    this->setId(id);
    this->setName(name);
    this->setHours(hours);
}

QString Task::getName() const { return name; }

void Task::setName(const QString& value) { name = value; }

int Task::getHours() const { return hours; }

void Task::setHours(int value) { hours = value; }
int Task::getId() const { return id; }

void Task::setId(int value) { id = value; }
