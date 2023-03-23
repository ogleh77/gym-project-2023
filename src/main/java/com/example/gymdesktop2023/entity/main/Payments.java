package com.example.gymdesktop2023.entity.main;


import com.example.gymdesktop2023.entity.Box;
import com.jfoenix.controls.JFXButton;

import java.time.LocalDate;

public class Payments {
    private final int paymentID;
    private final String paymentDate;
    private final LocalDate expDate;
    private final String month;
    private final String year;
    private final double amountPaid;
    private final String paidBy;
    private final double discount;
    private final boolean poxing;
    private Box box;
    private final String customerFK;
    private final boolean online;
    private final boolean pending;

    private final JFXButton pendingBtn;

    public Payments(int paymentID, String paymentDate, LocalDate expDate, String month, String year, double amountPaid, String paidBy, double discount, boolean poxing, String customerFK, boolean online, boolean pending) {
        this.paymentID = paymentID;
        this.paymentDate = paymentDate;
        this.expDate = expDate;
        this.month = month;
        this.year = year;
        this.amountPaid = amountPaid;
        this.paidBy = paidBy;
        this.discount = discount;
        this.poxing = poxing;
        this.customerFK = customerFK;
        this.online = online;
        this.pending = pending;
        this.pendingBtn = new JFXButton("Xanib");
        this.pendingBtn.setStyle("-fx-background-color: #1e6e66;-fx-text-fill: white;-fx-pref-width: 100;-fx-font-size: 15");
//        if (!this.online) {
//            pendingBtn.setDisable(true);
//        }
    }

    public int getPaymentID() {
        return paymentID;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public double getDiscount() {
        return discount;
    }

    public boolean isPoxing() {
        return poxing;
    }

    public Box getBox() {
        return box;
    }

    public String getCustomerFK() {
        return customerFK;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isPending() {
        return pending;
    }

    public void setBox(Box box) {
        this.box = box;
    }


    public JFXButton getPendingBtn() {
        return pendingBtn;
    }

    //  @Override
//    public String toString() {
//        return "--paymentDate: " + paymentDate + " expDate: " + expDate + " is_online: " + isOnline() + "customer phone " + customerFK;
//    }
    @Override
    public String toString() {
        return "Payments{" +
                "paymentID=" + paymentID +
                ", paymentDate='" + paymentDate + '\'' +
                ", expDate=" + expDate +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", amountPaid=" + amountPaid +
                ", paidBy='" + paidBy + '\'' +
                ", discount=" + discount +
                ", poxing=" + poxing +
                ", box=" + box +
                ", customerFK='" + customerFK + '\'' +
                ", online=" + online +
                ", pending=" + pending +
                '}';
    }
}
