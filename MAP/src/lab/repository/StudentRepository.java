package lab.repository;

import lab.domain.HasId;
import lab.validator.Validator;

public class StudentRepository<Integer, Student extends HasId<Integer>> extends AbstractRepository<Integer, Student> {

    public StudentRepository(Validator<Student> val) {
        super(val);
    }

}
