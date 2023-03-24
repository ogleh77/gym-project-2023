package com.example.gymdesktop2023.dao;

import com.example.gymdesktop2023.entity.Box;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class BoxServiceTest {

    @Test
    void updateBox() throws SQLException {
        Box box = BoxService.fetchBoxes().get(1);
        BoxService.updateBox(box);
    }
}