package gui.homework;

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
import lab.domain.Homework;
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

public class ControllerHomework implements Observer {
    private Service service;
    @FXML private AnchorPane AnchorPaneTable;
    @FXML TableView<Homework> tableView;
    @FXML TableColumn<Homework, String> id,deadline, description;
    @FXML TextField idField, deadlineField;
    @FXML TextArea textAreaDescript;
    @FXML CheckBox checkId, checkDescription, checkDeadline;
    @FXML Button btnAdd, btnClearAll, btnUpdate;
    private ObservableList<Homework> model;

    private final static int rowsPerPage = 25;   //paginated
    private Comparator<Homework> cmpHomeworkDeadline=(x,y)->{
        if(x.getDeadline()==y.getDeadline())
            return 0;
        else if(x.getDeadline()<y.getDeadline())
            return -1;
        return 1;
    };
    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<Homework,String>("id"));
        deadline.setCellValueFactory(new PropertyValueFactory<Homework,String>("deadline"));
        description.setCellValueFactory(new PropertyValueFactory<Homework,String>("description"));
        tableView.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<Homework>() {
                    @Override
                    public void changed(ObservableValue<? extends Homework> observable,
                                        Homework oldValue, Homework newValue) {
                        showHomeworkDetails(newValue);
                    }
                });
    }
    @Override
    public void update(Observable o, Object arg) {
        //model.setAll(service.iterableToList(service.getAllHomeworks()));
        model= FXCollections.observableArrayList(service.filterAndSorter(service.iterableToList(service.getAllHomeworks()), x->true, cmpHomeworkDeadline));
        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        AnchorPaneTable.getChildren().addAll(pagination);
    }


    public void setServices(Service services) {
        this.service = services;
       // model= FXCollections.observableArrayList(this.service.iterableToList(service.getAllHomeworks()));
        model= FXCollections.observableArrayList(service.filterAndSorter(service.iterableToList(service.getAllHomeworks()), x->true, cmpHomeworkDeadline));
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

    @FXML
    public void add(ActionEvent event) throws IOException {
        try {
            service.addTema(createHomeworlFields());
        } catch (NumberFormatException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        } catch (ValidationException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        } catch (ServiceException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        } catch (RepositoryException err){
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        }
    }
    @FXML
    public void delete(ActionEvent event) throws IOException {
//        try {
//            service.removeStudent(Integer.parseInt(idField.getText()));
//        }catch (NumberFormatException err) {
//            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
//        } catch (ValidationException err) {
//            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
//        } catch (ServiceException err) {
//            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
//        } catch (RepositoryException err){
//            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
//        }
    }
    @FXML
    public void update(ActionEvent event) throws IOException {
        try {
            service.updateDeadline(Integer.parseInt(idField.getText()),Integer.parseInt(deadlineField.getText()));
        }catch (NumberFormatException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        } catch (ValidationException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        }  catch (ServiceException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        } catch (RepositoryException err){
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        }
    }
    @FXML
    public void clearAll(ActionEvent event) throws IOException {
        idField.clear();
        textAreaDescript.clear();
        deadlineField.clear();
    }

    public void enableBtnAdmin(boolean admin){
        if(admin){
            btnAdd.setDisable(false);
            btnUpdate.setDisable(false);
            btnClearAll.setDisable(false);
        }else{
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnClearAll.setDisable(true);
        }
    }



    @FXML
    public void filter(ActionEvent event) throws IOException {
        String id=idField.getText();
        String deadline=deadlineField.getText();
        String description = textAreaDescript.getText().toString().replaceAll("[\r\n]+", " ");
        Predicate<Homework> pred = x->true;

        Comparator<Homework> cmpHomeworkId=(x,y)->{
            if(x.getId()==y.getId())
                return 0;
            else if(x.getId()<y.getId())
                return -1;
            return 1;
        };

        //OBS!!!! enumerarea cu ; este fara spatiu
        if(! checkId.isSelected() && !checkDeadline.isSelected() && !checkDescription.isSelected()){
            pred=x->true;
        }else {
            try {
                if (checkId.isSelected()&& !id.isEmpty()) {
                    String[] ids = id.split("[;]");
                    for (String s : ids) {
                        int idx=parseInt(s);
                        pred = pred.and(x -> x.getId() == idx);
                    }
                }
                if (checkDeadline.isSelected()&& !deadline.isEmpty()) {
                    String[] deads = deadline.split("[;]");
                    for (String s : deads) {
                        int ddx = parseInt(s);
                        pred = pred.and(x -> x.getDeadline() == ddx);
                    }
                }
                if (checkDescription.isSelected()&& !description.isEmpty()) {
                    String[] descr = description.split("[;]");
                    for (String s : descr)
                        pred = pred.and(x -> x.getDescription().contains(s));
                }

            } catch (NumberFormatException e){
                pred=x->true;  //eroare filtrare todo
                showMessage(Alert.AlertType.ERROR, "Error", "Insert integer values for filter's fields");
            }
        }

        model= FXCollections.observableArrayList(service.filterAndSorter(service.iterableToList(service.getAllHomeworks()), pred, cmpHomeworkId));
        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        AnchorPaneTable.getChildren().addAll(pagination);
        //tableView.setItems(model);
    }

    private Homework createHomeworlFields(){
        int id= Integer.parseInt(idField.getText());
        int deadline= Integer.parseInt(deadlineField.getText());
        String descr=textAreaDescript.getText().toString().replaceAll("[\r\n]+", " ");
        return new Homework(id,descr,deadline);

    }

    static void showMessage(Alert.AlertType type,String header, String messageText){
        Alert alert=new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(messageText);
        alert.showAndWait();
    }

    public void showHomeworkDetails(Homework newValue) {
        if(newValue!=null){
            idField.setText(""+newValue.getId());
            deadlineField.setText(""+newValue.getDeadline());
            textAreaDescript.setText(newValue.getDescription());
        }
    }



}
