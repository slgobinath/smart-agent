package com.javahelps.smartagent.communication;

import java.util.List;

/**
 * Created by gobinath on 21/07/17.
 */

public class DeviceData {

    private String user;
    private int computingPower;
    private List<SensorData> sensorDataList;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getComputingPower() {
        return computingPower;
    }

    public void setComputingPower(int computingPower) {
        this.computingPower = computingPower;
    }

    public List<SensorData> getSensorDataList() {
        return sensorDataList;
    }

    public void setSensorDataList(List<SensorData> sensorDataList) {
        this.sensorDataList = sensorDataList;
    }
}
