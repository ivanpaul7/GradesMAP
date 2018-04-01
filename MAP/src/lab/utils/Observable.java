package lab.utils;

public interface Observable<E> {
    void addObserver(Observer<E> obs);
    void removeObserver(Observer<E> obs);
    void notifyObservers(ListEvent<E> listEvent);
}
