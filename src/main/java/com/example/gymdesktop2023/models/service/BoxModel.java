package com.example.gymdesktop2023.models.service;

import com.example.gymdesktop2023.entity.Box;
import com.example.gymdesktop2023.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BoxModel {
    private static final Connection connection = DbConnection.getConnection();

    public void insert(Box box) throws SQLException {
        String insertBox = " INSERT INTO box(box_name) " +
                "VALUES ('" + box.getBoxName() + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(insertBox);
        System.out.println("Box saved....");
        statement.close();
    }

    public void update(Box box) throws SQLException {
        String boxQuery = "UPDATE box SET is_ready=false WHERE box_id=" + box.getBoxId();

        if (!box.isReady()) {
            boxQuery = "UPDATE box SET is_ready=true WHERE box_id=" + box.getBoxId();
        }
        Statement statement = connection.createStatement();
        statement.executeUpdate(boxQuery);

        //  box.setReady(box.isReady());
        System.out.println(box.isReady() ? "box made false " : "box made true");

    }

    public ObservableList<Box> fetchBoxes() throws SQLException {
        ObservableList<Box> boxes = FXCollections.observableArrayList();
        Box box;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM box");

        while (rs.next()) {
            box = new Box(rs.getInt(1), rs.getString(2), rs.getBoolean(3));
            boxes.add(box);
        }
        rs.close();
        statement.close();
        return boxes;
    }

}
