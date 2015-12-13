#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    mainWidget(new QWidget),
    mainSplitterLayout(new QHBoxLayout),
    topLayout(new QHBoxLayout),
    formLayout(new QFormLayout),
    filterLayout1(new QFormLayout),
    filterLayout2(new QHBoxLayout),
    leftLayout(new QVBoxLayout),
    rightLayout(new QVBoxLayout),

    // form widgets
    idLabel(new QLabel),
    idText(new QLineEdit),
    quantityLabel(new QLabel),
    quantityText(new QLineEdit),
    nameLabel(new QLabel),
    nameText(new QLineEdit),
    producerLabel(new QLabel),
    producerText(new QLineEdit),

    // filter widgets,
    filterNameText(new QLineEdit),
    filterNameButton(new QPushButton),
    filterProducerText(new QLineEdit),
    filterProducerButton(new QPushButton),
    filterQuantityText(new QLineEdit),
    filterQuantityCombo(new QComboBox),
    filterQuantityButton(new QPushButton),

    // other widgets
    insertButton(new QPushButton),
    updateButton(new QPushButton),
    deleteButton(new QPushButton),
    undoButton(new QPushButton),
    table(new QTableWidget),
    statusLabel(new QLabel)
{
    // set layout
    this->setCentralWidget(mainWidget);
    mainSplitterLayout->addLayout(leftLayout, 1);
    mainSplitterLayout->addLayout(rightLayout);

    rightLayout->addLayout(topLayout);
    rightLayout->addLayout(formLayout);
    rightLayout->addLayout(filterLayout2);
    rightLayout->addLayout(filterLayout1);

    mainLayout = new QVBoxLayout(mainWidget);
    mainLayout->addLayout(mainSplitterLayout);

    // add widgets to layouts
    topLayout->addWidget(statusLabel);

    leftLayout->addWidget(table);

    rightLayout->addWidget(insertButton);
    rightLayout->addWidget(updateButton);
    rightLayout->addWidget(deleteButton);
    rightLayout->addWidget(undoButton);

    formLayout->addRow(idLabel, idText);
    formLayout->addRow(nameLabel, nameText);
    formLayout->addRow(producerLabel, producerText);
    formLayout->addRow(quantityLabel, quantityText);

    filterLayout1->addRow(filterNameText, filterNameButton);
    filterLayout1->addRow(filterProducerText, filterProducerButton);
    filterLayout2->addWidget(filterQuantityText);
    filterLayout2->addWidget(filterQuantityCombo);
    filterLayout2->addWidget(filterQuantityButton);

    // set widgets options
    this->resize(800, 600);
    this->setMaximumSize(1366, 768);
    filterCombo << "less" << "equal" << "greater";

    // table
    table->setEditTriggers(QAbstractItemView::NoEditTriggers);
    table->setSelectionBehavior(QAbstractItemView::SelectRows);

    table->setColumnCount(4);
    for (int i = 0; i < tableHeader.length(); i++)
    {
        table->setHorizontalHeaderItem(i, new QTableWidgetItem(this->tableHeader[i]));
        table->horizontalHeader()->setSectionResizeMode(i, QHeaderView::Stretch);
    }

    // slots connect
    connect(table->horizontalHeader(), SIGNAL(sectionClicked(int)), this, SLOT(headerClicked(int)));
    connect(table, SIGNAL(itemSelectionChanged()), this,SLOT(itemSelectionChanged()));
    connect(insertButton, SIGNAL(clicked()), this, SLOT(insertClicked()));
    connect(updateButton, SIGNAL(clicked()), this, SLOT(updateClicked()));
    connect(deleteButton, SIGNAL(clicked()), this, SLOT(deleteClicked()));
    connect(undoButton, SIGNAL(clicked()), this, SLOT(undoClicked()));
    connect(filterNameButton, SIGNAL(clicked()), this, SLOT(filterNameClicked()));
    connect(filterQuantityButton, SIGNAL(clicked()), this, SLOT(filterQuantityClicked()));
    connect(filterProducerButton, SIGNAL(clicked()), this, SLOT(filterProducerClicked()));

    // font
    QFont font = this->statusLabel->font();
    font.setPointSize(18);
    this->statusLabel->setFont(font);

    // set default text
    this->setWidgetsText();
}

