package gui.statis;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.print.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import lab.domain.Grade2;
import lab.domain.Homework;
import lab.domain.Student;
import lab.service.Service;
import javafx.scene.control.TextField;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

public class ControllerStatistics implements Observer {
    private Service service;
    @FXML
    PieChart pieChart;
    @FXML
    BarChart barCharStudDelivery;
    @FXML
    TextField txtFieldSt;
    @FXML
    TableView<Homework> tableView;
    @FXML
    TableColumn<Homework, String> deadline, description;
    @FXML
    AnchorPane AnchorPaneWrap;
    private ObservableList<Homework> model;


    @FXML
    public void initialize() {
        deadline.setCellValueFactory(new PropertyValueFactory<Homework,String>("deadline"));
        description.setCellValueFactory(new PropertyValueFactory<Homework,String>("description"));
    }
    @Override
    public void update(Observable o, Object arg) {
        setBarCharStudDelivery();
    }

    public void setServices(Service services) {
        this.service = services;
        setBarCharStudDelivery();
        model= FXCollections.observableArrayList(this.service.iterableToList(service.getAllHomeworks()));
        tableView.setItems(model);
    }


    public void setBarCharStudDelivery(){
        Map<Integer, Integer> vectorAparitii=service.nrHmkPerStudent();
        int nrHmks = (int)service.getAllHomeworks().spliterator().getExactSizeIfKnown();
        int tenPercent=nrHmks/10;

        Map<Integer, Integer> studPercent=new HashMap<Integer, Integer>();

        for (Map.Entry<Integer, Integer> entry : vectorAparitii.entrySet()) {
            int length = String.valueOf(entry.getValue()).length();
            //int columnNumber=(int) Math.ceil(((float)entry.getValue()/(int) Math.pow(10, length) )* 10);
            int columnNumber=(int) Math.ceil(((float)entry.getValue()/nrHmks )* 10);
            if (studPercent.get(columnNumber ) == null) {
                studPercent.put(columnNumber  , 1);
            } else {
                studPercent.put(columnNumber, studPercent.get(columnNumber) + 1);
            }
        }

        for (int i=0;i<=10;i++) {
            studPercent.putIfAbsent(i, 0);
        }

        barCharStudDelivery.setTitle("Student Delivery %");
        XYChart.Series series1 = new XYChart.Series();

        for (Map.Entry<Integer, Integer> entry : studPercent.entrySet()) {
            series1.getData().add(new XYChart.Data(""+entry.getKey()*10+" %", entry.getValue()));
        }
        barCharStudDelivery.getData().clear();
        barCharStudDelivery.getData().addAll(series1);
    }

    @FXML public void updateTableWithStudent(){
        List<Homework> homeworks= new ArrayList<>();
        String idS;
        try {
            idS = txtFieldSt.getText();

            if (!idS.isEmpty()) {
                String[] ids = idS.split("[;]");
                for (String s : ids) {
                    try {
                        int id = Integer.parseInt(s);
                        Student st = service.getStudent(id);

                        for (Homework hmk : service.getAllHomeworks()) {
                            boolean found = false;
                            for (Grade2 gr : service.getAllGrades()) {
                                if (Objects.equals(gr.getId().getKey().getId(), st.getId()) && Objects.equals(gr.getId().getValue().getId(), hmk.getId())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found && !homeworks.contains(hmk))
                                homeworks.add(hmk);
                        }
                    } catch (NumberFormatException ignored) {
                        Student st = null;
                        for (Student std : service.getAllStudents()) {
                            if (std.getName().contains(s)){
                                st = std;
                                break;
                            }
                        }
                        if (st != null) {
                            for (Homework hmk : service.getAllHomeworks()) {
                                boolean found = false;
                                for (Grade2 gr : service.getAllGrades()) {
                                    if (gr.getId().getKey().getId() == st.getId() && gr.getId().getValue().getId() == hmk.getId()) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found && !homeworks.contains(hmk))
                                    homeworks.add(hmk);
                            }
                        }
                    }
                }
                setPieChart((int)service.getAllHomeworks().spliterator().getExactSizeIfKnown(), homeworks.size());
                tableView.setItems(FXCollections.observableArrayList(homeworks));
            }
        }catch (Exception  x){
            setPieChart(1,1);
        }

    }

    private void setPieChart(int totalNumberKmk, int nerezolvate) {
        int x= (int) Math.round(((float)nerezolvate/totalNumberKmk)*100);
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Undone", x),
                        new PieChart.Data("Done", 100-x));
        pieChart.setData(pieChartData);
        pieChart.setTitle("%Homeworks for a Student");
    }

    @FXML void saveToPDF(){
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout
                = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterAttributes attr = printer.getPrinterAttributes();
        PrinterJob job = PrinterJob.createPrinterJob();
        double scaleX
                = pageLayout.getPrintableWidth() / AnchorPaneWrap.getBoundsInParent().getWidth();
        double scaleY
                = pageLayout.getPrintableHeight() / AnchorPaneWrap.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);

        //Scale scale = new Scale(0.9, 0.62);


        AnchorPaneWrap.getTransforms().add(scale);

        if (job != null && job.showPrintDialog(AnchorPaneWrap.getScene().getWindow())) {
            boolean success = job.printPage(pageLayout, AnchorPaneWrap);
            if (success) {
                job.endJob();

            }
        }
        AnchorPaneWrap.getTransforms().remove(scale);
    }


}
