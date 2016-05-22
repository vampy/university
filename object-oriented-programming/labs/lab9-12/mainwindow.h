#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QComboBox>
#include <QDebug>
#include <QFont>
#include <QFormLayout>
#include <QGroupBox>
#include <QHBoxLayout>
#include <QLabel>
#include <QLayout>
#include <QLineEdit>
#include <QList>
#include <QMainWindow>
#include <QPushButton>
#include <QString>
#include <QStringList>
#include <QVBoxLayout>
#include <QVector>
#include <QWidget>

#include <QAbstractItemView>
#include <QHeaderView>
#include <QTableWidget>
#include <QTableWidgetItem>

#include "controller.hpp"
#include "ingredient.hpp"
#include "repository.hpp"

#define DELETE_POINTER(pointer) \
    delete (pointer);           \
    pointer = NULL;

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget* parent = 0);
    void setController(Controller*);

public slots:
    void cellClicked(int, int);
    void headerClicked(int);
    void itemSelectionChanged();
    void insertClicked();
    void updateClicked();
    void deleteClicked();
    void undoClicked();
    void filterNameClicked();
    void filterQuantityClicked();
    void filterProducerClicked();

private:
    Controller* controller;
    QVector<int> sortHeader      = {-1, -1, -1, -1};
    QVector<QString> tableHeader = {"id", "Name", "Producer", "Quantity"};
    QStringList filterCombo; // for filterQuantityCombo

    // main widget
    QWidget* mainWidget;

    // layouts
    QVBoxLayout* mainLayout;         // holds all the items
    QHBoxLayout* mainSplitterLayout; // splits the screen in two
    QHBoxLayout* topLayout;          // the status message
    QFormLayout* formLayout;
    QFormLayout* filterLayout1;
    QHBoxLayout* filterLayout2;
    QVBoxLayout* leftLayout;
    QVBoxLayout* rightLayout;

    // form widgets
    QLabel* idLabel;
    QLineEdit* idText;
    QLabel* quantityLabel;
    QLineEdit* quantityText;
    QLabel* nameLabel;
    QLineEdit* nameText;
    QLabel* producerLabel;
    QLineEdit* producerText;

    // filter widgets
    QLineEdit* filterNameText;
    QPushButton* filterNameButton;
    QLineEdit* filterProducerText;
    QPushButton* filterProducerButton;
    QLineEdit* filterQuantityText;
    QComboBox* filterQuantityCombo;
    QPushButton* filterQuantityButton;

    // other widgets
    QPushButton* insertButton;
    QPushButton* updateButton;
    QPushButton* deleteButton;
    QPushButton* undoButton;
    QTableWidget* table;
    QLabel* statusLabel;

    void setWidgetsText();
    void refreshTableData();
    void clearFormData();
    bool
    validateFormData(unsigned int& id, string& name, string& producer, unsigned int& quantity, bool idExists = true);
    bool validateFormId(unsigned int& id, bool idExists = true);
};

#endif // MAINWINDOW_H
