package project.aspect;

import project.controller.GUIController;
import project.controller.TerminalController;
import project.model.SalesAgency;

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
    pointcut ObservableSalesAgency(SalesAgency salAg): execution(@SubjectChanged * *(..)) && this(salAg);
    private SalesAgency salesAgency;

    // add Observer
    after(SalesAgency salAg, TerminalController tc) : initialization(project.controller.TerminalController.new(*))
        && this(tc) && args(salAg)
        {
            System.out.println("Inside ObserverAspect: TerminalController.add");
            salAg.addObserver(tc);
            salesAgency = salAg;
        }
    after(SalesAgency salAg, GUIController controller) : initialization(project.controller.GUIController.new(*))
        && this(controller) && args(salAg)
        {
            System.out.println("Inside ObserverAspect: GUIController.add");
            salAg.addObserver(controller);
            salesAgency = salAg;
        }

    // observers notification
    after(SalesAgency salAg): ObservableSalesAgency(salAg)
        {
            System.out.println("Inside ObserverAspect: notifyObservers");
            salAg.notifyObservers(null);
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
            setTableProductsFromSalesAgency();
        }

    // remove Observer
    after(TerminalController controller): execution(* TerminalController.close()) && this(controller)
        {
            System.out.println("Inside ObserverAspect: TerminalController.remove");
            salesAgency.removeObserver(controller);
        }
    after(GUIController controller): execution(* GUIController.close()) && this(controller)
        {
            System.out.println("Inside ObserverAspect: GUIController.remove");
            salesAgency.removeObserver(controller);
        }
}
