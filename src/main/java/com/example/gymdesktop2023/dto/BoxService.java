package com.example.gymdesktop2023.dto;

import com.example.gymdesktop2023.entity.Box;
import com.example.gymdesktop2023.helpers.CustomException;
import com.example.gymdesktop2023.models.BoxModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class BoxService {
    private static ObservableList<Box> boxes;
    private static final BoxModel boxModel = new BoxModel();


    public static void insertBox(Box box) throws SQLException {
        try {
            boxModel.insert(box);
        } catch (Exception e) {
            throw new CustomException("Lama diwan gelin karo khanadan fadlan ku celi " + e.getMessage());
        }

    }

    public static void updateBox(Box box) throws SQLException {
        boxModel.update(box);
        box.setReady(!box.isReady());
        int index = findBoxIndex(fetchBoxes(), box.getBoxId());
        fetchBoxes().set(index, box);
    }

    public static ObservableList<Box> fetchBoxes() throws SQLException {
        System.out.println("Called fetch box");
        if (boxes == null) {
            System.out.println("init boxes");
            boxes = boxModel.fetchBoxes();
        }

        return boxes;
    }


    public static int findBoxIndex(ObservableList<Box> boxes, int box_id) {
        int index = 0;
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = 0; j < boxes.size(); j++) {
                if (box_id == boxes.get(i).getBoxId()) {
                    index = i;
                    break;
                }
            }
        }
        return index;

    }
}
