package gui.grade;

import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;
import lab.domain.Grade;
import lab.domain.Grade2;
import lab.domain.Homework;
import lab.domain.Student;
import lab.repository.RepositoryException;
import lab.service.Service;
import lab.validator.ServiceException;
import lab.validator.ValidationException;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

import static java.lang.Integer.parseInt;

public class ControllerGrade implements Observer {
    private Service service;
    @FXML private TableView<Grade2> tableView;
    @FXML private TableColumn<Grade2, String>  idStudent,idHomework,  nameStd, descriptionHmk, groupStudent;
    @FXML private TableColumn<Grade2, Number> value ;
    @FXML private TextField  idSField, idHField, valueField;
    @FXML private TextArea textArea;
    @FXML private CheckBox  checkIdStudent, checkIdHomework, checkValue;
    @FXML private ComboBox<String> idCombo, comboHomeworkSeries, comboGroupSeries;
    @FXML private ToggleButton btnToggleNormal, btnToggleSeries;
    @FXML private AnchorPane anchorPaneNormalMode, anchorPaneSeriesMode;
    @FXML private AnchorPane AnchorPaneTable;
    private ToggleGroup group;
    private ObservableList<Grade2> model;
    private ObservableList<String> comboHmkSeriesList, comboGroupSeriesList;
    private List<Grade2> listSeriesAdd;

    private final static int rowsPerPage = 25;   //paginated

    @FXML
    public void initialize() {
        idStudent.setCellValueFactory(new PropertyValueFactory<Grade2,String>("idStudent"));
        idHomework.setCellValueFactory(new PropertyValueFactory<Grade2,String>("idHomework"));
        groupStudent.setCellValueFactory(new PropertyValueFactory<Grade2,String>("groupStudent"));
        //value.setCellValueFactory(new PropertyValueFactory<Grade2,Number>("value"));
        value.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue()));
