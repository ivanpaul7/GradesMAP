package gui;

import gui.grade.ControllerGrade;
import gui.homework.ControllerHomework;
import gui.statis.ControllerHallOfFame;
import gui.statis.ControllerStatistics;
import gui.student.ControllerStudent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import lab.service.Service;

import java.util.Random;

import static java.lang.Integer.parseInt;

public class Controller implements Initializable{
    protected Pane rootPane;
    @FXML BorderPane mainBorderPane;
    @FXML MenuItem menuItemStudent, menuItemHomework, menuItemGrade;
    @FXML MenuBar menuBar;
    @FXML Menu menuMenu, statisticsMenu, helpMenu;
    @FXML Button reloadBtn, buttonGrade2ndWin,buttonHomework2ndWin, buttonStudent2ndWin;
    @FXML TextField capchaField, userField, passwordField;
    @FXML CheckBox checkBoxRobot;
    @FXML Label labelCaptcha;
    @FXML AnchorPane anchorPane1, anchorPane2;
    @FXML Label labelErrUser, labelErrPassw, labelErrRobot, labelErrCaptcha;

    private AnchorPane studentAnchorPane,mainAnchorPane,gradeAnchorPane,homeworkAnchorPane, statisticsAnchorPane, hofAnchoPane, aboutAnchorPane, howToAnchorPane; //, secondWinAnchorPane;
    private Service service;
    private ControllerStudent ctrStudent;
    private ControllerStatistics ctrStatist;
    private ControllerHomework ctrHomework;
    private ControllerGrade ctrGrade;
    private ControllerHallOfFame ctrHallOfFame;
    //protected ControllerSecondWin ctrSecondWin;
    protected int sumCaptcha;


