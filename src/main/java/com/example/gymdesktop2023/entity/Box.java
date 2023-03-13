package com.example.gymdesktop2023.entity;

public class Box {
    private int boxId;
    private String boxName;
    private boolean ready;



    public Box(int boxId, String boxName, boolean ready) {
        this.boxId = boxId;
        this.boxName = boxName;
        this.ready = ready;
    }

    public Box(String boxName) {
        this.boxName = boxName;
    }

    public int getBoxId() {
        return boxId;
    }

    public String getBoxName() {
        return boxName;
    }

    public boolean isReady() {
        return ready;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "Box{" +
                "boxId=" + boxId +
                ", boxName='" + boxName + '\'' +
                ", ready=" + ready +
                '}' + "\n";
    }
}
