package lab.validator;
import lab.domain.*;

public class GradeValidator implements Validator<Grade2>{
    public void validate(Grade2 t) {
//        if(t.getId()<0)
//            throw new ValidationException("ERR! Note's id must be pozitive");
        if(t.getId().getKey().getId()<0)
            throw new ValidationException("ERR! Student's id must be pozitive");
        if(t.getId().getValue().getId()<0)
            throw new ValidationException("ERR! Homework's id must be pozitive");
        if(t.getValue()<0 || t.getValue() >10)
            throw new ValidationException("ERR! Note's id must be pozitive");
    }
}

