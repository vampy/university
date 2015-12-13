QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TEMPLATE = app

SOURCES += \
    main.cpp \
    mainwindow.cpp \
    controller.cpp \
    file_repository.cpp \
    ingredient.cpp \
    mem_repository.cpp \
    tests.cpp \
    util.cpp

HEADERS += \
    mainwindow.h \
    controller.hpp \
    file_repository.hpp \
    ingredient.hpp \
    list.hpp \
    mem_repository.hpp \
    repository.hpp \
    repository_filters.hpp \
    tests.hpp \
    util.hpp \
    vector.hpp

QMAKE_CXXFLAGS += -std=c++11 -Wall
