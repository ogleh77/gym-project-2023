package com.example.gymdesktop2023.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Gym {
    private int gymId;
    private SimpleStringProperty gymName = new SimpleStringProperty();
    private int zaad;
    private int eDahab;
    private double fitnessCost;
    private double poxingCost;
    private double boxCost;
    private int pendingDate;
    private double maxDiscount;
    private final ObservableList<Box> vipBoxes;

    public Gym(int gymId, String gymName, double fitnessCost, double poxingCost, double boxCost, double maxDiscount, int pendingDate, int zaad, int eDahab) {
        this.gymId = gymId;
        this.setGymName(gymName);
        this.fitnessCost = fitnessCost;
        this.poxingCost = poxingCost;
        this.boxCost = boxCost;
        this.maxDiscount = maxDiscount;
        this.zaad = zaad;
        this.eDahab = eDahab;
        this.pendingDate = pendingDate;
        this.vipBoxes = FXCollections.observableArrayList();
    }

    public void setGymName(String gymName) {
        this.gymName.set(gymName);
    }

    public int getGymId() {
        return gymId;
    }

    public String getGymName() {
        return gymName.get();
    }

    public SimpleStringProperty gymNameProperty() {
        return gymName;
    }

    public int getZaad() {
        return zaad;
    }

    public int geteDahab() {
        return eDahab;
    }

    public double getFitnessCost() {
        return fitnessCost;
    }

    public double getPoxingCost() {
        return poxingCost;
    }

    public double getBoxCost() {
        return boxCost;
    }

    public int getPendingDate() {
        return pendingDate;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public void seteDahab(int eDahab) {
        this.eDahab = eDahab;
    }

    public void setFitnessCost(double fitnessCost) {
        this.fitnessCost = fitnessCost;
    }

    public void setPoxingCost(double poxingCost) {
        this.poxingCost = poxingCost;
    }

    public void setBoxCost(double boxCost) {
        this.boxCost = boxCost;
    }

    public void setPendingDate(int pendingDate) {
        this.pendingDate = pendingDate;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public ObservableList<Box> getVipBoxes() {
        return vipBoxes;
    }

    @Override
    public String toString() {
        return "Gym{" +
                "gymId=" + gymId +
                ", gymName='" + gymName + '\'' +
                ", zaad='" + zaad + '\'' +
                ", eDahab='" + eDahab + '\'' +
                ", fitnessCost=" + fitnessCost +
                ", poxingCost=" + poxingCost +
                ", boxCost=" + boxCost +
                ", pendingDate=" + pendingDate +
                ", maxDiscount=" + maxDiscount +
                ", vipBoxes=" + vipBoxes +
                '}';
    }
}
