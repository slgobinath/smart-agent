package com.javahelps.smartagent.communication;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DeviceData implements Serializable {

    private List<String> users;
    private int computingPower;
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

    public void merge(DeviceData deviceData) {
        this.users.addAll(deviceData.users);
        for (SensorData sensorData : deviceData.sensorDataMap.values()) {
            SensorData thisSensorData = this.sensorDataMap.get(sensorData.getName());
            if (thisSensorData.getAccuracy() < sensorData.getAccuracy()) {
                thisSensorData.setData(sensorData.getData());
            }
        }
    }
}
