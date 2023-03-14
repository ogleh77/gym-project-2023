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
    opens com.example.gymdesktop2023.dto.main to javafx.fxml;
    exports com.example.gymdesktop2023.models.service;
    opens com.example.gymdesktop2023.models.service to javafx.fxml;
    opens com.example.gymdesktop2023.models.main to javafx.fxml;
    opens com.example.gymdesktop2023.controllers to javafx.fxml;

    exports com.example.gymdesktop2023;
    exports com.example.gymdesktop2023.entity.main;
    exports com.example.gymdesktop2023.entity.service;
    opens com.example.gymdesktop2023.entity.service to javafx.fxml;
    exports com.example.gymdesktop2023.dto.service;
    opens com.example.gymdesktop2023.dto.service to javafx.fxml;
    exports com.example.gymdesktop2023.dto.main;
    exports com.example.gymdesktop2023.models.main;


}