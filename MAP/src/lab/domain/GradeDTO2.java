package lab.domain;

import javafx.util.Pair;

public class GradeDTO2  {
    private int  currentWeek;
    private String observation;
    private Grade2 grade;
//    private Homework homework;
//    private Student student;

    public GradeDTO2( Grade2 gr, int currentWeek, String observation) {
        this.currentWeek = currentWeek;
        this.observation = observation;
        this.grade=gr;
//        this.student=std;
//        this.homework=hmk;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Grade2 getGrade() {
        return grade;
    }

    public void setGrade(Grade2 grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "GradeDTO2{" +
                " nameStudent='"+ grade.getId().getKey().getName() + '\'' +
                ", nr tema:=" + grade.getId().getValue().getId() +
                ", deadline=" + grade.getId().getValue().getDeadline() +
                ", value=" + grade.getValue() +
                ", currentWeek=" + currentWeek +
                ", observation='" + observation + '\'' +
                '}';
    }
}
