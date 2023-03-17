package com.example.gymdesktop2023.controllers.info;

import com.example.gymdesktop2023.controllers.service.CardController;
import com.example.gymdesktop2023.dto.main.CustomerService;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.service.Users;
import com.example.gymdesktop2023.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class OutDatedController extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton female;

    @FXML
    private DatePicker fromDate;

    @FXML
    private Label headerInfo;

    @FXML
    private JFXRadioButton male;

    @FXML
    private Pagination pagination;

    @FXML
    private JFXComboBox<String> shift;

    @FXML
    private DatePicker toDate;

    private ObservableList<Customers> outDatedCustomers;
    @FXML
    private JFXButton searchBtn;
    private int column = 0;
    private int row = 0;
    private int perPage = 6;
    private final ToggleGroup toggleGroup;

    public OutDatedController() {
        this.toggleGroup = new ToggleGroup();
        this.outDatedCustomers = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            fromDate.setValue(LocalDate.now().minusDays(30));
            toDate.setValue(LocalDate.now());
            male.setToggleGroup(toggleGroup);
            female.setToggleGroup(toggleGroup);
            shift.setItems(super.getShift());
            shift.getItems().add("All");
            shift.setValue("All");
            getMandatoryFields().addAll(fromDate, toDate);
            pagination.setPageFactory(this::createPage);
          ///  pagination.setPageCount(20);
        });

        service.setOnSucceeded(e -> {
            searchBtn.setGraphic(null);
            searchBtn.setText("Search");
            //Collections.sort(outDatedCustomers);
            //pagination.setPageFactory(outDatedCustomers.isEmpty() ? this::vBox : this::createPage);

            pagination.setPageFactory(this::createPage);
         //   pagination.setPageCount(outDatedCustomers.isEmpty() ? 0 : 20);
//            System.out.println("\n");
//            System.out.println("Done...");


        });
    }

    @FXML
    void searchHandler() {
        if (isValid(getMandatoryFields(), null)) {
            if (start) {
                service.restart();
                searchBtn.setGraphic(getLoadingImageView());
                searchBtn.setText("Searching");
            } else {
                service.start();
                searchBtn.setGraphic(getLoadingImageView());
                searchBtn.setText("Searching");
                start = true;
            }
        } else {
            System.out.println("Invalid....");
        }

    }


    private GridPane createPage(int index) {
        int fromPage = perPage * index;
        int toIndex = Math.min(fromPage + perPage, 20);


        GridPane gridView = null;
        try {

            gridView = new GridPane();
            gridView.setHgap(10);
            gridView.setPadding(new Insets(40, 50, 10, 50));

            FXMLLoader loader;
            AnchorPane anchorPane;

            for (int i = fromPage; i < toIndex; i++) {
                System.out.println("I have " + outDatedCustomers.get(i));
                if (column == 2) {
                    column = 0;
                    row++;
                }

                loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/gymdesktop2023/views/service/customer-card.fxml"));
                anchorPane = loader.load();

                CardController controller = loader.getController();
                controller.setI(i);
             }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gridView;
    }

    private VBox vBox(int index) {
        VBox vBox = new VBox();
        vBox.setPrefSize(200, 200);
        Label label = new Label("No outdated meners found at " + fromDate.getValue() + " to " + toDate.getValue());

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label);
        return vBox;

    }

    private String customerQuery() {
        String query = "SELECT * FROM customers ";
        String ganderSelected = male.isSelected() ? "Male" : "Female";
        if (activeUser.getRole().equals("super_admin")) {
            query += "WHERE gander='" + ganderSelected + "'";
        } else if (activeUser.getRole().equals("admin")) {
            query += " WHERE gander='" + activeUser.getGender() + "'";
        }
        if (!shift.getValue().equals("All")) {
            query += "AND shift='" + shift.getValue() + "'";
        }
        return query;
    }


    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        System.out.println(activeUser);
        if (activeUser.getRole().equals("admin")) {
            male.setSelected(activeUser.getGender().equals("Male"));
            female.setSelected(activeUser.getGender().equals("Female"));

            male.setDisable(true);
            female.setDisable(true);

        }
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);
                    try {
                        String customerQuery = customerQuery();

                        outDatedCustomers = CustomerService.fetchQualifiedOfflineCustomers(customerQuery, fromDate.getValue(),
                                toDate.getValue());

                    } catch (Exception e) {
                        Platform.runLater(() -> errorMessage(e.getMessage()));
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
    };
}