void MainWindow::setWidgetsText()
{
    this->setWindowTitle("Ingredients program");

    // labels
    idLabel->setText("Id");
    nameLabel->setText("Name");
    producerLabel->setText("Producer");
    quantityLabel->setText("Quantity");
    statusLabel->setText("Status");

    // buttons
    insertButton->setText("Insert");
    updateButton->setText("Update");
    deleteButton->setText("Delete");
    undoButton->setText("Undo");
    filterNameButton->setText("Filter Name");
    filterQuantityButton->setText("Filter Quantity");
    filterProducerButton->setText("Filter Producer");

    // combost
    filterQuantityCombo->addItems(this->filterCombo);
}

void MainWindow::cellClicked(int row, int column)
{
    qDebug() << row << " " << column;
}

void MainWindow::headerClicked(int index)
{
    qDebug() << "clicked: header = " << index;

    // set table header order
    if(sortHeader[index] == -1) // no sort set
    {
        table->sortByColumn(index, Qt::DescendingOrder);
        sortHeader[index] = Qt::DescendingOrder;
    }
    else // set a order
    {
        if(sortHeader[index] == Qt::DescendingOrder)
        {
            table->sortByColumn(index, Qt::AscendingOrder);
            sortHeader[index] = Qt::AscendingOrder;
        }
        else
        {
            table->sortByColumn(index, Qt::DescendingOrder);
            sortHeader[index] = Qt::DescendingOrder;
        }
    }

    // clear
    controller->getRepository()->clear();

    // add to new repo
    for (int row = 0; row < table->rowCount(); row++)
    {
            auto idItem = table->item(row, 0);
            auto nameItem = table->item(row, 1);
            auto producerItem = table->item(row, 2);
            auto quantityItem = table->item(row, 3);

            controller->addIngredient(
                idItem->text().toUInt(),
                quantityItem->text().toUInt(),
                nameItem->text().toStdString(),
                producerItem->text().toStdString()
            );

    }

    this->refreshTableData();
}

void MainWindow::insertClicked()
{
    qDebug() << "clicked: insert button";
    unsigned int id, quantity;
    string name, producer;

    if(!this->validateFormData(id, name, producer, quantity, true))
    {
        return;
    }

    // add to repo
    controller->addIngredient(id, quantity, name, producer);
    statusLabel->setText("Inserted");
    this->refreshTableData();
}

void MainWindow::updateClicked()
{
    qDebug() << "clicked: update button";
    unsigned int id, quantity;
    string name, producer;
    if(!this->validateFormData(id, name, producer, quantity, false))
    {
        return;
    }

    // update the repo
    controller->updateIngredient(id, name, producer, quantity);
    statusLabel->setText("Updated");
    this->refreshTableData();
}

void MainWindow::deleteClicked()
{
    qDebug() << "clicked: delete button";
    unsigned int id;
    if(!this->validateFormId(id, false))
    {
        return;
    }

    // delete from repo
    controller->removeIngredient(id);
    statusLabel->setText("Deleted");
    this->refreshTableData();
}

void MainWindow::undoClicked()
{
    qDebug() << "clicked: undo button";
    if(controller->undo())
    {
        statusLabel->setText("Undone");
        this->refreshTableData();
    }
    else
    {
        statusLabel->setText("Nothing to undo");
    }
}

void MainWindow::itemSelectionChanged()
{
    QList<QTableWidgetItem *> list = table->selectedItems();

    if (!list.empty()) // get first widget
    {
        QTableWidgetItem *firstWidget = list.first();
        qDebug() << "Id to select: " << firstWidget->text().toInt();

        Ingredient *ingredient = controller->getRepository()->getById(firstWidget->text().toUInt());
        idText->setText(QString::number(ingredient->getId()));
        nameText->setText(QString::fromStdString(ingredient->getName()));
        producerText->setText(QString::fromStdString(ingredient->getProducer()));
        quantityText->setText(QString::number(ingredient->getQuantity()));
    }
    else // no items selected clear
    {
        this->clearFormData();
    }

    qDebug() << "Selected non empty items: " << list.count();
}

