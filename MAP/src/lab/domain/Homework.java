package lab.domain;

public class Homework implements HasId<Integer> {
    private int id, deadline;
    private String description;

    public Homework(int id, String description, int deadline){
        this.id=id;
        this.description=description;
        this.deadline=deadline;
    }

    public Homework(Homework other) {
        this.id = other.id;
        this.deadline = other.deadline;
        this.description = other.description;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public Integer getId() {
        return this.id;
    }

    public int getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "" + id + ". " + description + ",  deadline: " + deadline;
    }
}