    public Controller(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private void incarcareFisiereFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student/student3.fxml"));
            studentAnchorPane = (AnchorPane) loader.load();
            ctrStudent = loader.getController();
            ctrStudent.setServices(service);
            service.addObserver(ctrStudent);

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("homework/homework2.fxml"));
            homeworkAnchorPane=(AnchorPane)  loader2.load();
            ctrHomework=loader2.getController();
            ctrHomework.setServices(service);
            service.addObserver(ctrHomework);

            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("grade/grade5.fxml"));
            gradeAnchorPane=(AnchorPane)  loader3.load();
            ctrGrade=loader3.getController();
            ctrGrade.setServices(service);
            service.addObserver(ctrGrade);

            //statistics
            FXMLLoader loader10 = new FXMLLoader(getClass().getResource("statis/statistics.fxml"));
            statisticsAnchorPane=(AnchorPane)  loader10.load();
            ctrStatist=loader10.getController();
            ctrStatist.setServices(service);
            service.addObserver(ctrStatist);

            //hall of fame
            FXMLLoader loader11 = new FXMLLoader(getClass().getResource("statis/hallOfFame.fxml"));
            hofAnchoPane=(AnchorPane)  loader11.load();
            ctrHallOfFame=loader11.getController();
            ctrHallOfFame.setServices(service);
            service.addObserver(ctrHallOfFame);


            //howto
            FXMLLoader loader20 = new FXMLLoader(getClass().getResource("help/howto.fxml"));
            howToAnchorPane=(AnchorPane)  loader20.load();

            //about
            FXMLLoader loader21 = new FXMLLoader(getClass().getResource("help/about.fxml"));
            aboutAnchorPane=(AnchorPane)  loader21.load();

        }catch (IOException e){
            //showErrorMessage("Eroare la incarcare :D");
        }
    }

    public void setServices(Service serv){

        menuMenu.setDisable(true);
        statisticsMenu.setDisable(true);
        //helpMenu.setDisable(true);


//        menuMenu.setDisable(false);
//        statisticsMenu.setDisable(false);
        //todo

        this.service=serv;
        incarcareFisiereFXML();
        initCaptcha();
        ctrStudent.enableBtnAdmin(false);
        ctrHomework.enableBtnAdmin(false);
    }
    @FXML
    public void reloadBtn(ActionEvent event) throws IOException{
        initCaptcha();
    }

    @FXML
    public void loginAction(ActionEvent event) throws IOException{
        String error="";
        int rangUser=service.checkValidUser(userField.getText(),passwordField.getText());

        if(!checkBoxRobot.isSelected()){
            error=error+"CheckBox \"I'm not a robot\" not checked\n";
            labelErrRobot.setText("unchecked \"I'm not a robot\"");
            labelErrRobot.setVisible(true);
        }else{
            labelErrRobot.setVisible(false);
        }
        if(userField.getText().equals("")){
            error += "User fiels is empty\n";
            labelErrUser.setText("empty field");
            labelErrUser.setVisible(true);
        }else
            labelErrUser.setVisible(false);

        if(passwordField.getText().equals("")){
            error += "Password fields is empty\n";
            labelErrPassw.setText("empty field");
            labelErrPassw.setVisible(true);
        }else
            labelErrPassw.setVisible(false);

        if(!passwordField.getText().equals("") && !userField.getText().equals("")){
            if(rangUser==0 ) {
                error = error + "User and password don't match\n";
                labelErrUser.setText("wrong user");
                labelErrPassw.setText("wrong password");
                labelErrPassw.setVisible(true);
                labelErrUser.setVisible(true);
            }else{
                labelErrUser.setVisible(false);
                labelErrPassw.setVisible(false);
            }
        }


        if(capchaField.getText().equals("")){
            error=error+"Captca field is empty\n";
            labelErrCaptcha.setText("empty field");
            labelErrCaptcha.setVisible(true);
        }
        else{
            try {
                if (parseInt(capchaField.getText()) != sumCaptcha){
                    error=error+"Wrong captcha sum\n";
                    labelErrCaptcha.setText("wrong captcha sum");
                    labelErrCaptcha.setVisible(true);
                }else{
                    labelErrCaptcha.setVisible(false);
                }
            }catch (NumberFormatException err){
                error=error+"Wrong captcha! It must be an integer\n";
                labelErrCaptcha.setText("wrong captcha sum");
                labelErrCaptcha.setVisible(true);
            }
        }

        //todo profesorul sa nu poata adauga(doar temele si notele)
        if(error=="") {
            if(rangUser==3 || rangUser==2){
                anchorPane1.setVisible(false);
                anchorPane2.setVisible(true);
                menuMenu.setDisable(false);
                statisticsMenu.setDisable(false);
                helpMenu.setDisable(false);
                if(rangUser==3){
                    ctrStudent.enableBtnAdmin(true);
                    ctrHomework.enableBtnAdmin(true);
                }else{
                    ctrStudent.enableBtnAdmin(false);
                    ctrHomework.enableBtnAdmin(true);
                }
            }else if(rangUser==1){
                anchorPane1.setVisible(false);
                anchorPane2.setVisible(true);
                menuMenu.setDisable(false);
                statisticsMenu.setDisable(false);
                helpMenu.setDisable(false);

                menuItemGrade.setDisable(true);
                menuItemStudent.setDisable(true);
                buttonGrade2ndWin.setDisable(true);
                buttonStudent2ndWin.setDisable(true);
            }
        }else{
            showMessage(Alert.AlertType.ERROR, null, error);
        }

//        if(service.checkValidUser(userField.getText(),passwordField.getText())==1){
//            if(checkBoxRobot.isSelected()){
//                try {
//                    if (parseInt(capchaField.getText()) == sumCaptcha) {
//                        anchorPane1.setVisible(false);
//                        anchorPane2.setVisible(true);
//                        menuMenu.setDisable(false);
//                        statisticsMenu.setDisable(false);
//                        //openStudent(event);
//                    }
//                }catch (NumberFormatException err){
//                    showMessage(Alert.AlertType.ERROR, "Error", "Wrong captcha! ( "+err.getMessage()+ " ).It must be an integer\n");
//                }
//            } else{
//                showMessage(Alert.AlertType.ERROR, "Error", "CheckBox "+"~ I'm not a robot ~"+"  should be checked!!!");
//            }
//        }else{
//            showMessage(Alert.AlertType.ERROR, "Wrong User", "user an password don't match");
//        }
    }
    private void initCaptcha(){
        Random rand = new Random();
        int  n1 = rand.nextInt(50) + 1;
        Random rand2 = new Random();
        int  n2 = rand2.nextInt(50) + 1;
        sumCaptcha=n1+n2;
        labelCaptcha.setText("Captcha: "+n1+ " + "+n2);
    }

    @FXML
    protected void openStudent(ActionEvent event) throws IOException{
        mainBorderPane.setCenter(studentAnchorPane);
    }
    @FXML
    protected void openHomework(ActionEvent event) throws IOException{
        mainBorderPane.setCenter(homeworkAnchorPane);
    }
    @FXML
    protected void openGrades(ActionEvent event) throws IOException{
        mainBorderPane.setCenter(gradeAnchorPane);
    }
    @FXML
    protected void openStatistics(ActionEvent event) throws IOException{
        mainBorderPane.setCenter(statisticsAnchorPane);
        ctrStatist.setBarCharStudDelivery();
    }
    @FXML
    protected void openHallOfFame(ActionEvent event) throws IOException{
        mainBorderPane.setCenter(hofAnchoPane);
    }

    @FXML
    protected void openHowTo(ActionEvent event) throws IOException{
        mainBorderPane.setCenter(howToAnchorPane);
    }
    @FXML
    protected void openAbout(ActionEvent event) throws IOException{
        mainBorderPane.setCenter(aboutAnchorPane);
    }

    @FXML
    protected void closeApp(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }


    static void showMessage(Alert.AlertType type, String header, String messageText){
        Alert alert=new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(messageText);
        alert.showAndWait();
    }
}
