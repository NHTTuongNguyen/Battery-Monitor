package com.example.batterymonitor.models;

public class ChartsModel {
    float levelBattery;
    float hours;

    public ChartsModel(float levelBattery, float hours) {
        this.levelBattery = levelBattery;
        this.hours = hours;
    }

    public float getLevelBattery() {
        return levelBattery;
    }

    public void setLevelBattery(int levelBattery) {
        this.levelBattery = levelBattery;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
