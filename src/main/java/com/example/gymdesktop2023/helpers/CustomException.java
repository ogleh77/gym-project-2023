package com.example.gymdesktop2023.helpers;

import java.sql.SQLException;

public class CustomException extends SQLException {

    public CustomException(String message) {
        super(message);
    }

}
