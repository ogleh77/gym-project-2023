package com.example.gymdesktop2023.dto.service;

import com.example.gymdesktop2023.entity.service.Gym;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class GymServiceTest {

    @Test
    void updateGym() throws SQLException {

        System.out.println("Before update");

        System.out.println(GymService.getGym());

        Gym gym = new Gym(1, "Guleed", 3.3, 4.5,
                6, 1, 14, 44433, 504383);

        GymService.updateGym(gym);
        System.out.println("After update");

        System.out.println(GymService.getGym());

    }

    @Test
    void getGym() throws SQLException {
        System.out.println(GymService.getGym());
        System.out.println(GymService.getGym().hashCode());
        System.out.println(GymService.getGym().hashCode());
        System.out.println(GymService.getGym().hashCode());
    }
}