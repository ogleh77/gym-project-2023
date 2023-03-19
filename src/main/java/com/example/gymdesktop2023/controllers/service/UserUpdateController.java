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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserUpdateController extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton admin;

    @FXML
    private ComboBox<Users> chooseUser;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstname;

    @FXML
    private TextField idFeild;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField lastname;

    @FXML
    private JFXRadioButton male;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private JFXRadioButton superAdmin;

    @FXML
    private Label topText;

    @FXML
    private JFXButton updateBtn;

    @FXML
    private TextField username;

    private Users user;

    private final ToggleGroup roleToggle = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();
            try {
                chooseUser.setItems(UserService.users());
                chooseUser.getItems().remove(activeUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
        service.setOnSucceeded(e -> {
            updateBtn.setGraphic(null);
        });
    }


    @FXML
    void choiceUserHandler() {
        setUser(chooseUser.getValue());
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";
        user = new UsersBuilder().setUserId(chooseUser.getValue() == null ? activeUser.getUserId() : chooseUser.getValue().getUserId())
                .setFirstName(firstname.getText().trim())
                .setPhone(phone.getText().trim())
                .setLastName(lastname.getText().trim())
                .setUsername(username.getText().trim())
                .setPassword(password.getText().trim())
                .setImage(image)
                .setGender(gander)
                .setRole(role)
                .setShift(shift.getValue().trim())
                .build();
    }


    @FXML
    void updateHandler(ActionEvent event) {
        System.out.println(user);
        if (isValid(getMandatoryFields(), null)) {
            if (start) {
                service.restart();
                updateBtn.setGraphic(getLoadingImageView());
                updateBtn.setText("Updating");
            } else {
                service.start();
                updateBtn.setGraphic(getLoadingImageView());
                updateBtn.setText("Updating");
                start = true;
            }
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        // setUser(activeUser);
    }

    private Users users() {
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";


        return new UsersBuilder()
                .setUserId(chooseUser.getValue() == null ? activeUser.getUserId() : chooseUser.getValue().getUserId())
                .setFirstName(firstname.getText().trim())
                .setPhone(phone.getText().trim())
                .setLastName(lastname.getText().trim())
                .setUsername(username.getText().trim())
                .setPassword(password.getText().trim())
                .setImage(image)
                .setGender(gander)
                .setRole(role)
                .setShift(shift.getValue().trim())
                .build();
    }

    private void setUser(Users activeUser) {
        idFeild.setText(String.valueOf(activeUser.getUserId()));
        firstname.setText(activeUser.getFirstName());
        lastname.setText(activeUser.getLastName());
        phone.setText(activeUser.getPhone());
        shift.setValue(activeUser.getShift());
        username.setText(activeUser.getUsername());
        password.setText(activeUser.getPassword());
        if (activeUser.getGender().equals("Male")) {
            male.setSelected(true);
        } else if (activeUser.getGender().equals("Female")) {
            female.setSelected(true);
        }

        if (activeUser.getRole().equals("super_admin")) {
            superAdmin.setSelected(true);
        } else if (activeUser.getRole().equals("admin")) {
            admin.setSelected(true);
        }


        if (activeUser.getImage() != null) {
            try {
                if (activeUser.getImage() != null) {
                    imageView.setImage(new Image(new FileInputStream(
                            activeUser.getImage())));
                    selectedFile = new File(activeUser.getImage());
                    imageUploaded = true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initFields() {
        admin.setToggleGroup(roleToggle);
        superAdmin.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        getMandatoryFields().add(chooseUser);
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
                                UserService.update(users());
                                Platform.runLater(() -> informationAlert("You are successfully updated"));
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


}
