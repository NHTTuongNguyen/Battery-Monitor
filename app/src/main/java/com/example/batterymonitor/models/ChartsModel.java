package com.example.batterymonitor.models;

public class ChartsModel {
    int toaDox;
    int toaDoy;

    public ChartsModel() {
    }

    public ChartsModel(int toaDox, int toaDoy) {
        this.toaDox = toaDox;
        this.toaDoy = toaDoy;
    }

    public int getToaDox() {
        return toaDox;
    }

    public void setToaDox(int toaDox) {
        this.toaDox = toaDox;
    }

    public int getToaDoy() {
        return toaDoy;
    }

    public void setToaDoy(int toaDoy) {
        this.toaDoy = toaDoy;
    }
}
