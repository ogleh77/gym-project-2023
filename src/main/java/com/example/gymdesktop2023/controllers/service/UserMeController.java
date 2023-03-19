package com.example.gymdesktop2023.controllers.service;

import com.example.gymdesktop2023.dto.service.UserService;
import com.example.gymdesktop2023.entity.service.Users;
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

public class UserMeController extends CommonClass implements Initializable {
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
    private JFXButton uploadImageBtn;

    @FXML
    private TextField username;

    private final ToggleGroup roleToggle = new ToggleGroup();
    private Users updatingUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();

            if (!activeUser.getRole().equals("super_admin")) {
                chooseUser.setDisable(true);
//                female.setDisable(true);
//                male.setDisable(true);
//                superAdmin.setDisable(true);
//                admin.setSelected(true);
            } else {
                try {
                    chooseUser.setItems(UserService.users());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @FXML
    void choiceUserHandler(ActionEvent event) {
        updatingUser = chooseUser.getValue();
        if (!activeUser.getUsername().equals(chooseUser.getValue().getUsername())) {
            System.out.println(activeUser.getUsername() + " not same " + chooseUser.getValue().getUsername());
            firstname.setDisable(true);
            lastname.setDisable(true);
            phone.setDisable(true);
            username.setDisable(true);
            password.setDisable(true);
            uploadImageBtn.setVisible(false);
        } else if (activeUser.getUsername().equals(chooseUser.getValue().getUsername())) {
            System.out.println(activeUser.getUsername() + " are same " + chooseUser.getValue().getUsername());
            firstname.setDisable(false);
            lastname.setDisable(false);
            phone.setDisable(false);
            username.setDisable(false);
            password.setDisable(false);
            uploadImageBtn.setVisible(true);
        }
        setUser(updatingUser, true);
    }

    @FXML
    void imageUploadHandler(ActionEvent event) {
        uploadImage(imageView);
    }

    @FXML
    void updateHandler(ActionEvent event) {
        if (!imageUploaded) {
            checkImage(imageView, "Sawirku wuxuu kuu qurxinyaa profile kaga");
        }
        try {
            if (chooseUser.getValue() != null) {
                updateUser();
            } else {
                updateUser();
            }
            System.out.println(updatingUser);
            UserService.update(updatingUser);

            informationAlert("Waxaad Update garaysay user-ka " + updatingUser.getUsername());
        } catch (SQLException e) {
            errorMessage(e.getMessage());
        }

    }

    ///-----------------Helpers------------------------–

    private void initFields() {
        admin.setToggleGroup(roleToggle);
        superAdmin.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        shift.setItems(getShift());
        getMandatoryFields().addAll(firstname, lastname, phone, shift, username, password);
    }

    private Users updateUser() {
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";
        return updatingUser = new Users(Integer.parseInt(idFeild.getText()), firstname.getText().trim(), lastname.getText().trim(), phone.getText().trim(), gander,
                shift.getValue(), username.getText().trim(), password.getText().trim(), image, role);
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
                                if (chooseUser.getValue() != null) {
                                    updateUser();
                                } else {
                                    updateUser();
                                }
                                System.out.println(updatingUser);
                                UserService.update(updatingUser);

                                Platform.runLater(() -> informationAlert("Waxaad Update garaysay user-ka " + updatingUser.getUsername()));
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

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        setUser(activeUser, false);
        System.out.println("Before \n" + activeUser);
        updatingUser = activeUser;
    }

    private void setUser(Users activeUser, boolean isFromCombo) {
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
                            isFromCombo ? chooseUser.getValue().getImage() : activeUser.getImage())));
                    selectedFile = new File(activeUser.getImage());
                    imageUploaded = true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
