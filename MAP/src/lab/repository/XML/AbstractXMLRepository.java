package lab.repository.XML;

import lab.domain.HasId;
import lab.repository.AbstractRepository;
import lab.validator.Validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class AbstractXMLRepository<ID, E  extends HasId<ID>> extends AbstractRepository<ID, E > {
    protected String numeFis;
    public AbstractXMLRepository(String numeFis, Validator validator) {
        super(validator);
        this.numeFis=numeFis;
    }

    protected abstract void writeToFile();

    protected abstract void loading();


    @Override
    public void add(E e){
        super.add(e);
        writeToFile();
    }

    @Override
    public E delete(ID id){
        E elem=super.delete(id);
        writeToFile();
        return elem;
    }

    @Override
    public void update(ID id, E newObj) {
        super.update(id, newObj);
        writeToFile();
    }
}

