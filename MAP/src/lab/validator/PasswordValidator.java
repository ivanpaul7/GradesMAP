package lab.validator;
import lab.domain.Password;

public class PasswordValidator implements Validator<Password> {
    public void validate(Password t) {
        if (t.getId() =="") {
            throw new ValidationException("ERR! Homework id " + t.getId() + " < 0");
        }
        if (t.getPassword() =="") {
            throw new ValidationException("ERR! Homework id " + t.getId() + " < 0");
        }
    }

}