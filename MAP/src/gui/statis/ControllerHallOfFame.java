package gui.statis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import lab.domain.Grade2;
import lab.domain.Student;
import lab.domain.StudentHOF;
import lab.repository.RepositoryException;
import lab.service.Service;
import lab.validator.ServiceException;
import lab.validator.ValidationException;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ControllerHallOfFame implements Observer {
    private Service service;
    @FXML private TableView<StudentHOF> tableView;
    @FXML private TableColumn<StudentHOF, String> position, name, group, average, professor;
    @FXML private Button saveToPDF;
    @FXML private AnchorPane AnchorPaneTable, AnchorPaneMain;
    private ObservableList<StudentHOF> model;


    private final static int rowsPerPage = 17;

    @FXML
    public void initialize() {
        position.setCellValueFactory(new PropertyValueFactory<StudentHOF,String>("position"));
        name.setCellValueFactory(new PropertyValueFactory<StudentHOF,String>("name"));
        group.setCellValueFactory(new PropertyValueFactory<StudentHOF,String>("group"));
        average.setCellValueFactory(new PropertyValueFactory<StudentHOF,String>("average"));
        professor.setCellValueFactory(new PropertyValueFactory<StudentHOF,String>("professor"));
    }


    @Override
    public void update(Observable o, Object arg) {
        model= FXCollections.observableArrayList(getList());
        tableView.setItems(model);
//        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
//        pagination.setPageFactory(this::createPage);
//        AnchorPaneTable.getChildren().addAll(pagination);
    }

    //paginated
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, model.size());
        tableView.setItems(FXCollections.observableArrayList(model.subList(fromIndex, toIndex)));
        return new AnchorPane(tableView);
    }


    public void setServices(Service services) {
        this.service = services;
        model= FXCollections.observableArrayList(getList());
        tableView.setItems(model);
//        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
//        pagination.setPageFactory(this::createPage);
//        AnchorPaneTable.getChildren().addAll(pagination);
    }


    private List<StudentHOF> getList(){
         List<StudentHOF> list =new ArrayList<StudentHOF>();

        int i=1;
        for (Map.Entry<Integer, Integer> entry : service.sortMAPByValueReverse(service.averageGrade()).entrySet()) {
            Student st =  service.getStudent(entry.getKey());
            if(entry.getValue()<5 || i==16)
                break;
            list.add(new StudentHOF(i, entry.getValue(), st.getName(), st.getGroup(), st.getProfessor()));
            i++;
        }
        return list;
    }





    @FXML
    public void setSaveToPDF(ActionEvent event) throws IOException {
        try {
            //printNode(tableView);
            printNode(AnchorPaneMain);
        }catch (NoSuchMethodException| InstantiationException| IllegalAccessException msg){
            showMessage(Alert.AlertType.ERROR, null, "PDF errror creation  \n"+msg);
        }
    }

    public void printNode(final Node node) throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout
                = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterAttributes attr = printer.getPrinterAttributes();
        PrinterJob job = PrinterJob.createPrinterJob();
//        double scaleX
//                = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
//        double scaleY
//                = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
//        Scale scale = new Scale(scaleX, scaleY);

        Scale scale = new Scale(1, 0.616);


        node.getTransforms().add(scale);

        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            boolean success = job.printPage(pageLayout, node);
            if (success) {
                job.endJob();

            }
        }
        node.getTransforms().remove(scale);
    }


    static void showMessage(Alert.AlertType type,String header, String messageText){
        Alert alert=new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(messageText);
        alert.showAndWait();
    }



}