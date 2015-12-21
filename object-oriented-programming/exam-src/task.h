#ifndef TASK_H
#define TASK_H

#include <QString>
#include <QDebug>
#include <cassert>
#include <QtAlgorithms>

class Task
{
private:
    int id;
    QString name;
    int hours;

public:
    /**
     * Task constructor
     *
     * @param int id the task id
     * @param QString name the task name
     * @param int hours the hours
     */
    Task(int id, QString name, int hours);

    /**
     * @return int id the task id
     */
    int getId() const;

    /**
     * @param int id the task id
     */
    void setId(int value);

    /**
     * @return QString name the task name
     */
    QString getName() const;

    /**
     * @param QString name the task name
     */
    void setName(const QString& value);

    /**
     * @return int hours the task hours
     */
    int getHours() const;

    /**
     * @param int hours the hours
     */
    void setHours(int value);
};

#endif // TASK_H
