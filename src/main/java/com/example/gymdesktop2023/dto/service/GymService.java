package com.example.gymdesktop2023.dto.service;

import com.example.gymdesktop2023.entity.service.Gym;
import com.example.gymdesktop2023.helpers.CustomException;
import com.example.gymdesktop2023.models.service.GymModel;

import java.sql.SQLException;

public class GymService {
    private static final GymModel gymModel = new GymModel();
    private static Gym currentGym = null;

    public static void updateGym(Gym gym) throws SQLException {
        try {
            gymModel.update(gym);
            currentGym = gym;
        } catch (SQLException e) {
            throw new CustomException("Khalad ayaa dhacay fadlan hubi in wax ka bedelku dhaqan galay\n" +
                    "hadii kalese ku celi mar kale " + e.getMessage());
        }
    }

    public static Gym getGym() throws SQLException {
        if (currentGym == null) {
            System.out.println("Called");
            currentGym = gymModel.currentGym();
        }
        return currentGym;
    }

}
