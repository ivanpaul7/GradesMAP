package lab.repository.FILE;

import lab.domain.Homework;
import lab.validator.Validator;
import java.io.IOException;
import java.io.PrintWriter;

// AbstractFileRepository  sau AbstractFileRepositoryStream -totuna, doar loading difera(nu il afecteaza)
public class HomeworkFileRepository extends AbstractFileRepositoryStream<Integer, Homework> {
    public HomeworkFileRepository(String numeFis, Validator<Homework> val){
        super(numeFis, val);
        loading();
    }

    @Override
    protected Homework buildObject(String[] s){
        if(s.length !=3){
            System.out.println("Linie invalida:   "+ s);
        }
        try{
            int id=Integer.parseInt(s[0]);
            int deadline=Integer.parseInt(s[2]);
            Homework hm=new Homework(id,s[1], deadline);
            return  hm;
        }catch(NumberFormatException e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter(numeFis)){
            for(Homework tm: getAll()){
                String line=""+tm.getId()+"|"+tm.getDescription()+"|"+tm.getDeadline();
                pw.println(line);
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
}
