package com.example.gymdesktop2023.entity.main;

import javafx.collections.ObservableList;

public class CustomerBuilder {
    private int customerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String gander;
    private String shift;
    private String address;
    private String image;
    private double weight;
    private String whoAdded;
    private ObservableList<Payments> payments;


    public CustomerBuilder setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerBuilder setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public CustomerBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CustomerBuilder setGander(String gander) {
        this.gander = gander;
        return this;
    }

    public CustomerBuilder setShift(String shift) {
        this.shift = shift;
        return this;
    }

    public CustomerBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomerBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public CustomerBuilder setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public CustomerBuilder setWhoAdded(String whoAdded) {
        this.whoAdded = whoAdded;
        return this;
    }

    public CustomerBuilder setPayments(ObservableList<Payments> payments) {
        this.payments = payments;
        return this;
    }

    public Customers build() {
        return new Customers(customerId, firstName, middleName, lastName, phone, gander, shift, address, image,
                weight, whoAdded);
    }
}
