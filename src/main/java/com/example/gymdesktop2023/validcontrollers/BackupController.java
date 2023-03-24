package com.example.gymdesktop2023.validcontrollers;

import com.example.gymdesktop2023.helpers.CommonClass;
import com.example.gymdesktop2023.models.service.BackupModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BackupController extends CommonClass implements Initializable {
    @FXML
    private ListView<String> listView;

    @FXML
    private TextField name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            getMandatoryFields().add(name);
            try {
                listView.setItems(BackupModel.backupPath());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void backupHandler() throws SQLException {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            BackupModel.backUp(listView.getSelectionModel().getSelectedItem());

        } else {
            System.out.println("Choose a path");
        }

    }

    // TODO: 24/03/2023 Insha Allah retore ka sii hubi 
    @FXML
    void restoreHandler() throws SQLException {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            BackupModel.restore(listView.getSelectionModel().getSelectedItem());

        } else {
            System.out.println("Choose a path");
        }


    }


    @FXML
    void pathHandler() throws SQLException {
        if (isValid(getMandatoryFields(), null)) {
            pathSelector();

        } else {
            System.out.println("Invalid");
        }
    }

    private void pathSelector() throws SQLException {
        File selectedPath;
        DirectoryChooser chooser = new DirectoryChooser();
        selectedPath = chooser.showDialog(null);
        BackupModel.insertPath(selectedPath.getAbsolutePath() + "/" + name.getText());
        listView.getItems().add(selectedPath.getAbsolutePath() + "/" + name.getText());
    }

}
