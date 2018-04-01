package lab.repository;

import lab.domain.HasId;
import lab.validator.Validator;

public class HomeworkRepository<Integer, Tema extends HasId<Integer>> extends AbstractRepository<Integer, Tema> {
    public HomeworkRepository(Validator<Tema> val) {
        super(val);
    }

}
