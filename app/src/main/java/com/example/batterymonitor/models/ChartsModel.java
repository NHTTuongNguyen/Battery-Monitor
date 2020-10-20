package com.example.batterymonitor.models;

public class ChartsModel {
    int levelBattery;
    int hours;

    public ChartsModel(int levelBattery, int hours) {
        this.levelBattery = levelBattery;
        this.hours = hours;
    }

    public int getLevelBattery() {
        return levelBattery;
    }

    public void setLevelBattery(int levelBattery) {
        this.levelBattery = levelBattery;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
