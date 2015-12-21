#include "taskcontroller.h"

TaskController::TaskController(TaskRepository* repository) { this->repository = repository; }

bool TaskController::existsId(int id)
{
    auto tasks = repository->getTasks();
    foreach (Task* task, *tasks)
    {
        if (task->getId() == id)
        {
            return true;
        }
    }

    return false;
}

void TaskController::addTask(int id, QString name, int hours) { repository->add(new Task(id, name, hours)); }

QVector<Task*>* TaskController::getTasks()
{
    assert(this->repository != NULL);
    return repository->getTasks();
}

QVector<Task*> TaskController::getTasksByHour(int hour_max)
{
    QVector<Task*> filtered_tasks;
    auto tasks = repository->getTasks();
    for (auto i = 0; i < tasks->size(); i++)
    {
        auto task = tasks->at(i);
        if (task->getHours() <= hour_max)
        {
            filtered_tasks.push_back(task);
        }
    }

    return filtered_tasks;
}
