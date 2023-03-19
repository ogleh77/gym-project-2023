package com.example.gymdesktop2023.controllers.service;

import com.example.gymdesktop2023.dao.UserService;
import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.entity.UsersBuilder;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserCreationController extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton admin;

    @FXML
    private JFXButton createBtn;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstname;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField lastname;

    @FXML
    private JFXRadioButton male;

    @FXML
    private PasswordField oldPassword;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private JFXRadioButton superAdmin;

    @FXML
    private Label topText;
    @FXML
    private Label phoneValidation;

    @FXML
    private TextField username;

    private boolean imageUploaded = false;

    private final ToggleGroup roleToggle = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();
            admin.setSelected(true);
        });
        phoneValidation();

        service.setOnSucceeded(e -> {
            createBtn.setGraphic(null);
            System.out.println(users());
        });
    }

    @FXML
    void createUserHandler() {
        System.out.println(users());

//        if (isValid(getMandatoryFields(), genderGroup)) {
//            if (!imageUploaded) {
//                checkImage();
//            }
//            if (start) {
//                service.restart();
//                createBtn.setGraphic(getLoadingImageView());
//                createBtn.setText("Creating");
//            } else {
//                service.start();
//                createBtn.setGraphic(getLoadingImageView());
//                createBtn.setText("Creating");
//                start = true;
//            }
//        }
    }

    @FXML
    void imageUploadHandler(ActionEvent event) {
        uploadImage();
    }

    private Users users() {
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";

        Users user = new UsersBuilder()
                .setFirstName(firstname.getText().trim())
                .setPhone(phone.getText().trim())
                .setUsername(username.getText().trim())
                .setPassword(oldPassword.getText().trim())
                .setImage(image)
                .setGender(gander)
                .setRole(role)
                .setShift(shift.getValue().trim())
                .build();


        return user;
    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Platform.runLater(() -> {
                            try {
                                UserService.insertUser(users());
                                Platform.runLater(() -> informationAlert("Wxad samaysay user cusub"));
                            } catch (SQLException e) {
                                Platform.runLater(() -> errorMessage(e.getMessage()));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> errorMessage(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };

    private void uploadImage() {
        try {
            if (selectedFile() != null) {
                Image image = new Image(new FileInputStream(selectedFile.getAbsolutePath()));
                imageView.setImage(image);
                imageView.setX(50);
                imageView.setY(25);
                imageUploaded = true;
            }
        } catch (FileNotFoundException e) {
            errorMessage("Fadlan sawirka lama helin isku day mar kale");
            imageUploaded = false;
        }
    }

    private void checkImage() {
        ButtonType ok = new ButtonType("Hada soo upload-garee", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Ogaan baan u dhaafay.", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Sawirku wuxuu kuu qurxinyaa profile kaga", ok, cancel);
        alert.setTitle("Sawir lama helin");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            uploadImage();
        } else {
            imageUploaded = true;
        }
    }

    private void initFields() {
        admin.setSelected(true);
        admin.setToggleGroup(roleToggle);
        superAdmin.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        shift.setItems(super.getShift());

        getMandatoryFields().addAll(firstname, lastname, phone, shift, username, oldPassword);
    }

    private void phoneValidation() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone.setText(newValue.replaceAll("[^\\d]", ""));
                phoneValidation.setText("Fadlan lanbarka xarfo looma ogola");
                phoneValidation.setVisible(true);
            } else if (!phone.getText().matches("^\\d{7}")) {
                phoneValidation.setText("Fadlan lanbarku kama yaran karo 7 digit");
                phoneValidation.setVisible(true);

            } else {
                phoneValidation.setVisible(false);
            }
        });

    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }
}
