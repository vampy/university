package view;

import model.IProgramStateObserver;

public class TableObserver implements IProgramStateObserver
{
    public void update(Object arg)
    {
        System.out.println("OBSERVER: Table modified");
        System.out.println(arg);
    }
}