//        value.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Grade2, Number>, ObservableValue<Number>>() {
//            @Override
//            public ObservableValue<Number> call(TableColumn.CellDataFeatures<Grade2, Number> p) {
//                return new SimpleIntegerProperty(p.getValue().getValue());
//            }
//        });
        nameStd.setCellValueFactory(new PropertyValueFactory<Grade2,String>("nameStudent"));
        descriptionHmk.setCellValueFactory(new PropertyValueFactory<Grade2,String>("descriptionHomework"));
        tableView.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<Grade2>() {
                    @Override
                    public void changed(ObservableValue<? extends Grade2> observable,
                                        Grade2 oldValue, Grade2 newValue) {
                        showGradeDetails(newValue);
                    }
                });
    }
    @Override
    public void update(Observable o, Object arg) {
        model.setAll(service.iterableToList(service.getAllGrades()));
        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        AnchorPaneTable.getChildren().addAll(pagination);
    }


    public void setServices(Service services) {
        this.service = services;
        model= FXCollections.observableArrayList(this.service.iterableToList(service.getAllGrades()));
        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        AnchorPaneTable.getChildren().addAll(pagination);

        group = new ToggleGroup();
        btnToggleNormal.setToggleGroup(group);
        btnToggleSeries.setToggleGroup(group);
        btnToggleNormal.setSelected(true);
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
            service.addGrade(
                parseInt(idSField.getText()),
                parseInt(idHField.getText()),
                parseInt(valueField.getText()),
                    textArea.getText().replaceAll("[\r\n]+", " ")
            );
        } catch (NumberFormatException | ValidationException | RepositoryException err) {
            showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        } catch (ServiceException err) {
            if(err.getMessage().contains("OBS! new value < old value (it ramains unchanged)"))
                showMessage(Alert.AlertType.INFORMATION, "Information", err.getMessage());
            else
                showMessage(Alert.AlertType.ERROR, "Error", err.getMessage());
        }
    }



    @FXML
    public void clearAll() throws IOException {
        idSField.clear();
        idHField.clear();
        valueField.clear();
        textArea.clear();
    }


    @FXML
    public void filter() throws IOException {
        String idS=idSField.getText();
        String idH=idHField.getText();
        String value=valueField.getText();

        Predicate<Grade2> pred = x->true;
        Comparator<Grade2> cmpGradeValue=(x, y)->{
        if(x.getValue()==y.getValue())
            return 0;
        else if(x.getValue()<y.getValue())
            return 1;
        return -1;
    };

        //OBS!!!! enumerarea cu ; este fara spatiu
        if( !checkIdHomework.isSelected() && !checkIdStudent.isSelected() && !checkValue.isSelected()){
            pred=x->true;  //pred=x-> x.getValue()!=0;
        }else {
            try {
                if (checkIdStudent.isSelected()&& !idS.isEmpty()) {
                    String[] ids = idS.split("[;]");
                    for (String s : ids) {
                        try {
                            int n=Integer.parseInt(s);
                            pred = pred.and(x -> x.getId().getKey().getId() == n);
                        } catch (NumberFormatException ignored) {
                            pred = pred.and(x -> x.getId().getKey().getName().contains(s));
                        }
                    }
                }
                if (checkIdHomework.isSelected()&& !idH.isEmpty()) {
                    String[] ids = idH.split("[;]");
                    for (String s : ids)
                        try {
                            int n=Integer.parseInt(s);
                            pred = pred.and(x -> x.getId().getValue().getId()==n);
                        } catch (NumberFormatException ignored) {
                            pred = pred.and(x -> x.getId().getValue().getDescription().contains(s));
                        }
                }
                if (checkValue.isSelected()&& !value.isEmpty()) {
                    String[] vals = value.split("[;]");
                    for (String s : vals){
                        int n=parseInt(s);
                        pred = pred.and(x -> x.getValue() == n);
                    }
                }
            } catch (NumberFormatException e){
                pred=x->true;
                showMessage(Alert.AlertType.ERROR, "Error", "Insert integer values for grade's value");
            }
        }
        model= FXCollections.observableArrayList(service.filterAndSorter(service.iterableToList(service.getAllGrades()), pred, cmpGradeValue));
        Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
        pagination.setPageFactory(this::createPage);
        AnchorPaneTable.getChildren().addAll(pagination);
        //tableView.setItems(model);
    }

    public void gradeModeAdd() throws IOException{
        if(btnToggleNormal.isSelected()) {
            anchorPaneNormalMode.setVisible(true);
            anchorPaneSeriesMode.setVisible(false);
            model= FXCollections.observableArrayList(this.service.iterableToList(service.getAllGrades()));
            Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
            pagination.setPageFactory(this::createPage);
            AnchorPaneTable.getChildren().addAll(pagination);
            value.setEditable(false);
            //initialize();  //reinitializam tabelul
        }else{
            anchorPaneNormalMode.setVisible(false);
            anchorPaneSeriesMode.setVisible(true);
            comboGroupSeries.setValue("Default");
            comboHomeworkSeries.setValue("Default");
            HashSet<String> groupsList=new HashSet<String>();
            HashSet<String> descrList=new HashSet<String>();
            groupsList.add("Default");
            descrList.add("Default");
            for(Student st: service.getAllStudents()){
                groupsList.add(""+st.getGroup());
            }
            for(Homework hmk: service.getAllHomeworks()){
                descrList.add(""+hmk.getDescription());
            }
            comboGroupSeriesList= FXCollections.observableArrayList(groupsList);
            comboHmkSeriesList=FXCollections.observableArrayList(descrList);
            comboGroupSeries.setItems(comboGroupSeriesList);
            comboHomeworkSeries.setItems(comboHmkSeriesList);
            value.setEditable(true);
        }
    }

    public  void updateTableAddSeries(){
        if(comboHomeworkSeries.getValue()!=null && comboGroupSeries.getValue()!=null) {
            listSeriesAdd = new ArrayList<>() {
            };
            for (Student st : service.getAllStudents())
                if (comboGroupSeries.getValue().equals("Default") || parseInt(comboGroupSeries.getValue()) == st.getGroup())
                    for (Homework hmk : service.getAllHomeworks())
                        if (comboHomeworkSeries.getValue().equals("Default") || hmk.getDescription().contains(comboHomeworkSeries.getValue()))
                            try {
                                service.getGrade(new Pair<>(st, hmk));
                            } catch (ServiceException | RepositoryException e) {
                                listSeriesAdd.add(new Grade2(new Pair<Student, Homework>(st, hmk), -1));
                            }
            model = FXCollections.observableArrayList(listSeriesAdd);

            //paginated
            Pagination pagination = new Pagination((model.size() / rowsPerPage + 1), 0);
            pagination.setPageFactory(this::createPage);
            AnchorPaneTable.getChildren().addAll(pagination);
            tableView.setEditable(true);
            //value.setCellFactory(TextFieldTableCell.forTableColumn());
            value.setCellFactory(TextFieldTableCell.<Grade2, Number>forTableColumn(new NumberStringConverter()));
        }
    }

    public void changeValueCellEvent(CellEditEvent edittedCell)
    {
        Grade2 gradeSelected =  tableView.getSelectionModel().getSelectedItem();
        gradeSelected.setValue(parseInt(edittedCell.getNewValue().toString()));
    }

    public void addSeries(){
        for(Grade2 gr:listSeriesAdd){
            try{
                if(gr.getValue()>=0){
                    service.addGrade(gr.getId().getKey().getId(), gr.getId().getValue().getId(), gr.getValue(), "NULL");
                }
            }catch (ValidationException | ServiceException | RepositoryException ignored){

            }
        }
        updateTableAddSeries();
    }

    //------------------------------------------------------------------


    private static void showMessage(Alert.AlertType type, String header, String messageText){
        Alert alert=new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(messageText);
        alert.showAndWait();
    }

    private void showGradeDetails(Grade2 newValue) {
        if(newValue!=null){
            idSField.setText(""+newValue.getId().getKey().getId());
            idHField.setText(""+newValue.getId().getValue().getId());
            valueField.setText(""+newValue.getValue());
        }
    }
}
