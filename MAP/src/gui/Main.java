package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import lab.domain.Grade2;
import lab.domain.Homework;
import lab.domain.Password;
import lab.domain.Student;
import lab.repository.FILE.PasswordFileRepository;
import lab.repository.GenericRepository;
import lab.repository.XML.GradeXMLRepository;
import lab.repository.XML.HomeworkXMLRepository;
import lab.repository.XML.StudentXMLRepository;
import lab.repository.statistics.StatisticsRepository;
import lab.service.Service;
import lab.validator.GradeValidator;
import lab.validator.HomeworkValidator;
import lab.validator.PasswordValidator;
import lab.validator.StudentValidator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Objects;

public class Main extends Application {
    private StudentValidator sVal = new StudentValidator();
    //private GenericRepository<Integer, Student> studRepo= new StudentFileRepository("src\\lab\\repository\\FILE\\students.txt",sVal);
    private GenericRepository<Integer, Student> studRepo=new StudentXMLRepository("MAP\\src\\lab\\repository\\XML\\student.xml",sVal);

    private HomeworkValidator hVal = new HomeworkValidator();
    //private GenericRepository<Integer, Homework> hmwkRepo = new HomeworkFileRepository("src\\lab\\repository\\FILE\\homeworks.txt",hVal);
    private GenericRepository<Integer, Homework> hmwkRepo = new HomeworkXMLRepository("MAP\\src\\lab\\repository\\XML\\homeworks.xml",hVal);

    private GradeValidator gVal= new GradeValidator();
    //private GenericRepository<Integer, Grade> grRepo=new GradeFileRepository("src\\lab\\repository\\FILE\\catalog.txt",gVal);
    //private GenericRepository<Pair<Student,Homework>, Grade2> grRepo2=new GradeFileRepositoryPointerSH("src\\lab\\repository\\FILE\\catalog2.txt",gVal, studRepo, hmwkRepo);
    private GenericRepository<Pair<Student,Homework>, Grade2> grRepo2 = new GradeXMLRepository("MAP\\src\\lab\\repository\\XML\\grades.xml",gVal, studRepo, hmwkRepo);

    private PasswordValidator pVal= new PasswordValidator();
    private GenericRepository<String, Password> psswRepo=new PasswordFileRepository("MAP\\src\\lab\\repository\\FILE\\passwords.txt",pVal);


    private StatisticsRepository statisticsRepository;

    private Service service = new Service(studRepo, hmwkRepo, grRepo2, psswRepo, statisticsRepository);

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWin2.fxml"));
        BorderPane root= loader.load();
        Controller controller=loader.getController();
        controller.setServices(service);

        root.getStyleClass().add("root");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("utils/icon.png")));

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
