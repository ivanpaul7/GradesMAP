package lab.validator;

import lab.domain.Student;

public class StudentValidator implements Validator<Student> {
    public void validate(Student st) {
        if (st.getId() < 0) {
            throw new ValidationException("ERR! Student id " + st.getId() + "< 0");
        }

        if (st.getEmail().isEmpty()) {
            throw new ValidationException("ERR! Student email is empty");
        }

        if (st.getGroup() <= 0) {
            throw new ValidationException("ERR! Student group " + st.getGroup() + " <= 0");
        }

        if (st.getName().isEmpty()) {
            throw new ValidationException("ERR! Student name is empty");
        }

        if (st.getProfessor().isEmpty()) {
            throw new ValidationException("ERR! Student professor is empty");
        }
    }
}