package lab.domain;

public class Grade implements HasId<Integer> {
    private int id, idStudent, value, idHomework;

    public Grade(int id, int idStudent, int idHomework, int value){
        this.id=id;
        this.idStudent=idStudent;
        this.value=value;
        this.idHomework=idHomework;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public int getIdHomework() {
        return idHomework;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public int getValue() {
        return value;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setIdHomework(int idHomework) {
        this.idHomework = idHomework;
    }

    @Override
    public String toString() {
        return "" + id + " -stud " + idStudent + " -hmw " + getIdHomework()+ " -val " +getValue();
    }

}