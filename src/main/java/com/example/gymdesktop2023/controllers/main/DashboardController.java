package com.example.gymdesktop2023.controllers.main;

import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.helpers.CommonClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends CommonClass implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }

    @FXML
    void addUserHandler(ActionEvent event) {

    }

}
