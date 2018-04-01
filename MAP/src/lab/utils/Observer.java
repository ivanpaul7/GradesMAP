package lab.utils;

public interface Observer<E> {
    void notifyEvent(ListEvent<E> list);
}
