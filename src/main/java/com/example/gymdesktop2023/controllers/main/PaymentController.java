package com.example.gymdesktop2023.controllers.main;

import animatefx.animation.FadeIn;
import com.example.gymdesktop2023.dao.GymService;
import com.example.gymdesktop2023.dao.main.PaymentService;
import com.example.gymdesktop2023.entity.Box;
import com.example.gymdesktop2023.entity.Gym;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.main.PaymentBuilder;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PaymentController extends CommonClass implements Initializable {
    @FXML
    private TextField amountPaid;

    @FXML
    private ComboBox<Box> boxChooser;

    @FXML
    private JFXButton createBtn;

    @FXML
    private TextField discount;

    @FXML
    private Label discountValidation;

    @FXML
    private DatePicker expDate;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstName;

    @FXML
    private Label gymTitle;

    @FXML
    private ImageView imgView;

    @FXML
    private TextField lastName;

    @FXML
    private JFXRadioButton male;
    @FXML
    private TextField middleName;
    @FXML
    private ComboBox<String> paidBy;
    @FXML
    private Label paymentInfo;
    @FXML
    private TextField phone;
    @FXML
    private JFXCheckBox poxing;
    @FXML
    private Label infoMin;
    private final Gym currentGym;
    private final double fitnessCost;
    private final double poxingCost;
    private final double vipBoxCost;
    private double currentCost = 0;
    private boolean paymentIsGoing = false;
    private final ButtonType ok;
    private LocalDate expiringDate;

    private boolean done = false;

    public PaymentController() throws SQLException {
        this.currentGym = GymService.getGym();
        this.fitnessCost = currentGym.getFitnessCost();
        this.poxingCost = currentGym.getPoxingCost();
        this.vipBoxCost = currentGym.getBoxCost();
        this.ok = new ButtonType("Finish", ButtonBar.ButtonData.OK_DONE);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();
            paidBy.setItems(super.getPaidBy());
        });

        currentCost = fitnessCost;
        amountPaid.setText(String.valueOf(currentCost));
        paymentValidation();
        validateDiscount();

        service.setOnSucceeded(e -> {
            if (done) {
                System.out.println("Is done");
            } else {
                System.out.println("Not done");
            }
        });
    }

    @FXML
    void createPaymentHandler() {
        if (isValid(getMandatoryFields(), null) && validateDiscount() == null) {
            if (start) {
                service.restart();
                createBtn.setGraphic(getLoadingImageView());
                createBtn.setText("Creating");
            } else {
                service.start();
                createBtn.setGraphic(getLoadingImageView());
                createBtn.setText("Creating");
                start = true;
            }
        }
    }

    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        gymTitle.setText(currentGym.getGymName() + " eDahab: " + currentGym.geteDahab() + " Zaad: " + currentGym.getZaad());
        if (customer != null) {
            firstName.setText(customer.getFirstName());
            middleName.setText(customer.getFirstName());
            lastName.setText(customer.getFirstName());
            middleName.setText(customer.getMiddleName());
            lastName.setText(customer.getLastName());
            phone.setText(customer.getPhone());

            male.setSelected(customer.getGander().equals("Male"));

            female.setSelected(customer.getGander().equals("Female"));


            try {
                if (customer.getImage() != null) {
                    imgView.setImage(new Image(new FileInputStream(customer.getImage())));
                }
            } catch (FileNotFoundException e) {
                errorMessage("Khalad ba ka dhacay " + e.getMessage());
            }

            expDate.setValue(LocalDate.now().plusDays(30));

            if (!customer.getPayments().isEmpty()) {
                for (Payments payment : customer.getPayments()) {
                    if (payment.isOnline() && (payment.getExpDate().isAfter(LocalDate.now()))
                            || payment.getExpDate().equals(LocalDate.now())) {

                        amountPaid.setText(String.valueOf(payment.getAmountPaid()));
                        paidBy.setValue(payment.getPaidBy());
                        discount.setText(String.valueOf(payment.getDiscount()));
                        poxing.setSelected(payment.isPoxing());
                        expiringDate = payment.getExpDate();
                        paymentIsGoing = true;
                        System.out.println("Yes is out going..");
                        break;
                    }
                }
            }
        }

    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        double _discount = ((!discount.getText().isEmpty() || !discount.getText().isBlank())) ? Double.parseDouble(discount.getText()) : 0;
                        currentCost -= _discount;

                        Payments payment = new PaymentBuilder()
                                .setAmountPaid(currentCost)
                                .setExpDate(expDate.getValue())
                                .setPaidBy(paidBy.getValue())
                                .setPoxing(poxing.isSelected())
                                .setCustomerFK(customer.getPhone())
                                .setDiscount(_discount)
                                .setOnline(true)
                                .setPending(false)
                                .build();

                        if (boxChooser.getValue() != null && !boxChooser.getValue().getBoxName().matches("remove box")) {
                            payment.setBox(boxChooser.getValue());
                            boxChooser.getItems().remove(boxChooser.getValue());
                        }
                        PaymentService.insertPayment(customer);
                        customer.getPayments().add(0, payment);
                        Thread.sleep(100);
                        Platform.runLater(() -> informationAlert("Waxaad samayasay payment cusub.."));
                        done = true;
                    } catch (SQLException e) {
                        Platform.runLater(() -> {
                            errorMessage(e.getMessage());
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
            };
        }
    };

    private void paymentValidation() {
        boxChooser.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Stop the user to name a box into remove or something insha Allah
            if ((oldValue == null || oldValue.getBoxName().matches("re.*")) && !newValue.getBoxName().matches("re.*")) {
                currentCost += vipBoxCost;
            } else if (oldValue != null && boxChooser.getValue().getBoxName().matches("re.*")) {
                currentCost -= vipBoxCost;
            }
            amountPaid.setText(String.valueOf(currentCost));
        });

        poxing.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (poxing.isSelected()) {
                currentCost += poxingCost;
            } else {
                currentCost -= poxingCost;
            }
            amountPaid.setText(String.valueOf((currentCost)));

        });


    }

    private void initFields() {
        expDate.setValue(LocalDate.now().plusDays(30));

        paidBy.setItems(super.getPaidBy());
        currentCost = fitnessCost;
        amountPaid.setText(String.valueOf(currentCost));
        amountPaid.setEditable(false);
        getMandatoryFields().addAll(amountPaid, paidBy);

        if (boxChooser.getItems().isEmpty()) {
            for (Box box : currentGym.getVipBoxes()) {
                if (box.isReady()) boxChooser.getItems().add(box);
            }

        }

        boxChooser.getItems().add(new Box(0, "remove box", false));
        expDate.setValue(LocalDate.now().plusDays(30));

        if (paymentIsGoing) {
            paidBy.setEditable(false);
            discount.setEditable(false);
            createBtn.setDisable(true);
            tellInfo(expiringDate);
        }
    }

    private void tellInfo(LocalDate expDate) {
        paymentInfo.setText(customer.getFirstName() + " Wakhtigu kama dhicin");
        infoMin.setText("wuxuse ka dhaacyaa" + expDate.toString() + " Insha Allah");
        paymentInfo.setStyle("-fx-text-fill: red;");
        FadeIn fadeIn = new FadeIn(paymentInfo);
        fadeIn.setCycleCount(50);
        fadeIn.setDelay(Duration.millis(100));
        fadeIn.play();

    }


    private String validateDiscount() {

        if ((!discount.getText().isEmpty() || !discount.getText().isBlank())) {
            if (!discount.getText().matches("[0-9]*")) {
                discountValidation.setVisible(true);
                discountValidation.setText("discount must be digits only ");
                return "error";
            } else {

                double _discount = Double.parseDouble(discount.getText());

                if (_discount > currentGym.getMaxDiscount()) {
                    discountValidation.setVisible(true);
                    discountValidation.setText("discount can't greater then max discount of $" + currentGym.getMaxDiscount());
                    return "error";
                } else {
                    discountValidation.setVisible(false);
                    discountValidation.setText(null);
                    return null;
                }
            }
        }

        return null;
    }
}
