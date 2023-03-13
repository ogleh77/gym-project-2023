package com.example.gymdesktop2023.entity.main;

import com.example.gymdesktop2023.entity.service.Box;

import java.time.LocalDate;
public class PaymentBuilder {
    private int paymentID;
    private String paymentDate;
    private LocalDate expDate;
    private String month;
    private String year;
    private double amountPaid;
    private String paidBy;
    private double discount;
    private boolean poxing;
    private Box box;
    private String customerFK;
    private boolean online;
    private boolean pending;


    public PaymentBuilder setPaymentID(int paymentID) {
        this.paymentID = paymentID;
        return this;
    }

    public PaymentBuilder setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public PaymentBuilder setExpDate(LocalDate expDate) {
        this.expDate = expDate;
        return this;
    }

    public PaymentBuilder setMonth(String month) {
        this.month = month;
        return this;
    }

    public PaymentBuilder setYear(String year) {
        this.year = year;
        return this;
    }

    public PaymentBuilder setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
        return this;
    }

    public PaymentBuilder setPaidBy(String paidBy) {
        this.paidBy = paidBy;
        return this;
    }

    public PaymentBuilder setDiscount(double discount) {
        this.discount = discount;
        return this;
    }

    public PaymentBuilder setPoxing(boolean poxing) {
        this.poxing = poxing;
        return this;
    }

    public PaymentBuilder setBox(Box box) {
        this.box = box;
        return this;
    }

    public PaymentBuilder setCustomerFK(String customerFK) {
        this.customerFK = customerFK;
        return this;
    }

    public PaymentBuilder setOnline(boolean online) {
        this.online = online;
        return this;
    }

    public PaymentBuilder setPending(boolean pending) {
        this.pending = pending;
        return this;
    }


    public Payments build() {
        return new Payments(paymentID, paymentDate, expDate,
                month, year, amountPaid, paidBy, discount, poxing, customerFK, online, pending);
    }
}
