package lab.domain;

public class StudentHOF {
    private int position, average, group;
    private String name, professor;

    public StudentHOF(int position, int average, String name, int group, String professor) {
        this.position = position;
        this.average = average;
        this.group = group;
        this.name = name;
        this.professor = professor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    @Override
    public String toString() {
        return "StudentHOF{" +
                "position=" + position +
                ", average=" + average +
                ", group=" + group +
                ", name='" + name + '\'' +
                ", professor='" + professor + '\'' +
                '}';
    }
}
