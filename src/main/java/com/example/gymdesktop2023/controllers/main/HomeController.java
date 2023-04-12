package com.example.gymdesktop2023.controllers.main;

import com.example.gymdesktop2023.dao.main.CustomerService;
import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.helpers.CommonClass;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableColumn<Customers, String> phone;

    @FXML
    private TableColumn<Customers, String> shift;
    @FXML
    private TableView<Customers> tableView;
    @FXML
    private TextField search;
    private ObservableList<Customers> customersList;
    private FilteredList<Customers> filteredList;
    private SortedList<Customers> sortedList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println();
        Platform.runLater(() -> {
            initTable();
            filteredList = new FilteredList<>(customersList, b -> true);
            sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedList);
            search.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                if (customer.getFirstName().contains(newValue.toLowerCase()) || customer.getFirstName().contains(newValue.toUpperCase())) {
                    return true;
                } else if (customer.getPhone().contains(newValue)) {
                    return true;
                } else if (customer.getLastName().contains(newValue.toLowerCase()) || customer.getLastName().contains(newValue.toUpperCase())) {
                    return true;
                } else
                    return customer.getMiddleName().contains(newValue.toLowerCase()) || customer.getMiddleName().contains(newValue.toUpperCase());
            }));
        });
    }

    private void initTable() {
        System.out.println("called init method in home");
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        fullName.setCellValueFactory(customers -> new SimpleStringProperty(customers.getValue().getFirstName() + "   " + customers.getValue().getMiddleName() + "   " + customers.getValue().getLastName()));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gander.setCellValueFactory(new PropertyValueFactory<>("gander"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        tableView.setItems(customersList);
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
    void deleteHandler() {
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
        try {
            customersList = CustomerService.fetchAllCustomer(activeUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }
}
