package lab.domain;

import javafx.util.Pair;

public class Grade2 implements HasId<Pair<Student, Homework>> {
    private Pair<Student, Homework> id;
    private int  value;

    public Grade2(Pair<Student, Homework> id, int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void setId(Pair<Student, Homework> integerIntegerPair) {
        this.id=integerIntegerPair;
    }

    @Override
    public Pair<Student, Homework> getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    public int getIdHomework() {
        return id.getValue().getId();
    }
    public int getDeadlineHomework() {
        return id.getValue().getDeadline();
    }
    public String getDescriptionHomework() {
        return id.getValue().getDescription();
    }

    public int getIdStudent() {
        return id.getKey().getId();
    }
    public int getGroupStudent(){return id.getKey().getGroup();}
    public String getProfessorStudent() {
        return id.getKey().getProfessor();
    }
    public String getNameStudent() {
        return id.getKey().getName();
    }
    public String getEmailStudent() {
        return id.getKey().getEmail();
    }





    @Override
    public String toString() {
        return "Grade2{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}