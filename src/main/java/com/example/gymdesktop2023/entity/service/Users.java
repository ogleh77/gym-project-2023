package com.example.gymdesktop2023.entity.service;

public class Users {
    private int userId;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String gender;
    private final String shift;
    private final String username;
    private  String password;
    private final String image;
    private final String role;


    public Users(int userId, String firstName, String lastName, String phone, String gender, String shift, String username, String password, String image, String role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.shift = shift;
        this.username = username;
        this.password = password;
        this.image = image;
        this.role = role;
    }

    public Users(String firstName, String lastName, String phone, String gender, String shift, String username, String password, String image, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.gender = gender;
        this.shift = shift;
        this.username = username;
        this.password = password;
        this.image = image;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getShift() {
        return shift;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    @Override
//    public String toString() {
//        return "[" + username + " role:- " + role + "]\n";
//    }
//


    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", shift='" + shift + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
