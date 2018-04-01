package lab.validator;
import lab.domain.Homework;

public class HomeworkValidator implements Validator<Homework> {
    public void validate(Homework t) {
        if (t.getId() < 0) {
            throw new ValidationException("ERR! Homework id " + t.getId() + " < 0");
        }

        if (t.getDeadline() < 1 || t.getDeadline() > 14) {
            throw new ValidationException("ERR! Homework deadline " + t.getDeadline() + " should be:  2 <= deadline <= 14");
        }

        if (t.getDescription().isEmpty()) {
            throw new ValidationException("ERR! Homework description is empty");
        }
    }

}