package lab.repository.FILE;

import lab.domain.Homework;
import lab.domain.Password;
import lab.validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;


public class PasswordFileRepository extends AbstractFileRepositoryStream<String, Password> {
    public PasswordFileRepository(String numeFis, Validator<Password> val){
        super(numeFis, val);
        loading();
    }

    @Override
    protected Password buildObject(String[] s){
        if(s.length !=3){
            System.out.println("Linie invalida:   "+ s);
        }
        try{
            //int id=Integer.parseInt(s[0]);
            Password psw=new Password(s[0], s[1],s[2]);
            return  psw;
        }catch(NumberFormatException e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter(numeFis)){
            for(Password ps: getAll()){
                String line=""+ps.getId()+"|"+ps.getPassword();
                pw.println(line);
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }
}
