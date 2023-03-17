package com.example.gymdesktop2023.controllers.service;

import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.helpers.CommonClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

public class CardController extends CommonClass implements Initializable {
    @FXML
    public Label shift;
    @FXML
    private ImageView customerPhoto;
    @FXML
    private Label fullName;
    @FXML
    private Label lastPaid;
    @FXML
    private Label outDated;
    @FXML
    private Label phone;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        LocalDate exp = customer.getPayments().get(0).getExpDate();
        LocalDate today = LocalDate.now();
        Period period = Period.between(exp, today);

        fullName.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
        lastPaid.setText(customer.getPayments().get(0).getPaymentDate());
        if (period.getDays() == 0) {
            outDated.setText("today is left");
        } else if (period.getDays() < 0) {
            outDated.setText(Math.abs(period.getDays()) + " days left");
        } else {
            outDated.setText(period.getDays() == 1 ? period.getDays() + " day ago" : period.getDays() + " days ago");
        }
        phone.setText(customer.getPhone());
        shift.setText(customer.getShift());
        try {
            if (customer.getImage() != null) {
                customerPhoto.setImage(new Image(new FileInputStream(customer.getImage())));
            }
        } catch (FileNotFoundException e) {
            errorMessage(e.getMessage());
        }
    }

    public void setI(int i) {
        phone.setText(i + "");
        System.out.println("I have "+i);
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }


}
