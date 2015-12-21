#include "taskrepository.h"

#include <QtAlgorithms>

static bool cmpSortTasks(const Task* a, const Task* b) { return a->getName() < b->getName(); }

TaskRepository::TaskRepository(QString filename)
{
    this->filename = filename;
    this->tasks = new QVector<Task*>;
    this->readFromFile();
}

TaskRepository::~TaskRepository()
{
    foreach (Task* task, *tasks)
    {
        delete task;
    }
    delete tasks;
}

void TaskRepository::add(Task* task)
{
    tasks->push_back(task);
    this->sort();
    this->writeToFile();
}

void TaskRepository::sort() { qSort(tasks->begin(), tasks->end(), cmpSortTasks); }

QString TaskRepository::getFilename() const { return filename; }

void TaskRepository::writeToFile()
{
    QFile handle(filename);

    handle.open(QFile::WriteOnly);

    QTextStream out(&handle);

    foreach (Task* task, *tasks)
    {
        out << QString("%1||%2||%3\n")
                   .arg(QString::number(task->getId()), task->getName(), QString::number(task->getHours()));
    }
}

QVector<Task*>* TaskRepository::getTasks() { return tasks; }

void TaskRepository::readFromFile()
{
    if (filename.isEmpty())
    {
        qDebug() << "filename is empty nothing to read from";
        return;
    }

    QFile handle(filename);

    if (!handle.exists()) // create first time then write
    {
        if (!handle.open(QFile::WriteOnly))
        {
            qDebug() << "Error openeing the file for writing";
            return;
        }

        qDebug() << "Writing the file for the first time";
        handle.close();
        return;
    }

    // read from file
    if (!handle.open(QFile::ReadOnly))
    {
        qDebug() << "Error opening file for reading";
        return;
    }

    while (!handle.atEnd())
    {
        QString line = handle.readLine();
        line = line.trimmed();
        if (line.isEmpty())
        {
            continue;
        }

        QStringList fields = line.split("||");

        qDebug() << line;

        assert(fields.length() == 3);
        tasks->push_back(new Task(fields.at(0).toInt(), fields.at(1), fields.at(2).toInt()));
    }
    qDebug() << "read succesfully";

    this->sort();
}
