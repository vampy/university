package model;

import java.util.ArrayList;
import java.util.List;

public class ProgramStateObservable
{
    private List<IProgramStateObserver> stackObservers = new ArrayList<IProgramStateObserver>();
    private List<IProgramStateObserver> outObservers   = new ArrayList<IProgramStateObserver>();
    private List<IProgramStateObserver> tableObservers = new ArrayList<IProgramStateObserver>();

    public static final int TYPE_STACK = 1;
    public static final int TYPE_TABLE = 2;
    public static final int TYPE_OUT   = 3;

    public void registerObserver(IProgramStateObserver observer, int type)
    {
        switch (type)
        {
            case TYPE_STACK:
                stackObservers.add(observer);
                break;
            case TYPE_TABLE:
                tableObservers.add(observer);
                break;
            case TYPE_OUT:
                outObservers.add(observer);
                break;
            default:
                System.out.println("ERROR: INVALID TYPE registerObserver");
                break;
        }
    }

    public void notifyObservers(Object arg, int type)
    {
        List<IProgramStateObserver> observers;
        switch (type)
        {
            case TYPE_STACK:
                observers = stackObservers;
                break;
            case TYPE_TABLE:
                observers = tableObservers;
                break;
            case TYPE_OUT:
                observers = outObservers;
                break;
            default:
                System.out.println("ERROR: INVALID TYPE registerObserver");
                return;
        }
        
        for (IProgramStateObserver observer : observers)
        {
            observer.update(arg);
        }
    }
}
