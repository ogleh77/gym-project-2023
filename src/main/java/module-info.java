module com.example.gymdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.jfoenix;
    requires junit;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.gymdesktop2023 to javafx.fxml;
    opens com.example.gymdesktop2023.controllers.service to javafx.fxml;

    exports com.example.gymdesktop2023;
    exports com.example.gymdesktop2023.entity;


}