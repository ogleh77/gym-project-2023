package com.example.gymdesktop2023.controllers.main;

import com.example.gymdesktop2023.dao.main.CustomerService;
import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController extends CommonClass implements Initializable {
    @FXML
    private TableColumn<Customers, Integer> customerId;

    @FXML
    private TableColumn<Customers, String> fullName;

    @FXML
    private TableColumn<Customers, String> gander;

    @FXML
    private TableColumn<Customers, JFXButton> information;

    @FXML
    private TableColumn<Customers, JFXButton> payments;

    @FXML
    private TableColumn<Customers, String> phone;

    @FXML
    private TableColumn<Customers, String> shift;

    @FXML
    private TableView<Customers> tableView;

    @FXML
    private TableColumn<Customers, JFXButton> update;
    @FXML
    private JFXButton paymentBtn;
    private ObservableList<Customers> customersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println();
        Platform.runLater(() -> {
            initTable();
            System.out.println("After in table initial Home \n");
            try {
                System.out.println(CustomerService.fetchAllCustomer(activeUser));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initTable() {
        System.out.println("called init method in home");
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        fullName.setCellValueFactory(customers -> new SimpleStringProperty(
                customers.getValue().getFirstName() + "   " + customers.getValue().getMiddleName()
                        + "   " + customers.getValue().getLastName()));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gander.setCellValueFactory(new PropertyValueFactory<>("gander"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        try {
            tableView.setItems(CustomerService.fetchAllCustomer(activeUser));
            System.out.println("Before in table init Home \n");
            System.out.println(CustomerService.fetchAllCustomer(activeUser));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void paymentHandler() throws IOException {

        if (tableView.getSelectionModel().getSelectedItem() != null) {

            FXMLLoader loader = openNormalWindow("/com/example/gymdesktop2023/views/desing/payments.fxml", borderPane);
            PaymentController controller = loader.getController();
            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
            controller.setBorderPane(borderPane);
        }

    }

    @FXML
    void fullInfoHandler() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = openNormalWindow("/com/example/gymdesktop2023/views/desing/customer-info.fxml", borderPane);
            CustomerInfoController controller = loader.getController();
            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void deleteHandler() throws IOException {
//        if (tableView.getSelectionModel().getSelectedItem() != null) {
//
//            FXMLLoader loader = openNormalWindow("/com/example/gymdesktop2023/views/desing/customer-info.fxml", borderPane);
//            CustomerInfoController controller = loader.getController();
//            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
//            // controller.setBorderPane(borderPane);
//        }
        tableView.refresh();
    }

    @FXML
    void updateHandler() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = openNormalWindow("/com/example/gymdesktop2023/views/desing/registrations.fxml", borderPane);
            RegistrationController controller = loader.getController();
            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
            controller.setActiveUser(activeUser);
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }
}
