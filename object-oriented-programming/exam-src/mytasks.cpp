#include "mytasks.h"

MyTasks::MyTasks(QWidget* parent)
    : QMainWindow(parent),
      m_main_widget(new QWidget),
      m_list(new QListWidget),

      // add
      m_id_label(new QLabel),
      m_id_value(new QLineEdit),
      m_name_label(new QLabel),
      m_name_value(new QLineEdit),
      m_hour_label(new QLabel),
      m_hour_value(new QLineEdit),
      m_add_button(new QPushButton),

      // count
      m_count_label(new QLabel),

      // filter
      m_slider_label(new QLabel),
      m_slider_value(new QSlider)
{
    // setup main
    this->setCentralWidget(m_main_widget);
    m_main_layout = new QGridLayout(m_main_widget);

    // layout
    m_main_layout->addWidget(m_list, 0, 0, 1, -1);
    m_main_layout->addWidget(m_id_label, 1, 0);
    m_main_layout->addWidget(m_id_value, 1, 1);
    m_main_layout->addWidget(m_name_label, 2, 0);
    m_main_layout->addWidget(m_name_value, 2, 1);
    m_main_layout->addWidget(m_hour_label, 3, 0);
    m_main_layout->addWidget(m_hour_value, 3, 1);
    m_main_layout->addWidget(m_add_button, 4, 1);
    m_main_layout->addWidget(m_slider_label, 6, 0);
    m_main_layout->addWidget(m_slider_value, 6, 1);
    m_main_layout->addWidget(m_count_label, 8, 1);

    this->setWidgetsText();
    this->setWidgetsConnections();
    this->resize(400, 400);
}

MyTasks::~MyTasks() {}

void MyTasks::setWidgetsText()
{
    m_id_label->setText("Id: ");
    m_name_label->setText("Name: ");
    m_hour_label->setText("Hours: ");
    m_add_button->setText("Add");
    m_slider_label->setText("Filter hours " + QString::number(m_hours_max));
    m_slider_value->setMinimum(1);
    m_slider_value->setMaximum(10);
    m_slider_value->setValue(m_hours_max);
    m_slider_value->setOrientation(Qt::Horizontal);
}

void MyTasks::setWidgetsConnections()
{
    connect(m_add_button, SIGNAL(clicked()), this, SLOT(onAddClicked()));
    connect(m_slider_value, SIGNAL(valueChanged(int)), this, SLOT(onFilterSliderChanged(int)));
}

void MyTasks::updateView()
{
    m_list->clear();
    auto tasks = controller->getTasksByHour(m_hours_max);

    for (auto i = 0; i < tasks.size(); i++)
    {
        auto task = tasks.at(i);

        m_list->addItem(QString("%1\t%2\t%3")
                            .arg(QString::number(task->getId()), task->getName(), QString::number(task->getHours())));
    }

    // update counter
    m_count_label->setText("Tasks count: " + QString::number(controller->getTasks()->size()));
}

void MyTasks::onAddClicked()
{
    qDebug() << "Add clicked";
    QString errors, name;

    bool okId, okHours, okName = true;
    int id    = m_id_value->text().toInt(&okId);
    int hours = m_hour_value->text().toInt(&okHours);
    name      = m_name_value->text().trimmed();

    if (!okId)
    {
        errors += "Id is not an integer number\n";
    }

    if (!okHours)
    {
        errors += "Hours is not an integer number\n";
    }

    if (name.isEmpty())
    {
        okName = false;
        errors += "Name is empty";
    }

    if (okId && okHours && okName)
    {
        qDebug() << "validate adding";
        // id
        if (controller->existsId(id))
        {
            errors += "That id already exists\n";
        }

        // hours
        if (hours < 1 || hours > 10)
        {
            errors += "Hours need to be between 1 and 10\n";
        }
    }

    if (errors.isEmpty()) // add
    {
        controller->addTask(id, name, hours);
        this->updateView();
    }
    else
    {
        QMessageBox::warning(this, "ERROR", errors);
    }
}

void MyTasks::onFilterSliderChanged(int value)
{
    qDebug() << "Filter value changed to " + QString::number(value);
    m_slider_label->setText("Filter hours " + QString::number(value));
    m_hours_max = value;
    this->updateView();
}

TaskController* MyTasks::getController() const { return controller; }

void MyTasks::setController(TaskController* value) { controller = value; }
