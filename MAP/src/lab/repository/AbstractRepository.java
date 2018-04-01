package lab.repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lab.domain.HasId;
import lab.validator.Validator;

public abstract class AbstractRepository<ID, E extends HasId<ID>> implements GenericRepository<ID,E> {
    protected Map<ID, E> repoMap;
    protected Validator<E> validator;

    public AbstractRepository(Validator<E> validator) {
        this.validator = validator;
        repoMap = new HashMap<>();
    }


    @Override
    public void add(E elem) {
        //todo
        //validator.validate(elem);
        if (repoMap.containsKey(elem.getId())) {
            throw new RepositoryException("ERR !!! Elem cu id: " + elem.getId() + " e deja in Repository");
        } else {
            repoMap.put(elem.getId(), elem);
        }
    }

    @Override
    public void update(ID id, E newObj) {
        //validator.validate(newObj);
        //todo
        if (!repoMap.containsKey(id)) {
            throw new RepositoryException("element with id " + id + " not in repository");
        }
        if (newObj.getId() != id) {
            throw new RepositoryException("diffrent id");
        }
        repoMap.put(id, newObj);
    }

    @Override
    public E get(ID id) {
        if (!repoMap.containsKey(id))
            throw new RepositoryException("ERR !!! Elem cu id: " + id + " nu e in Repository");
        return repoMap.get(id);
    }
    @Override
    public E delete(ID id){
    //public Optional<E> delete(ID id) {
        //return Optional.ofNullable(repoMap.remove(id));
        if (!repoMap.containsKey(id))
            throw new RepositoryException("ERR !!! Elem cu id: " + id + " nu e in Repository");
        return repoMap.remove(id);
    }

    @Override
    public Iterable<E> getAll() {
        return repoMap.values();
    }

    @Override
    public boolean find(E elem) {
        return repoMap.containsValue(elem);
    }

    @Override
    public int size() {
        return repoMap.values().size();
    }
}
