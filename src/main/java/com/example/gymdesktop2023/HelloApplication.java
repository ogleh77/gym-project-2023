package com.example.gymdesktop2023;

import com.example.gymdesktop2023.controllers.main.DashboardController;
import com.example.gymdesktop2023.dao.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymdesktop2023/views/desing/gym.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        DashboardController controller = fxmlLoader.getController();
//        // controller.setCustomer(CustomerService.fetchAllCustomer(UserService.users().get(0)).get(5));
//        controller.setActiveUser(UserService.users().get(0));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    // TODO: 13/03/2023 Trigger hadii customer phone ka la update gareyo paymentkana updategareya insha Allah
    public static void main(String[] args) {
        launch();
    }
}