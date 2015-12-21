#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QList>
#include <QVector>
#include <QString>
#include <QStringList>
#include <QDebug>
#include <QWidget>
#include <QMainWindow>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <QLayout>
#include <QFormLayout>
#include <QHBoxLayout>
#include <QVBoxLayout>
#include <QGridLayout>
#include <QListWidget>
#include <QMessageBox>
#include <QSlider>

#include "taskcontroller.h"

class MyTasks : public QMainWindow
{
    Q_OBJECT

public:
    explicit MyTasks(QWidget* parent = 0);
    ~MyTasks();

    /**
     * Get the controller
     * @return TaskController
     */
    TaskController* getController() const;

    /**
     * Set the controller
     * @param TaskController *value
     */
    void setController(TaskController* value);

    /**
     * Update the view
     */
    void updateView();

public slots:
    void onAddClicked();
    void onFilterSliderChanged(int value);

private:
    // main widget
    QWidget* m_main_widget;

    // add
    QListWidget* m_list;
    QLabel* m_id_label;
    QLineEdit* m_id_value;
    QLabel* m_name_label;
    QLineEdit* m_name_value;
    QLabel* m_hour_label;
    QLineEdit* m_hour_value;
    QPushButton* m_add_button;

    // label count
    QLabel* m_count_label;

    // filter
    QLabel* m_slider_label;
    QSlider* m_slider_value;
    int m_hours_max = 5;

    // layouts
    QGridLayout* m_main_layout; // holds all the items

    // other
    /**
     * Set the texts and limitations to all widgets
     */
    void setWidgetsText();

    /**
     * Set the slot/signal connections
     */
    void setWidgetsConnections();

    TaskController* controller;
};

#endif // MAINWINDOW_H
