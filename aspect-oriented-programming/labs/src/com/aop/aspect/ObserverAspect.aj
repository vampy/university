package com.aop.aspect;

import com.aop.controller.GUIController;
import com.aop.controller.TerminalController;
import com.aop.model.Library;

import java.util.ArrayList;
import java.util.List;

public aspect ObserverAspect
{
//    declare parents: hasmethod(@SubjectChanged * *(..)) implements Subject;

    private List<Observer> Subject.observers = new ArrayList<Observer>();
    public void Subject.addObserver(Observer obs)
        {
            observers.add(obs);
        }
    public void Subject.removeObserver(Observer obs)
        {
            observers.remove(obs);

        }
    public void Subject.notifyObservers(Object o)
        {
            for (Observer ob : observers)
            {
                ob.update(o);
            }
        }

    // observer pointcut
    pointcut ObservableLibrary(Library lib): (execution(@SubjectChanged * *(..))) && this(lib);
    private Library library;

    // add Observer
    after(Library lib, TerminalController controller): initialization(com.aop.controller.TerminalController.new(*))
        && this(controller) && args(lib)
        {
            System.out.println("Inside ObserverAspect: TerminalController.add");
            lib.addObserver(controller);
            library = lib;
        }
    after(Library lib, GUIController controller): initialization(com.aop.controller.GUIController.new(*))
        && this(controller) && args(lib)
        {
            System.out.println("Inside ObserverAspect: GUIController.add");
            lib.addObserver(controller);
            library = lib;
        }

    // observers notification
    after(Library lib): ObservableLibrary(lib)
        {
            System.out.println("Inside ObserverAspect: notifyObservers");
            lib.notifyObservers(null);
        }


    // update Observer
    public void TerminalController.update(Object o)
        {
            System.out.println("Inside ObserverAspect: TerminalController.update");
            reset();
        }
    public void GUIController.update(Object o)
        {
            System.out.println("Inside ObserverAspect: GUIController.update");
            setTableBooksFromLibrary();
        }

    // remove Observer
    after(TerminalController controller): execution(* TerminalController.close()) && this(controller)
        {
            System.out.println("Inside ObserverAspect: TerminalController.remove");
            library.removeObserver(controller);
        }
    after(GUIController controller): execution(* GUIController.close()) && this(controller)
        {
            System.out.println("Inside ObserverAspect: GUIController.remove");
            library.removeObserver(controller);
        }
}
