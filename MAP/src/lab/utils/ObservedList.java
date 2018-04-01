package lab.utils;

import java.util.ArrayList;
import java.util.List;

public class ObservedList<E> extends ArrayList<E> implements Observable<E> {
    protected List<Observer<E>> observers=new ArrayList<Observer<E>>();


    @Override
    public void addObserver(Observer<E> obs) { observers.add(obs); }

    @Override
    public void removeObserver(Observer<E> obs) { observers.remove(obs); }

    @Override
    public void notifyObservers(ListEvent<E> listEvent) {
        for(Observer obs:observers)
            obs.notifyEvent(listEvent);
    }

    @Override
    public boolean add(E e){
        boolean ret= super.add(e);
        ListEvent<E> event=createEvent(ListEventType.ADD, e);
        notifyObservers(event);
        return ret;
    }

    @Override
    public E set(int index, E e){
        E ret= super.set(index, e);
        ListEvent<E> event=createEvent(ListEventType.UPDATE, ret);
        notifyObservers(event);
        return ret;
    }

    @Override
    public E remove(int index){
        E ret= super.remove(index);
        ListEvent<E> event=createEvent(ListEventType.REMOVE, ret);
        notifyObservers(event);
        return ret;
    }

    private ListEvent<E> createEvent(ListEventType type, final E elem){
        return new ListEvent<E>(type) {
            @Override
            public ObservedList<E> getList() {
                return ObservedList.this;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }
}
