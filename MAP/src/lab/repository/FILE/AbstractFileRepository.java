package lab.repository.FILE;

import lab.domain.HasId;
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


public abstract class AbstractFileRepository<ID, E  extends HasId<ID> > extends AbstractRepository<ID, E > {
    protected String numeFis;
    public AbstractFileRepository(String numeFis,Validator validator) {
        super(validator);
        this.numeFis=numeFis;
    }

    protected abstract void writeToFile();

    protected void loading(){
        try(BufferedReader br=new BufferedReader(new FileReader(numeFis ))){
            String linie;
            while((linie= br.readLine())!=null){
                String[] s=linie.split("[|]");
                E st=buildObject(s);
                super.add(st);
            }
        }catch(IOException e){
            System.err.println(e);
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
