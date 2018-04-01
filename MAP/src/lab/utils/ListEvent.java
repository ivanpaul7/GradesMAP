package lab.utils;

public abstract class ListEvent<E> {
    private ListEventType type;

    public ListEvent(ListEventType type){
        this.type=type;
    }

    public ListEventType getType() {
        return type;
    }

    public void setType(ListEventType type) {
        this.type = type;
    }

    public abstract Iterable<E> getList();
    public abstract E getElement();

}
