package com.example.gymdesktop2023.controllers.service;

import com.example.gymdesktop2023.dto.service.BoxService;
import com.example.gymdesktop2023.dto.service.GymService;
import com.example.gymdesktop2023.entity.service.Box;
import com.example.gymdesktop2023.entity.service.Gym;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.example.gymdesktop2023.helpers.CustomException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GymCreationController extends CommonClass implements Initializable {
    @FXML
    private TextField addBox;

    @FXML
    private TextField boxCost;

    @FXML
    private ComboBox<Box> boxView;

    @FXML
    private TextField fitnessCost;

    @FXML
    private TextField gymName;

    @FXML
    private AnchorPane gymPane;

    @FXML
    private TextField maxDiscount;

    @FXML
    private TextField pendDate;

    @FXML
    private TextField poxingCost;

    @FXML
    private TextField zaad;
    @FXML
    private TextField eDahab;

    private Gym currentGym;
    private final String regEx = "\\d{1,2}\\.?\\d{1,2}";

    public GymCreationController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initData);
        // pendValidation();
        boxValidation();
        pendValidation();
        discountValidation();
        fitnessValidation();
        zaadValidation();
        eDahabValidation();
    }


    @FXML
    void createBoxHandler(ActionEvent event) {
        Box box = new Box(addBox.getText());
        try {
            BoxService.insertBox(box);
            BoxService.fetchBoxes().add(box);
            boxView.getItems().add(box);
            informationAlert("Waxaad diwaan gelisay khanad cusub fadlan ka check garee box viewga");
        } catch (CustomException e) {
            errorMessage(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void boxValidation() {
        System.out.println("Validating box");
        boxCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                boxCost.setText(newValue.replaceAll("[^\\d\\.?}]", ""));
                System.out.println("Valid");
            } else {
                System.out.println("Invalid");
            }
        });
    }

    private void pendValidation() {
        System.out.println("Validating pend");
        pendDate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pendDate.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void discountValidation() {
        System.out.println("Validating max");

        maxDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxDiscount.setText(newValue.replaceAll("[^\\d\\.?}]", ""));
            }
        });
    }

    private void fitnessValidation() {
        System.out.println("Validating fitness");

        fitnessCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                fitnessCost.setText(newValue.replaceAll("[^\\d\\.?}]", ""));
            }
        });
    }

    private void zaadValidation() {
        System.out.println("Validating pend");
        zaad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                zaad.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void eDahabValidation() {
        System.out.println("Validating pend");
        eDahab.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                eDahab.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void initData() {
        gymName.setText(currentGym.getGymName());
        fitnessCost.setText(String.valueOf(currentGym.getFitnessCost()));
        boxCost.setText(String.valueOf(currentGym.getBoxCost()));
        poxingCost.setText(String.valueOf(currentGym.getPoxingCost()));
        maxDiscount.setText(String.valueOf(currentGym.getMaxDiscount()));
        eDahab.setText((String.valueOf(currentGym.geteDahab())));
        zaad.setText((String.valueOf(currentGym.getZaad())));
        boxView.setItems(currentGym.getVipBoxes());
        pendDate.setText(String.valueOf(currentGym.getPendingDate()));
    }

    @FXML
    void cancelHandler(MouseEvent mouseEvent) {
    }


    @FXML
    void updateHandler() {
        try {
            double fitness_Cost = Double.parseDouble(fitnessCost.getText());
            double pox_cost = Double.parseDouble(poxingCost.getText());
            double max_discount = Double.parseDouble(maxDiscount.getText());
            int zaad_number = Integer.parseInt(zaad.getText());
            int eDahab_number = Integer.parseInt(eDahab.getText());
            int pend_date = Integer.parseInt(pendDate.getText());
            double box_cost = Double.parseDouble(boxCost.getText());

            currentGym = new Gym(1, gymName.getText(), fitness_Cost, pox_cost, box_cost,
                    max_discount, pend_date, zaad_number, eDahab_number);
            GymService.updateGym(currentGym);

            informationAlert("Waxaad ku guleysatay update-garaynta gymka  ");
         } catch (Exception e) {
            if (e.getClass().isInstance(SQLException.class)) {
                errorMessage(e.getMessage());
            } else {
                errorMessage("Fadlan hubi inad si saxan u gelisay qimayasha " +
                        "Tusaale 12 AMA 12.0 error caused " + e.getMessage());

            }
        }

    }

}
