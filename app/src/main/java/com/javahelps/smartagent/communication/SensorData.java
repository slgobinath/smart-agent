package com.javahelps.smartagent.communication;

import java.io.Serializable;

public class SensorData implements Serializable {

    private String name;
    private float accuracy;
    private Serializable data;

    public SensorData() {

    }

    public SensorData(String name, float accuracy, Serializable data) {
        this.name = name;
        this.accuracy = accuracy;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "name='" + name + '\'' +
                ", accuracy=" + accuracy +
                ", data=" + data +
                '}';
    }
}
