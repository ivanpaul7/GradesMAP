package lab.repository.FILE;

import lab.domain.Grade;
import lab.validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;

// AbstractFileRepository  sau AbstractFileRepositoryStream -totuna, doar loading difera(nu il afecteaza)
public class GradeFileRepository extends AbstractFileRepositoryStream<Integer, Grade> {
    public GradeFileRepository(String numeFis, Validator<Grade> val){
        super(numeFis,val);
        loading();
    }

    @Override
    protected Grade buildObject(String[] s){
        if(s.length !=4){
            System.out.println("GradeFileRepo- Linie invalida:   "+ s);
        }
        try{
            int id=Integer.parseInt(s[0]);
            int idS=Integer.parseInt(s[1]);
            int idH=Integer.parseInt(s[2]);
            int val=Integer.parseInt(s[3]);
            Grade gr=new Grade(id,idS, idH, val);
            return  gr;
        }catch(NumberFormatException e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter(numeFis)){
            for(Grade gr: getAll()){
                String line=""+gr.getId()+"|"+gr.getIdStudent()+"|"+gr.getIdHomework()+"|"+gr.getValue();
                pw.println(line);
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
}
