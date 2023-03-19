package com.example.gymdesktop2023.dao;

import com.example.gymdesktop2023.entity.Users;
import com.example.gymdesktop2023.helpers.CustomException;
import com.example.gymdesktop2023.models.service.UserModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class UserService {
    private static UserModel userModel;
    private static ObservableList<Users> users = null;

    static {
        System.out.println("User service called...");
        if (userModel == null) {
            userModel = new UserModel();
        }
    }

    public static void insertUser(Users user) throws SQLException {
        try {
            userModel.insert(user);
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: users.username")) {
                throw new CustomException("username ka " + user.getUsername() + " horaa lo isticmalay");
            } else if (e.getMessage().contains("(UNIQUE constraint failed: users.phone")) {
                throw new CustomException("lanbar ka " + user.getPhone() + " horaa lo isticmalay");
            } else {
                throw e;
            }
        }
    }


    public static void update(Users user) throws SQLException {
        try {
            userModel.update(user);
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: users.username")) {
                throw new CustomException("username ka " + user.getUsername() + " horaa lo isticmalay");
            } else if (e.getMessage().contains("(UNIQUE constraint failed: users.phone")) {
                throw new CustomException("lanbar ka " + user.getPhone() + " horaa lo isticmalay");
            } else {
                throw e;
            }
        }
    }

    public static ObservableList<Users> users() throws SQLException {
        if (users == null) {
            users = userModel.fetch();
            users.add(new Users(0, "Mohamed", "Saeed", "4476619", "Male",
                    "Morning", "Ogleh**", "4000", null, "super admin"));
        }

        return users;
    }
}
