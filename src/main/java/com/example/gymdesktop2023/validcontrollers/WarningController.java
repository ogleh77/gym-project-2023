package com.example.gymdesktop2023.validcontrollers;

import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.helpers.CommonClass;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WarningController extends CommonClass implements Initializable {
    @FXML
    private VBox vbox;
    private ObservableList<Customers> outdatedCustomers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setOutdatedCustomers(ObservableList<Customers> outdatedCustomers) {
        this.outdatedCustomers = outdatedCustomers;
        if (!outdatedCustomers.isEmpty()) {
            FXMLLoader loader;
            AnchorPane anchorPane;
            for (Customers customer : outdatedCustomers) {
                loader = new FXMLLoader(getClass().getResource("/com/example/gymdesktop2023/views/service/customer-card.fxml"));
                try {
                    anchorPane = loader.load();
                    CardController controller = loader.getController();
                    controller.setCustomer(customer);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                vbox.getChildren().add(anchorPane);
            }
        } else {
            Label label = new Label("No Warning costumers was found");
            label.setStyle("-fx-font-size: 18");
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().add(label);
        }

    }
}
