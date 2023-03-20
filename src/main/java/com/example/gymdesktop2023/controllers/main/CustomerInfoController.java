package com.example.gymdesktop2023.controllers.main;


import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerInfoController extends CommonClass implements Initializable {
    @FXML
    private TableColumn<Payments, Double> amountPaid;

    @FXML
    private TableColumn<Payments, Double> discount;

    @FXML
    private TableColumn<Payments, LocalDate> expDate;

    @FXML
    private TableColumn<Payments, String> month;

    @FXML
    private TableColumn<Payments, String> paidBy;

    @FXML
    private TableColumn<Payments, String> paymentDate;

    @FXML
    private TableColumn<Payments, JFXButton> pendingBtn;
    @FXML
    private TableColumn<Payments, String> poxing;
    @FXML
    private TableColumn<Payments, String> running;
    @FXML
    private TableView<Payments> tableView;
    @FXML
    private TableColumn<Payments, String> vipBox;
    @FXML
    private TableColumn<Payments, String> year;
    @FXML
    private ImageView imgView;
    @FXML
    private Label fullName;
    @FXML
    private Label address;
    @FXML
    private Label phone;
    @FXML
    private Label shift;
    @FXML
    private Label weight;
    @FXML
    private Label gander;
    @FXML
    private Label whoAdded;
    @FXML
    private Label titile;

    private ObservableList<Payments> payments;
    //private String[] colors = {"-fx-background-color:green", "-fx-background-color:red"};

    public CustomerInfoController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initTable();

            for (Payments payment : customer.getPayments()) {

                EventHandler<MouseEvent> pending = event -> {
                    System.out.println("Pending is pressed...");
//                    if (payment.isPending()) {
//                        System.out.println("Ka fur pendingka..");
//                    } else {
//                        System.out.println("Pend garee...");
//                    }
                    //openPendingStage(payment);
                };

                payment.getPendingBtn().addEventFilter(MouseEvent.MOUSE_CLICKED, pending);
            }
        });
    }


    private void initTable() {
        amountPaid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        expDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));

        month.setCellValueFactory(new PropertyValueFactory<>("month"));

        paidBy.setCellValueFactory(new PropertyValueFactory<>("paidBy"));

        paymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        pendingBtn.setCellValueFactory(new PropertyValueFactory<>("pendingBtn"));

        poxing.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().isPoxing() ? "Yes" : "No"));

        running.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().isOnline() ? "Yes" : "No"));

        vipBox.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().getBox() != null ? "Yes" : "No"));

        year.setCellValueFactory(new PropertyValueFactory<>("year"));

        tableView.setItems(customer.getPayments());

    }

    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        fullName.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
        phone.setText(customer.getPhone());
        gander.setText(customer.getGander());
        address.setText(customer.getAddress() == null ? " no address " : customer.getAddress());
        shift.setText(customer.getShift());
        weight.setText(customer.getWeight() + "");
        whoAdded.setText(customer.getWhoAdded());

//        try {
//            // payments = FXCollections.observableArrayList(PaymentService.fetchAllCustomersPayments(customer.getPhone()));
//        } catch (SQLException e) {
//            errorMessage("error " + e.getMessage());
//        }
        payments = customer.getPayments();
        try {
            if (customer.getImage() != null) {
                imgView.setImage(new Image(new FileInputStream(customer.getImage())));
                // selectedFile = new File(customer.getImage());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


//    private void openPendingStage(Payments payment) {
//        try {
//            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/com/example/salmaan/views/pending-confirm.fxml"));
//
//            Scene scene = new Scene(loader.load());
//            PaymentPendingController controller = loader.getController();
//            controller.setPayment(payment);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


}


