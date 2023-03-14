package com.example.gymdesktop2023.controllers.main;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutLeft;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.service.Users;
import com.example.gymdesktop2023.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends CommonClass implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private HBox menuHBox;

    @FXML
    private StackPane notificationsHBox;

    @FXML
    private VBox sidePane;

    @FXML
    private HBox topProfile;

    @FXML
    private Label warningLabel;
    @FXML
    private HBox warningLabelParent;

    @FXML
    private Circle activeProfile;

    @FXML
    private Label activeUserName;
    private ObservableList<Customers> warningCustomers;

    // private boolean fullSize = false
    private boolean visible = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            borderPane.setLeft(null);

        });
    }

    @FXML
    void menuToggle(MouseEvent event) {
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
    void gymHandler(ActionEvent event) {

    }

    @FXML
    void homeClickHandler(ActionEvent event) {


    }

    @FXML
    void maximizeHandler(MouseEvent event) {

    }


    public void setWarningCustomers(ObservableList<Customers> warningCustomers) {
        this.warningCustomers = warningCustomers;
        if (warningCustomers.size() == 0) {
            warningLabelParent.setVisible(false);
        } else if (warningCustomers.size() > 9) {
            warningLabel.setText("9+");
        } else {
            warningLabel.setText(warningCustomers.size() + "");
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);

        activeUserName.setText(activeUser.getUsername()+" "+activeUser.getRole());
        URL url;
        final String[] profileImages = {
                "/com/example/gymdesktop2023/style/icons/man-profile.jpeg",
                "/com/example/gymdesktop2023/style/icons/woman-hijap.jpeg"
        };

        if (activeUser.getGender().equals("Male")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[0]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                activeProfile.setFill(new ImagePattern(new Image(activeUser.getImage())));
            }

        } else if (activeUser.getGender().equals("Female")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[1]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                activeProfile.setFill(new ImagePattern(new Image(activeUser.getImage())));
            }
        }

        activeProfile.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
    }
}
