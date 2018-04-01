package lab.repository.FILE;

import lab.domain.Student;
import lab.validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;

// AbstractFileRepository  sau AbstractFileRepositoryStream -totuna, doar loading difera(nu il afecteaza)
public class StudentFileRepository extends AbstractFileRepositoryStream<Integer, Student> {
    public StudentFileRepository(String numeFis, Validator<Student> val){
        super(numeFis,val);
        loading();
    }

    @Override
    protected Student buildObject(String[] s){
        if(s.length !=5){
            System.out.println("Linie invalida:   "+ s);
        }
        try{
            int id=Integer.parseInt(s[0]);
            int group=Integer.parseInt(s[2]);
            Student st=new Student(id,s[1], group, s[3], s[4]);
            return  st;
        }catch(NumberFormatException e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter(numeFis)){
            for(Student st: getAll()){
                String line=""+st.getId()+"|"+st.getName()+"|"+st.getGroup()+"|"+st.getEmail()+"|"+st.getProfessor();
                pw.println(line);
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
}
