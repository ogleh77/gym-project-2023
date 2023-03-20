package com.example.gymdesktop2023.controllers.users;

import animatefx.animation.FadeOut;
import com.example.gymdesktop2023.controllers.service.UserUpdateController;
import com.example.gymdesktop2023.dao.UserService;
import com.example.gymdesktop2023.entity.Users;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserChooserController implements Initializable {
    @FXML
    private ListView<Users> listView;
    private Stage thisStage;
    @FXML
    private AnchorPane choserPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            try {
                initFields();
                thisStage = (Stage) listView.getScene().getWindow();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void deleteHandler(ActionEvent event) {

    }

    @FXML
    void updateHandler(ActionEvent event) {
        FadeOut fadeOut = new FadeOut(choserPane);
        fadeOut.setOnFinished(e -> {
            thisStage.close();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/gymdesktop2023/views/users/user-update.fxml"));
            try {
                Scene scene=new Scene(loader.load());
                UserUpdateController controller=loader.getController();
                controller.setActiveUser(listView.getSelectionModel().getSelectedItem());
                Stage stage=new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        fadeOut.setDelay(Duration.millis(100));
        fadeOut.play();
        System.out.println(listView.getSelectionModel().getSelectedItems());
    }

    private void initFields() throws SQLException {
        listView.setItems(UserService.users());
    }

}
