package view;

import model.IProgramStateObserver;

public class OutObserver implements IProgramStateObserver
{
    public void update(Object arg)
    {
        System.out.println("OBSERVER: Out modified");
        System.out.println(arg);
    }
}
