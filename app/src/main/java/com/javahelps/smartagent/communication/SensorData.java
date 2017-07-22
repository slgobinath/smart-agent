package com.javahelps.smartagent.communication;

import java.io.Serializable;

public class SensorData implements Serializable {

    private String name;
    private float accuracy;
    private Serializable data;

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
}