void MainWindow::filterNameClicked()
{
    qDebug() << "clicked: filter name";
    string name = filterNameText->text().trimmed().toStdString();
    if(!name.length())
    {
        statusLabel->setText("Filter Name is empty");
        return;
    }

    // filter
    controller->filterByName(name);
    statusLabel->setText("");
    this->refreshTableData();
}

void MainWindow::filterProducerClicked()
{
    qDebug() << "clicked: filter producer";
    string producer = filterProducerText->text().trimmed().toStdString();
    if(!producer.length())
    {
        statusLabel->setText("Filter producer is empty");
        return;
    }

    // filter
    controller->filterByProducer(producer);
    statusLabel->setText("");
    this->refreshTableData();

}

void MainWindow::filterQuantityClicked()
{
    qDebug() << "clicked: filter quantity";

    bool okQuantity;
    unsigned int quantity = filterQuantityText->text().toUInt(&okQuantity);
    if(!okQuantity)
    {
        statusLabel->setText("Filter Quantity is not an integer");
        return;
    }

    // set filter type
    int filterType;
    if(filterQuantityCombo->currentIndex() == 0)
    {
        filterType = -1;
    }
    else if(filterQuantityCombo->currentIndex() == 1)
    {
        filterType = 0;
    }
    else
    {
        filterType = 1;
    }

    controller->filterByQuantity(quantity, filterType);
    statusLabel->setText("");
    this->refreshTableData();
}

void MainWindow::setController(Controller *controller)
{
    this->controller = controller;
    this->refreshTableData();
}

void MainWindow::refreshTableData()
{
    Repository *repository = controller->getRepository();
    this->table->setRowCount(repository->getLength());
    for (unsigned int row = 0; row < repository->getLength(); row++)
    {
        Ingredient *ingredient = repository->getByIndex(row);

        // table
        auto w1 = new QTableWidgetItem;
        auto w2 = new QTableWidgetItem;
        auto w3 = new QTableWidgetItem;
        auto w4 = new QTableWidgetItem;
        w1->setData(Qt::EditRole, ingredient->getId());
        w2->setData(Qt::EditRole, QString::fromStdString(ingredient->getName()));
        w3->setData(Qt::EditRole, QString::fromStdString(ingredient->getProducer()));
        w4->setData(Qt::EditRole, ingredient->getQuantity());
        table->setItem(row, 0, w1);
        table->setItem(row, 1, w2);
        table->setItem(row, 2, w3);
        table->setItem(row, 3, w4);
    }
}

bool MainWindow::validateFormId(unsigned int &id, bool idExists)
{
    bool okId;

    id = idText->text().toUInt(&okId);
    if(!okId)
    {
        statusLabel->setText("Id is not an integer");
        return false;
    }

    // default behaviour to invalidate
    if(idExists == controller->idExists(id))
    {
        if(idExists)
        {
            statusLabel->setText(QString("Id %1 already exists").arg(id));
        }
        else
        {
            statusLabel->setText(QString("Id %1 does not exist").arg(id));
        }


        return false;
    }

    return true;
}

bool MainWindow::validateFormData(unsigned int &id,
                                  string &name,
                                  string &producer,
                                  unsigned int &quantity,
                                  bool idExists)
{
    if(!this->validateFormId(id, idExists))
    {
        return false;
    }

    bool okQuantity;
    quantity = quantityText->text().toUInt(&okQuantity);
    if(!okQuantity)
    {
        statusLabel->setText("Quantity is not an integer");
        return false;
    }

    name = nameText->text().trimmed().toStdString();
    if(!name.length())
    {
        statusLabel->setText("Name is empty");
        return false;
    }

    producer = producerText->text().trimmed().toStdString();
    if(!producer.length())
    {
        statusLabel->setText("Producer is empty");
        return false;
    }

    return true;
}

void MainWindow::clearFormData()
{
    idText->clear();
    nameText->clear();
    producerText->clear();
    quantityText->clear();
}
