package com.example.gymdesktop2023.models.service;

import com.example.gymdesktop2023.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BackupModel {
    private static final Connection connection = DbConnection.getConnection();
    private static ObservableList<String> paths;

    public static void insertPath(String path) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO backup_table(location) VALUES('" + path + "')");
        System.out.println("Path saved");
    }


    public static ObservableList<String> backupPath() throws SQLException {
        if (paths == null) {
            paths = FXCollections.observableArrayList();
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM backup_table;");

            while (rs.next()) {
                paths.add(rs.getString("location"));
            }
            rs.close();
            statement.close();
        }


        return paths;
    }

    public static void backUp(String path) throws SQLException {

        Statement statement = connection.createStatement();
        String query = "BACKUP to " + path;
        System.out.println(query);
        statement.executeUpdate(query);
        System.out.println("Backup-ed");
    }

    public static void restore(String path) throws SQLException {
        String query = "RESTORE FROM " + path;
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        System.out.println("Restored-ed");

    }
}
