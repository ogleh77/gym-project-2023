package com.example.gymdesktop2023.controllers.main;

import animatefx.animation.FadeIn;
import com.example.gymdesktop2023.dao.GymService;
import com.example.gymdesktop2023.entity.Box;
import com.example.gymdesktop2023.entity.Gym;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Label infoMin;

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
    private final double fitnessCost;
    private final double poxingCost;
    private final double vipBoxCost;
    private double currentCost = 0;
    private boolean paymentIsGoing = false;
    private final Gym currentGym;
    private ObservableList<Payments> customerPayment;
    private LocalDate expiringDate;

    public PaymentController() throws SQLException {
        this.currentGym = GymService.getGym();
        this.fitnessCost = currentGym.getFitnessCost();
        this.poxingCost = currentGym.getPoxingCost();
        this.vipBoxCost = currentGym.getBoxCost();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initFields);
        currentCost = fitnessCost;
        amountPaid.setText(String.valueOf(currentCost));
        paymentValidation();
        validateDiscount();
    }

    @FXML
    void createPaymentHandler() {

    }

    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        this.gymTitle.setText(currentGym.getGymName() + " eDahab: " + currentGym.geteDahab() + " Zaad: " + currentGym.getZaad());

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
                    checkPayment(payment);
                }
            }
        }

    }

    private void checkPayment(Payments payment) {
        if (payment.isOnline() && (payment.getExpDate().isAfter(LocalDate.now()))
                || payment.getExpDate().equals(LocalDate.now())) {
            amountPaid.setText(String.valueOf(payment.getAmountPaid()));
            paidBy.setValue(payment.getPaidBy());
            discount.setText(String.valueOf(payment.getDiscount()));
            poxing.setSelected(payment.isPoxing());
            paymentIsGoing = true;
            tellInfo(payment);

            blockFields(payment);
        }
    }

    private void tellInfo(Payments payment) {
        paymentInfo.setText(customer.getFirstName() + " Wakhtigu kama dhicin");
        infoMin.setText("wuxuse ka dhaacyaa [" + payment.getExpDate().toString() + "] Insha Allah");
        paymentInfo.setStyle("-fx-text-fill: red;");
        FadeIn fadeIn = new FadeIn(paymentInfo);
        fadeIn.setCycleCount(50);
        fadeIn.setDelay(Duration.millis(100));
        fadeIn.play();
    }

    private void blockFields(Payments payment) {
        if (paymentIsGoing) {
            boxChooser.setValue(payment.getBox());
            amountPaid.setEditable(false);
            amountPaid.setText(String.valueOf(payment.getAmountPaid()));
            poxing.setSelected(payment.isPoxing());
            poxing.setDisable(true);
            paidBy.setValue(payment.getPaidBy());
            discount.setText(String.valueOf(payment.getDiscount()));
            discount.setEditable(false);
            createBtn.setDisable(true);

        }
    }

    private void initFields() {
        paidBy.setItems(super.getPaidBy());
        currentCost = fitnessCost;
        amountPaid.setText(String.valueOf(currentCost));
        amountPaid.setEditable(!paymentIsGoing);
        getMandatoryFields().addAll(amountPaid, paidBy);

        if (boxChooser.getItems().isEmpty()) {
            for (Box box : currentGym.getVipBoxes()) {
                if (box.isReady()) boxChooser.getItems().add(box);
            }
        }
        boxChooser.getItems().add(new Box(0, "remove box", false));
    }

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
