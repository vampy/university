package project.controller;
import project.aspect.Observer;

public abstract class AbstractController implements Observer
{
    abstract public void close();
}
