#ifndef TASKCONTROLLER_H
#define TASKCONTROLLER_H

#include "taskrepository.h"

class TaskController
{
private:
    /*
     * Get repository
    */
    TaskRepository *repository;

public:
    /*
     * Controller constructor
     * @param TaskRepository repository
    */
    TaskController(TaskRepository *repository);

    /*
     * Check if the id exists in the repository
     * @param int id
     *
     * @return bool
    */
    bool existsId(int id);

    /*
     * Add a tasks to the repository
     *
     * @param int id the task id
     * @param QString name the task name
     * @param int hours the hours
     *
     * @return bool
    */
    void addTask(int id, QString name, int hours);

    /*
     * Get all the tasks from the repository
     *
     * @return QVector<Task *> *
    */
    QVector<Task *> *getTasks();


    /*
     * Get all the tasks from the repository which are <= hour_max
     *
     * @param int hour_max
     *
     * @return QVector<Task *>
    */
    QVector<Task *> getTasksByHour(int hour_max);
};

#endif // TASKCONTROLLER_H
