package com.example.gymdesktop2023.controllers;

import com.example.gymdesktop2023.dto.main.CustomerService;
import com.example.gymdesktop2023.entity.main.Customers;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.entity.service.Users;
import com.example.gymdesktop2023.helpers.CommonClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SplashScreenController extends CommonClass implements Initializable {
    @FXML
    private ProgressBar progress;

    @FXML
    private Label waiting;

    @FXML
    private Label welcomeUserName;
    private final ObservableList<Customers> warningList;
    private ObservableList<Customers> offlineCustomers;


    public SplashScreenController() {
        this.warningList = FXCollections.observableArrayList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FetchOnlineCustomersByGander.setOnSucceeded(e -> {
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gym/views2/done/warning.fxml"));
//            Scene scene = null;
//            Stage stage = new Stage();
//            try {
//                scene = new Scene(fxmlLoader.load());
//                WarningController controller = fxmlLoader.getController();
//                controller.setOutdatedCustomers(warningList);
//                stage.setScene(scene);
//                stage.initStyle(StageStyle.UNDECORATED);
//                stage.show();
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }

            System.out.println("Done");
        });
    }

    public Task<Void> FetchOnlineCustomersByGander = new Task<Void>() {
        private final LocalDate now = LocalDate.now();

        @Override
        protected Void call() throws Exception {
            int i = 0;
            offlineCustomers = CustomerService.fetchOnlineCustomer(activeUser);
            //----Check-----
            for (Customers customer : offlineCustomers) {
                i++;
                updateMessage("Loading.. " + i + "%");
                updateProgress(i, offlineCustomers.size());
                for (Payments payment : customer.getPayments()) {
                    LocalDate expDate = payment.getExpDate();
                    if (now.plusDays(2).isEqual(expDate)
                            || now.plusDays(1).isEqual(expDate)
                            || now.isEqual(expDate)) {
                        warningList.add(customer);
                        System.out.println(customer.getFirstName() + " " + expDate + " Warning");
                    } else if (now.isBefore(expDate)) {
                        System.out.println(customer.getFirstName() + " " + expDate + " Is active");
                    } else {
                        // TODO: 05/04/2023 Make the payment of
                        System.out.println(customer.getFirstName() + " " + expDate + " Outdated");
                    }
                }
                Thread.sleep(100);
            }
            return null;
        }
    };

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        Thread thread = new Thread(FetchOnlineCustomersByGander);
        thread.setDaemon(true);
        thread.start();
        progress.progressProperty().bind(FetchOnlineCustomersByGander.progressProperty());
        welcomeUserName.setText("Welcome " + activeUser.getUsername());
        waiting.textProperty().bind(FetchOnlineCustomersByGander.messageProperty());
    }

}
