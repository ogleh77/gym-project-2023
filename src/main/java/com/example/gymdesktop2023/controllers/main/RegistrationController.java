package com.example.gymdesktop2023.controllers.main;

import com.example.gymdesktop2023.dao.GymService;
import com.example.gymdesktop2023.dao.main.CustomerService;
import com.example.gymdesktop2023.entity.Gym;
import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.entity.main.CustomerBuilder;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistrationController extends CommonClass implements Initializable {

    @FXML
    private TextField address;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstName;

    @FXML
    private Label gymTitle;

    @FXML
    private Label headerInfo;

    @FXML
    private ImageView imgView;

    @FXML
    private TextField lastName;

    @FXML
    private JFXRadioButton male;

    @FXML
    private Label phoneValidation;

    @FXML
    private TextField middleName;

    @FXML
    private TextField phone;

    @FXML
    private JFXButton registerBtn;
    @FXML
    private ComboBox<String> shift;
    @FXML
    private TextField weight;
    @FXML
    private Label weightValidation;
    private final Gym currentGym;
    private boolean imageUploaded = false;
    private boolean isCustomerNew = true;
    private ButtonType ok;

    public RegistrationController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    private ObservableList<Customers> customersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            shift.setItems(getShift());
            male.setToggleGroup(genderGroup);
            female.setToggleGroup(genderGroup);
            getMandatoryFields().addAll(firstName, middleName, lastName, phone, shift);
        });

        phoneValidation();
        weightValidation();

        service.setOnSucceeded(e -> {
            registerBtn.setGraphic(null);
            registerBtn.setText(isCustomerNew ? "Saved" : "Updated");
            System.out.println("Done");
//            System.out.println("After update " + customersList.hashCode());

        });
    }

    @FXML
    void customerSaveHandler() throws SQLException {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7
                || !phoneValidation.isVisible()) && (weight.getText().length() == 2
                || !weightValidation.isVisible())) {

            if (!imageUploaded) {
                checkImage();
            }
            if (start) {
                service.restart();
                registerBtn.setGraphic(getLoadingImageView());
                registerBtn.setText(isCustomerNew ? "Saving" : "Updating");
            } else {
                service.start();
                registerBtn.setGraphic(getLoadingImageView());
                registerBtn.setText(isCustomerNew ? "Saving" : "Updating");
                start = true;
            }
        }

    }

    @FXML
    void uploadImageHandler() {
        uploadImage();
    }

    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        System.out.println(customer);
        if (customer != null) {
            firstName.setText(customer.getFirstName());
            middleName.setText(customer.getMiddleName());
            lastName.setText(customer.getLastName());
            phone.setText(customer.getPhone());
            weight.setText(String.valueOf(customer.getWeight()));
            shift.setValue(customer.getShift());
            address.setText(customer.getAddress() != null ? customer.getAddress() : "No address");
            if (customer.getGander().equals("Male")) {
                male.setSelected(true);
            } else {
                female.setSelected(true);
            }
            weight.setText(String.valueOf(customer.getWeight()));
            shift.setValue(customer.getShift());
            address.setText(customer.getAddress() != null ? customer.getAddress() : "No address");
            try {
                if (customer.getImage() != null) {
                    imageUploaded = true;
                    imgView.setImage(new Image(new FileInputStream(customer.getImage())));
                    selectedFile = new File(customer.getImage());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            headerInfo.setText("UPDATING EXISTED CUSTOMER ");
            isCustomerNew = false;
            registerBtn.setText("Update");
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        gymTitle.setText(currentGym.getGymName() + " eDahab: " + currentGym.geteDahab() + " Zaad: " + currentGym.getZaad());
        try {
            customersList = CustomerService.fetchAllCustomer(activeUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }


    //------------------helpers-------------------
    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {

                    try {
                        CustomerService.insertOrUpdateCustomer(savingCustomer(), isCustomerNew);
                        if (isCustomerNew) {
                            customersList.add(0, savingCustomer());
                            Thread.sleep(1000);
                            Platform.runLater(() -> informationAlert("Waxaad diwaan gelisay macmiil cusub"));
                        } else {
                            customersList.set(CustomerService.binarySearch(customersList,
                                    0, customersList.size() - 1,
                                    customer.getCustomerId()), customer);
                            Thread.sleep(1000);

                            Platform.runLater(() -> informationAlert("Waxaad update garaysay macmiilka ah "));
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> errorMessage(e.getMessage()));
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
    };


    private Customers savingCustomer() {
        String gander = male.isSelected() ? "Male" : "Female";
        String _address = address.getText() != null ? address.getText().trim() : null;
        double _weight = ((!weight.getText().isEmpty() || !weight.getText().isBlank())) ? Double.parseDouble(weight.getText().trim()) : 65.0;
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        int customerId = super.customer == null ? 0 : customer.getCustomerId();
        customer = new CustomerBuilder()
                .setShift(shift.getValue())
                .setCustomerId(customerId)
                .setGander(gander)
                .setWhoAdded(activeUser.getUsername())
                .setImage(image)
                .setAddress(_address)
                .setLastName(lastName.getText())
                .setFirstName(firstName.getText())
                .setMiddleName(middleName.getText())
                .setPhone(phone.getText())
                .setWeight(_weight)
                .build();
        return customer;
    }

    private void phoneValidation() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone.setText(newValue.replaceAll("[^\\d]", ""));
                phoneValidation.setText("Fadlan lanbarka xarfo looma ogola");
                phoneValidation.setVisible(true);
            } else if (!phone.getText().matches("^\\d{7}")) {
                phoneValidation.setText("Fadlan lanbarku kama yaran karo 7 digit");
                phoneValidation.setVisible(true);
            } else {
                phoneValidation.setVisible(false);
            }
        });

    }

    private void weightValidation() {
        weight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                weight.setText(newValue.replaceAll("[^\\d]", ""));
                weightValidation.setText("Fadlan misaanka xarfo looma ogola");
                weightValidation.setVisible(true);
            } else if (!weight.getText().matches("^\\d{1,2}")) {
                weightValidation.setText("Ma jiro qof boaqol sano ka badan");
                weightValidation.setVisible(true);
            } else {
                weightValidation.setVisible(false);
            }
        });

    }

    private void uploadImage() {
        try {
            if (selectedFile() != null) {
                Image image = new Image(new FileInputStream(selectedFile.getAbsolutePath()));
                imgView.setImage(image);
                imgView.setX(50);
                imgView.setY(25);
                imgView.setFitHeight(166);
                imgView.setFitWidth(200);
                imageUploaded = true;
            }
        } catch (FileNotFoundException e) {
            errorMessage("Fadlan sawirka lama helin isku day mar kale");
            imageUploaded = false;
        }
    }

    private void checkImage() {
        ButtonType ok = new ButtonType("Hada soo upload-garee", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Ogaan baan u dhaafay.", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Fadlan sawirku wuu kaa cawinayaa inaad wejiga \n" + "macmiilka ka dhex garan kartid macamisha kle", ok, cancel);
        alert.setTitle("Sawir lama helin");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ok) {
            uploadImage();
        } else {
            imageUploaded = true;
        }
    }


//    public void setCustomers(ObservableList<Customers> customers) {
//        this.customersList = customers;
////        System.out.println("Before update " + customers.hashCode());
////        System.out.println(customers);
//    }
}

