package view;

import model.IProgramStateObserver;

public class StackObserver implements IProgramStateObserver
{
    public void update(Object arg)
    {
        System.out.println("OBSERVER: Stack modified");
        System.out.println(arg);
    }
}
