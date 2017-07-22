package com.javahelps.smartagent.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceData implements Serializable {

    private List<String> users = new ArrayList<>();
    private int computingPower;
    private boolean active;
    private Map<String, SensorData> sensorDataMap;

    public String getUser() {
        return users.get(0);
    }

    public void addUser(String user) {
        this.users.add(user);
    }

    public int getComputingPower() {
        return computingPower;
    }

    public void setComputingPower(int computingPower) {
        this.computingPower = computingPower;
    }

    public Map<String, SensorData> getSensorDataMap() {
        return sensorDataMap;
    }

    public void setSensorDataMap(Map<String, SensorData> sensorDataMap) {
        this.sensorDataMap = sensorDataMap;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    public void merge(DeviceData deviceData) {
        this.users.addAll(deviceData.users);
        for (SensorData sensorData : deviceData.sensorDataMap.values()) {
            SensorData thisSensorData = this.sensorDataMap.get(sensorData.getName());
            if (thisSensorData.getAccuracy() < sensorData.getAccuracy()) {
                thisSensorData.setData(sensorData.getData());
            }
        }
    }

    @Override
    public String toString() {
        return "DeviceData{" +
                "users=" + users +
                ", computingPower=" + computingPower +
                ", active=" + active +
                ", sensorDataMap=" + sensorDataMap +
                '}';
    }
}
