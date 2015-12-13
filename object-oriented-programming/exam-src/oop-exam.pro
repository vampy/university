QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

QMAKE_CXXFLAGS += -std=c++11 -g -Wall -Wextra -Wformat=2

SOURCES += \
    main.cpp \
    task.cpp \
    taskrepository.cpp \
    taskcontroller.cpp \
    tests.cpp \
    mytasks.cpp

HEADERS += \
    task.h \
    taskrepository.h \
    taskcontroller.h \
    mytasks.h
