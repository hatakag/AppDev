package com.project.javafx.controllerfx;


import com.jfoenix.controls.JFXButton;
import com.project.javafx.MainApp;
import com.project.javafx.controllerfx.course.CoursesController;
import com.project.javafx.controllerfx.register.AddRegisterController;
import com.project.javafx.controllerfx.student.StudentsController;
import com.project.javafx.ulti.AlertMaker;
import com.project.javafx.ulti.ViewConstants;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {

    private static MainApp mainApp;
    @FXML
    private Label lblUser;
    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnStudent;
    @FXML
    private JFXButton btnCourse;
    @FXML
    private JFXButton btnAnnual;
    @FXML
    private JFXButton btnCredit;
    @FXML
    private JFXButton btnRegister;
    @FXML
    private JFXButton btnGrade;
    @FXML
    private JFXButton btnLogOut;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btn_minimize;
    @FXML
    private JFXButton btn_maximize;
    @FXML
    private JFXButton btn_close;
    @FXML
    private JFXButton btn_option;
    @FXML
    private StackPane holderPane;
    private Parent homePane, studentPane, coursePane, registerPane, gradePane, majorPane, annualPane, creditClassPane;
    private double lastX = 0.0d;
    private double lastY = 0.0d;
    //    private void openMenus() {
//        JFXPopup popup = new JFXPopup(overflowContainer);
//        lblMenu.setOnMouseClicked(event -> {popup.show(lblMenu, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -10, 50);});
//    }
    private double lastWidth = 0.0d;
    private double lastHeight = 0.0d;
    private double xOffset = 0.0d;
    private double yOffset = 0.0d;
    @FXML
    private BorderPane topBar;

    public static void setMainApp(MainApp mainApp) {
        UserDashboardController.mainApp = mainApp;
    }

    HomeController homeController;
    StudentsController studentsController;
    CoursesController coursesController;
    AddRegisterController registerController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        openMenus();
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource(ViewConstants.HOME_VIEW));
            homePane = loader.load();
            homeController = loader.getController();

            loader = new FXMLLoader(getClass().getResource(ViewConstants.STUDENTS_VIEW));
            studentPane = loader.load();
            studentsController = loader.getController();
            studentsController.hiddenFucntionForStudent();

            loader = new FXMLLoader(getClass().getResource(ViewConstants.COURSES_VIEW));
            coursePane = loader.load();
            coursesController = loader.getController();
            coursesController.hiddenFucntionForStudent();

            loader = new FXMLLoader(getClass().getResource(ViewConstants.ADD_REGISTER_VIEW));
            registerPane = loader.load();
            registerController = loader.getController();

        } catch (IOException e) {
//            AlertMaker.showErrorMessage(e);
            e.printStackTrace();
        }
        //set up default node on page load
        setNode(homePane);
        initMovable((Stage) resources.getObject("stage"));
        lblUser.setText((String) resources.getObject("user"));

    }

    @FXML
    // init movable
    private void initMovable(Stage stage) {
        // adding movable borderless window
        //grab your root here
        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        //move around here
        topBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Set selected node to a content holder
     */
    private void setNode(Node node) {
        holderPane.getChildren().clear();
        if (node != null) {
            holderPane.getChildren().add(node);
        }

        FadeTransition ft = new FadeTransition(Duration.millis(700));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    void openCourse(ActionEvent event) {
        setNode(coursePane);
        coursesController.refreshTable();
    }

    @FXML
    void openHome(ActionEvent event) {
        setNode(homePane);
    }

    @FXML
    void openRegister(ActionEvent event) {
        setNode(registerPane);
    }

    @FXML
    void openStudent(ActionEvent event) {
        setNode(studentPane);
        studentsController.refreshTable(event);
    }

    public void logOff(ActionEvent event) throws IOException {
        boolean logoffCf = AlertMaker.getConfirmation("Logout Confirmation", "Are you want to logout?");
        if (logoffCf) {
            Stage currentStage = (Stage) btnLogOut.getScene().getWindow();
            currentStage.close();
            mainApp.showLoginStage();
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        boolean exitCf = AlertMaker.getConfirmation("Exit Confirmation", "Are you sure want to exit?");
        if (exitCf) {
            Platform.exit();
        }
    }

    @FXML
    public void minimize(MouseEvent event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void maximize(MouseEvent evt) {

        Node n = (Node) evt.getSource();

        Window w = n.getScene().getWindow();

        double currentX = w.getX();
        double currentY = w.getY();
        double currentWidth = w.getWidth();
        double currentHeight = w.getHeight();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        if (currentX != bounds.getMinX() &&
                currentY != bounds.getMinY() &&
                currentWidth != bounds.getWidth() &&
                currentHeight != bounds.getHeight()) {

            w.setX(bounds.getMinX());
            w.setY(bounds.getMinY());
            w.setWidth(bounds.getWidth());
            w.setHeight(bounds.getHeight());

            lastX = currentX; // save old dimensions
            lastY = currentY;
            lastWidth = currentWidth;
            lastHeight = currentHeight;

        } else {

            // de-maximize the window (not same as minimize)

            w.setX(lastX);
            w.setY(lastY);
            w.setWidth(lastWidth);
            w.setHeight(lastHeight);
        }

        evt.consume(); // don't bubble up to title bar
    }

    private void loadWindow(String loc, String title, ResourceBundle bundle) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc), bundle);
            Stage stage = new Stage(StageStyle.DECORATED); // Default Style
            stage.setTitle(title);
            stage.getIcons().add(new Image(ViewConstants.APP_ICON));
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.initOwner(btnRegister.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
