package com.javahelps.smartagent.communication;

public class SensorData {

    private String name;
    private float accuracy;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
