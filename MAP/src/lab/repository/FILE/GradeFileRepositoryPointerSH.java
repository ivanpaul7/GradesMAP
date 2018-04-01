package lab.repository.FILE;

import javafx.util.Pair;
import lab.domain.Grade;
import lab.domain.Grade2;
import lab.domain.Homework;
import lab.domain.Student;
import lab.repository.GenericRepository;
import lab.validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;

// AbstractFileRepository  sau AbstractFileRepositoryStream -totuna, doar loading difera(nu il afecteaza)
public class GradeFileRepositoryPointerSH extends AbstractFileRepositoryStream<Pair<Student,Homework>, Grade2> {
    GenericRepository<Integer, Student> studRepo;
    GenericRepository<Integer, Homework> hmkRepo;
    public GradeFileRepositoryPointerSH(String numeFis, Validator<Grade2> val, GenericRepository<Integer, Student> studRepo, GenericRepository<Integer, Homework> hmkRepo){
        super(numeFis,val);
        this.hmkRepo=hmkRepo;
        this.studRepo=studRepo;
        loading();
    }

    @Override
    protected Grade2 buildObject(String[] s){
        if(s.length !=3){
            System.out.println("GradeFileRepo- Linie invalida:   "+ s);
        }
        try{
            int idS=Integer.parseInt(s[0]);
            int idH=Integer.parseInt(s[1]);
            int val=Integer.parseInt(s[2]);
            Grade2 gr=new Grade2(new Pair<Student,Homework>(studRepo.get(idS), hmkRepo.get(idH)), val);
            return  gr;
        }catch(NumberFormatException e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter(numeFis)){
            for(Grade2 gr: getAll()){
                String line=""+gr.getId().getKey().getId()+"|"+gr.getId().getValue().getId()+"|"+gr.getValue();
                pw.println(line);
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
}
