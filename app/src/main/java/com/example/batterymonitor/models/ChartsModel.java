package com.example.batterymonitor.models;

public class ChartsModel {
    int levelBattery;
    String hours;

    public ChartsModel() {
    }

    public ChartsModel(int levelBattery) {
        this.levelBattery = levelBattery;
    }

    public ChartsModel(String hours) {
        this.hours = hours;
    }

    public ChartsModel(int levelBattery, String hours) {
        this.levelBattery = levelBattery;
        this.hours = hours;
    }

    public int getLevelBattery() {
        return levelBattery;
    }

    public void setLevelBattery(int levelBattery) {
        this.levelBattery = levelBattery;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
