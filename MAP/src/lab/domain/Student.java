package lab.domain;

public class Student implements HasId<Integer>{
    private int id, group;
    private String name, professor, email;

    public Student(int id, String name, int group, String email, String professor){
        this.email=email;
        this.group=group;
        this.id=id;
        this.name=name;
        this.professor=professor;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }


    public void setGroup(int grupa) {
        this.group = grupa;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getProfessor() {
        return professor;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return ""+ this.id + ", " + this.name + ", " + this.group + ", " + this.email+ ", " + this.professor;
    }
}
