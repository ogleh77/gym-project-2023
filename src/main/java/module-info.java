module com.example.gymdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.jfoenix;
    requires junit;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.gymdesktop2023 to javafx.fxml;
    opens com.example.gymdesktop2023.entity.main to javafx.fxml;
    opens com.example.gymdesktop2023.models to javafx.fxml;
    opens com.example.gymdesktop2023.dto to javafx.fxml;


    exports com.example.gymdesktop2023.dto;
    exports com.example.gymdesktop2023.models;
    exports com.example.gymdesktop2023;
    exports com.example.gymdesktop2023.entity;
    exports com.example.gymdesktop2023.entity.main;


}