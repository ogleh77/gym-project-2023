package com.example.gymdesktop2023.controllers.main;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutLeft;
import com.example.gymdesktop2023.dao.main.CustomerService;
import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController extends CommonClass implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox menuHBox;
    @FXML
    private VBox sidePane;

    @FXML
    private StackPane warningHBox;
    private boolean visible = false;
    private ObservableList<Customers> customersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            borderPane.setLeft(null);
            try {
                this.customersList = CustomerService.fetchAllCustomer(activeUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void menuToggleHandler() {
        if (visible) {
            SlideOutLeft slideOutLeft = new SlideOutLeft();
            slideOutLeft.setNode(sidePane);
            slideOutLeft.play();
            slideOutLeft.setOnFinished(e -> {
                borderPane.setLeft(null);
            });
        } else {
            new SlideInLeft(sidePane).play();
            borderPane.setLeft(sidePane);
        }
        visible = !visible;
    }

    @FXML
    void homeHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymdesktop2023/views/desing/home.fxml", borderPane, sidePane, menuHBox, warningHBox);
        HomeController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
    }

    @FXML
    void registrationHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymdesktop2023/views/desing/registrations.fxml", borderPane, sidePane, menuHBox, warningHBox);
        RegistrationController controller = loader.getController();
        controller.setActiveUser(activeUser);
    }

    @FXML
    void outDatedHandler() throws IOException {
        openWindow("/com/example/gymdesktop2023/views/desing/outdated.fxml", borderPane, sidePane, menuHBox, warningHBox);
    }

    @FXML
    void reportsHandler() throws IOException {
        openWindow("/com/example/gymdesktop2023/views/desing/report.fxml", borderPane, sidePane, menuHBox, warningHBox);
    }


    @FXML
    void addUserHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/com/example/gymdesktop2023/views/users/user-creation.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    @FXML
    void updateUserHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymdesktop2023/views/users/user-choose.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }

    public void setCustomersList(ObservableList<Customers> customersList) {

    }
}
