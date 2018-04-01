package gui.student;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lab.domain.Student;
import lab.repository.RepositoryException;
import lab.service.Service;
import lab.validator.ServiceException;
import lab.validator.ValidationException;

import java.io.IOException;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;
public class ControllerStudent implements Observer {
    private Service service;
    @FXML TableView<Student> tableView;
    @FXML TableColumn<Student, String> id, name, group, email, professor;
    @FXML TextField idField, nameField, profField, emailField, groupField;
    @FXML CheckBox checkId, checkName, checkProf, checkEmail, checkGroup;
    @FXML Button btnAdd, btnDelete, btnClearAll, btnUpdate, btnFilter;
    @FXML AnchorPane AnchorPaneTable;
    private ObservableList<Student> model;


    private final static int rowsPerPage = 25;
    private Comparator<Student> compStudentId=(x,y)->{
        if(x.getId()==y.getId())
            return 0;
        else if(x.getId()<y.getId())
            return -1;
        return 1;
    };


    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        group.setCellValueFactory(new PropertyValueFactory<Student,String>("group"));
        email.setCellValueFactory(new PropertyValueFactory<Student,String>("email"));
        professor.setCellValueFactory(new PropertyValueFactory<Student,String>("professor"));
        tableView.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<Student>() {
                    @Override
                    public void changed(ObservableValue<? extends Student> observable,
                                        Student oldValue, Student newValue) {
                        showStudentDetails(newValue);
                    }
                });
    }


    @Override
    public void update(Observable o, Object arg) {
        model= FXCollections.observableArrayList(service.filterAndSorter(service.iterableToList(service.getAllStudents()), x->true, compStudentId));
        //model.setAll(service.iterableToList(service.getAllStudents()));
        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        AnchorPaneTable.getChildren().addAll(pagination);
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
        //model= FXCollections.observableArrayList(this.service.iterableToList(service.getAllStudents()));
        model= FXCollections.observableArrayList(service.filterAndSorter(service.iterableToList(service.getAllStudents()), x->true, compStudentId));
        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        //AnchorPaneTable=new AnchorPane(pagination);
        AnchorPaneTable.getChildren().addAll(pagination);

        //tableView.setItems(model);
        //todo decomenteaza linia de mai jos
        enableBtnAdmin(false);
    }


    public void enableBtnAdmin(boolean admin){
        if(admin){
            btnAdd.setDisable(false);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnClearAll.setDisable(false);
        }else{
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            btnClearAll.setDisable(true);
        }
    }

    @FXML
    public void add(ActionEvent event) throws IOException {
        try {
            service.addStudent(createStudentFields());
        } catch (NumberFormatException | RepositoryException | ServiceException | ValidationException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        }
    }
    @FXML
    public void delete(ActionEvent event) throws IOException {
        try {
            service.removeStudent(Integer.parseInt(idField.getText()));
        }catch (NumberFormatException | ValidationException | RepositoryException | ServiceException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        }
    }
    @FXML
    public void update(ActionEvent event) throws IOException {
        try {
            service.updateStudent(createStudentFields());
        }catch (NumberFormatException | ValidationException | RepositoryException | ServiceException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        }
    }
    @FXML
    public void clearAll(ActionEvent event) throws IOException {
        idField.clear();
        nameField.clear();
        groupField.clear();
        profField.clear();
        emailField.clear();
        //idField.setDisable(false);
    }

    public void isAdmin(boolean bool){
    }

    @FXML
    public void filter(ActionEvent event) throws IOException {
        String id=idField.getText();
        String group=groupField.getText();
        String name = nameField.getText();
        String prof = profField.getText();
        String email = emailField.getText();
        Predicate<Student> pred = x->true;


        //OBS!!!! enumerarea cu ; este fara spatiu
        if(! checkProf.isSelected() && !checkEmail.isSelected() && !checkName.isSelected() && !checkGroup.isSelected() && !checkId.isSelected()){
            pred=x->true;
        }else {
            try {
                if (checkId.isSelected() && !id.isEmpty()) {
                    String[] ids = id.split("[;]");
                    for (String s : ids){
                        int ix=parseInt(s);
                        pred = pred.and(x -> x.getId() == ix);
                    }
                }
                if (checkName.isSelected()&& !name.isEmpty()) {
                    String[] names = name.split("[;]");
                    for (String s : names)
                        pred = pred.and(x -> x.getName().contains(s));
                }
                if (checkGroup.isSelected()&& !group.isEmpty()) {
                    String[] grs = group.split("[;]");
                    for (String s : grs) {
                        int grx=parseInt(s);
                        pred = pred.and(x -> x.getGroup() == grx);
                    }
                }
                if (checkEmail.isSelected()&& !email.isEmpty()) {
                    String[] emails = email.split("[;]");
                    for (String s : emails)
                        pred = pred.and(x -> x.getEmail().contains(s));
                }
                if (checkProf.isSelected()&& !prof.isEmpty()) {
                    String[] profs = prof.split("[;]");
                    for (String s : profs)
                        pred = pred.and(x -> x.getProfessor().contains(s));
                }
            } catch (NumberFormatException e){
                pred=x->true;  //eroare filtrare todo
                showMessage(Alert.AlertType.ERROR, "Error", "Insert integer values for filter's fields");
            }
        }


        model= FXCollections.observableArrayList(service.filterAndSorter(service.iterableToList(service.getAllStudents()), pred, compStudentId));
        //tableView.setItems(model);

        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        AnchorPaneTable.getChildren().addAll(pagination);
    }

    private Student createStudentFields(){
        int id= Integer.parseInt(idField.getText());
        String name=nameField.getText();
        int group= Integer.parseInt(groupField.getText());
        String email=emailField.getText();
        String professor=profField.getText();
        return new Student(id, name, group, email, professor);
    }

    static void showMessage(Alert.AlertType type,String header, String messageText){
        Alert alert=new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(messageText);
        alert.showAndWait();
    }

    public void showStudentDetails(Student newValue) {
        if(newValue!=null){
            idField.setText(""+newValue.getId());
            nameField.setText(newValue.getName());
            groupField.setText(""+newValue.getGroup());
            emailField.setText(newValue.getEmail());
            profField.setText(newValue.getProfessor());
        }
    }
}