package lab.repository.FILE;

import lab.domain.HasId;
import lab.domain.Student;
import lab.repository.AbstractRepository;
import lab.validator.Validator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class AbstractFileRepositoryStream<ID, E  extends HasId<ID>> extends AbstractRepository<ID, E > {
    protected String numeFis;
    public AbstractFileRepositoryStream(String numeFis,Validator validator) {
        super(validator);
        this.numeFis=numeFis;
    }

    protected abstract void writeToFile();

    protected void loading(){

        try(Stream<String> s= Files.lines(Paths.get(numeFis))) {
            List<E> list=s.map(x->buildObject(x.split("[|]"))).collect(Collectors.toList());
            for(E e:list)
                super.add(e);
        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }


    protected abstract E buildObject(String[] s);

    @Override
    public void add(E e){
        super.add(e);
        writeToFile();
    }

//    @Override
//    public Optional<E> delete(ID id) {
//        Optional<E> oldE=super.delete(id);
//        if(oldE.isPresent()){
//            writeToFile();
//            return oldE;
//        }
//        return Optional.empty();
//    }

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