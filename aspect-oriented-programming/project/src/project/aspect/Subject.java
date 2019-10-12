package project.aspect;

public interface Subject
{
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(Object newData);
}
