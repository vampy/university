QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TEMPLATE = app

SOURCES += *.cpp

HEADERS += *.h *.hpp

QMAKE_CXXFLAGS += -std=c++11 -Wall -g
