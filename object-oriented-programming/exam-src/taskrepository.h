#ifndef TASKREPOSITORY_H
#define TASKREPOSITORY_H

#include <QVector>
#include <QFile>
#include <QStringList>

#include "task.h"

class TaskRepository
{
private:
    QVector<Task*> *tasks;
    QString filename;

    /*
     * Read from file into the qvector
    */
    void readFromFile();
public:
    TaskRepository(QString filename = "tasks.dat");
    ~TaskRepository();

    /*
     * Add a task to the repo
     *
     * @param Task* task
    */
    void add(Task *task);

    /*
     * Sort the repo by name ascending
    */
    void sort();

    /*
     * Get the current filename repo
     *
     * @return QString the filename
    */
    QString getFilename() const;

    /*
     * Write to file
    */
    void writeToFile();

    /*
     * Get the repo tasks
     *
     * @return QVector<Task *> *
    */
    QVector<Task *> *getTasks();
};

#endif // TASKREPOSITORY_H
