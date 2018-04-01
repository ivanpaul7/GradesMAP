package lab.repository;

import java.util.Optional;

public interface GenericRepository<ID,E> {

    void add(E elem);
    void update(ID id, E replacement);
    E get(ID id);
    //Optional<E> delete(ID id);
    E delete(ID id);
    Iterable<E> getAll();
    boolean find(E elem);
    int size();
}
