package com.example.gymdesktop2023.controllers.main;

import animatefx.animation.SlideInRight;
import com.example.gymdesktop2023.dto.main.CustomerService;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.service.Users;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

    private ObservableList<Customers> customersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initTable();
            userButtons();
        });
    }


    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        try {
            this.customersList = CustomerService.fetchAllCustomer(activeUser);
            //System.out.println("Have " + customersList.hashCode());
        } catch (SQLException e) {
            errorMessage("Khald baa  dhacay" + e.getMessage());
        }
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }

    //-------------------Helpers------------------

    private void initTable() {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        fullName.setCellValueFactory(customers -> new SimpleStringProperty(
                customers.getValue().getFirstName() + "   " + customers.getValue().getMiddleName()
                        + "   " + customers.getValue().getLastName()));

        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        gander.setCellValueFactory(new PropertyValueFactory<>("gander"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));

        information.setCellValueFactory(new PropertyValueFactory<>("information"));
        update.setCellValueFactory(new PropertyValueFactory<>("update"));
        payments.setCellValueFactory(new PropertyValueFactory<>("paymentBtn"));
        tableView.setItems(customersList);
    }

    private void userButtons() {
        for (Customers customer : customersList) {

            EventHandler<MouseEvent> updateHandler = event -> {
                System.out.println("Update pressed");
            };


            customer.getUpdate().addEventFilter(MouseEvent.MOUSE_CLICKED, updateHandler);

            EventHandler<MouseEvent> paymentHandler = event -> {
                try {
                    openPayment(customer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };


            customer.getPaymentBtn().addEventFilter(MouseEvent.MOUSE_CLICKED, paymentHandler);

            EventHandler<MouseEvent> informationHandler = event -> {
                System.out.println("Update pressed");
            };


            customer.getInformation().addEventFilter(MouseEvent.MOUSE_CLICKED, informationHandler);
        }
    }

    private void openPayment(Customers customer) throws IOException {
        SlideInRight slideInRight = getSlideInRight();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymdesktop2023/views/main/payments.fxml"));
        AnchorPane anchorPane = loader.load();
        PaymentController controller = loader.getController();

        borderPane.setCenter(anchorPane);
        controller.setCustomer(customer);
        slideInRight.setNode(anchorPane);
        slideInRight.play();
    }

}
