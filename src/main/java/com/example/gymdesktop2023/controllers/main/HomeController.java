package com.example.gymdesktop2023.controllers.main;

import com.example.gymdesktop2023.dao.main.CustomerService;
import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    private ObservableList<Customers> customers;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initTable();
            userButtons();
        });
    }

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
        tableView.setItems(customers);
    }

    private void userButtons() {
        for (Customers customer : customers) {
            System.out.println("Fetched");
            EventHandler<MouseEvent> updateHandler = event -> {
                System.out.println("Update pressed");
            };
            customer.getUpdate().addEventFilter(MouseEvent.MOUSE_CLICKED, updateHandler);

            EventHandler<MouseEvent> paymentHandler = event -> {

                System.out.println("Payment is pressed..");
                // openPayment(customer);

            };


            customer.getPaymentBtn().addEventFilter(MouseEvent.MOUSE_CLICKED, paymentHandler);

            EventHandler<MouseEvent> informationHandler = event -> {
                System.out.println("information is pressed");
                try {
                    openPayment(customer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            customer.getInformation().addEventFilter(MouseEvent.MOUSE_CLICKED, informationHandler);
        }
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


    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        try {
            this.customers = CustomerService.fetchAllCustomer(activeUser);
            System.out.println(customers);
        } catch (SQLException e) {
            errorMessage(e.getMessage());
        }
    }
}
