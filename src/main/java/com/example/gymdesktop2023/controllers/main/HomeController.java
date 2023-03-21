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
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    private ObservableList<Customers> customersList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initTable);
    }

    private void initTable() {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        fullName.setCellValueFactory(customers -> new SimpleStringProperty(
                customers.getValue().getFirstName() + "   " + customers.getValue().getMiddleName()
                        + "   " + customers.getValue().getLastName()));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gander.setCellValueFactory(new PropertyValueFactory<>("gander"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        tableView.setItems(customersList);
    }

    private void openPayment(Customers customer) throws IOException {
        //  SlideInRight slideInRight = getSlideInRight();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymdesktop2023/views/info/customer-info.fxml"));
        Scene scene = new Scene(loader.load());
        CustomerInfoController controller = loader.getController();
        controller.setCustomer(customer);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        /// AnchorPane anchorPane = loader.load();

//        slideInRight.setNode(anchorPane);
//        slideInRight.play();
    }

    private void openUpdate(Customers customer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymdesktop2023/views/main/registrations.fxml"));
        Scene scene = new Scene(loader.load());
        RegistrationController controller = loader.getController();
        controller.setCustomer(customer);
        controller.setActiveUser(activeUser);
        System.out.println("The customers I sent " + customersList.hashCode());
        //  System.out.println(customer);
//        controller.setCustomers(customersList);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        try {
            this.customersList = CustomerService.fetchAllCustomer(activeUser);
            System.out.println("I home I have " + customersList);
        } catch (SQLException e) {
            errorMessage(e.getMessage());
        }
    }
}
