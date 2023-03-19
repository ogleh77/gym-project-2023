package com.example.gymdesktop2023.entity;

public class UsersBuilder {
    private int userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private String shift;
    private String username;
    private String password;
    private String image;
    private String role;

    public UsersBuilder setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public UsersBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;

    }

    public UsersBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;

    }

    public UsersBuilder setPhone(String phone) {
        this.phone = phone;
        return this;

    }

    public UsersBuilder setGender(String gender) {
        this.gender = gender;
        return this;

    }

    public UsersBuilder setShift(String shift) {
        this.shift = shift;
        return this;

    }

    public UsersBuilder setUsername(String username) {
        this.username = username;
        return this;

    }

    public UsersBuilder setPassword(String password) {
        this.password = password;
        return this;

    }

    public UsersBuilder setImage(String image) {
        this.image = image;
        return this;

    }

    public UsersBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    public Users build() {
        return new Users(userId, firstName, lastName, phone, gender,
                shift, username, password, image, role);
    }
}
