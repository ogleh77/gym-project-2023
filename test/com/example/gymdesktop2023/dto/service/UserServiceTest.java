package com.example.gymdesktop2023.dto.service;

import com.example.gymdesktop2023.entity.service.Users;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class UserServiceTest {

    @Test
    void insertUser() throws SQLException {
        Users user = new Users("Luul", "Ahmed", "Jama", "Female",
                "Morning", "lulka", "1234", null, "super_admin");
        UserService.insertUser(user);
    }

    @Test
    void update() throws SQLException {
        Users user = new Users(1,"Quman", "Ahmed", "Jama", "Female",
                "Morning", "lulka", "1234", null, "super_admin");

        UserService.update(user);
    }

    @Test
    void users() throws SQLException {

        System.out.println(UserService.users());
        System.out.println(UserService.users().hashCode());
        System.out.println(UserService.users().hashCode());
    }
}