package com.project.javafx.controllerfx;

import com.jfoenix.controls.JFXButton;
import com.project.javafx.model.AnnualStudent;
import com.project.javafx.model.CreditStudent;
import com.project.javafx.model.Student;
import com.project.javafx.repository.StudentRepository;
import com.project.javafx.ulti.AlertMaker;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class HomeController implements Initializable{

    @FXML
    private ImageView gear1;

    @FXML
    private ImageView gear2;

    @FXML
    private ImageView gear3;

    @FXML
    private JFXButton btnGraduate;

    private void rotategears()
    {
        RotateTransition rg1=new RotateTransition(Duration.seconds(5),gear1);
        rg1.setFromAngle(0);
        rg1.setToAngle(360);
        RotateTransition rg2=new RotateTransition(Duration.seconds(5),gear2);
        rg2.setFromAngle(360);
        rg2.setToAngle(0);
        RotateTransition rg3=new RotateTransition(Duration.seconds(5),gear3);
        rg3.setFromAngle(0);
        rg3.setToAngle(360);
        ParallelTransition pt=new ParallelTransition(rg1,rg2,rg3);
        pt.setCycleCount(ParallelTransition.INDEFINITE);
        pt.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rotategears();
    }

    public void updateStudy(ActionEvent event) {
        boolean confirmation = AlertMaker.getConfirmation("Closing year of study", "Are you sure to update this Year of Study");
        if (confirmation) {
            Set<Student> all = StudentRepository.getInstance().findAll();
            for (Student student : all) {
                if (student instanceof CreditStudent) {
                    ((CreditStudent) student).updatePassedCourseAll();
                } else if (student instanceof AnnualStudent) {
                    ((AnnualStudent) student).updateStudyYear();
                }
                StudentRepository.getInstance().update(student);
            }
        }
    }
}
