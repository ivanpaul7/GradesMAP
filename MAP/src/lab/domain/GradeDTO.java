package lab.domain;

public class GradeDTO {
    private int  idH, deadline, value, currentWeek;
    private String observation;

    public GradeDTO(Homework hm, Grade gr, String observation, int currentWeek){
        this.observation=observation;
        this.idH=hm.getId();
        this.value=gr.getValue();
        this.deadline=hm.getDeadline();
        this.currentWeek=currentWeek;
    }

    @Override
    public String toString() {
        return " nr tema: "+idH+" ;  nota: "+value+" ;  deadline saptamana: "+deadline+" ;  saptamana notarii: "+currentWeek+" ;  OBS: "+observation;
    }

    public int getIdH() {
        return idH;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getValue() {
        return value;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public String getObservation() {
        return observation;
    }

    public void setIdH(int idH) {
        this.idH = idH;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

}
