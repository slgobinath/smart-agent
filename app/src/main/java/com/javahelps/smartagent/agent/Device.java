package com.javahelps.smartagent.agent;

import android.content.Context;
import android.util.Log;

import com.javahelps.smartagent.communication.DeviceData;
import com.javahelps.smartagent.sensor.Sensor;
import com.javahelps.smartagent.sensor.SensorListener;
import com.javahelps.smartagent.util.Config;
import com.javahelps.smartagent.util.Constant;
import com.javahelps.smartagent.util.Session;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Device {

    private static final String TAG = "Device";

    private final Context context;
    private final Map<String, Sensor> sensors = new HashMap<>();
    private final Map<String, Object[]> sensorData = new HashMap<>();
    private int lastBatteryLevel = -1;
    private long lastBatteryLevelTime;
    private Sensor batterySensor;
    private boolean charging;
    private DeviceData recentDeviceData;

    public Device(Context context) {
        this.context = context;
    }

    public void init() {

        this.batterySensor = Sensor.create(this.context, Constant.Sensor.BATTERY);

        for (String sensorName : Config.SUPPORTED_SENSORS) {
            Sensor sensor = Sensor.create(this.context, sensorName);
            if (sensor != null) {

                Log.i(TAG, String.format("Sensor %s is available", sensor.toString()));

                sensor.setListener(new SensorListener() {
                    @Override
                    public void onSuccess(String sensor, Object... extras) {
                        if (extras != null) {
//                            Log.i(TAG, "Received " + sensor + " " + Arrays.toString(extras));
                            Device.this.sensorData.put(sensor, extras);

                            if (Device.this.sensorData.size() == Device.this.sensors.size()) {
                                Log.i(TAG, "All received");
                                DeviceData deviceData = new DeviceData();
                                deviceData.setActive(true);
                                deviceData.setComputingPower(Device.this.computingPower());
                                deviceData.addUser((String) Session.INSTANCE.get(Constant.Common.USER_EMAIL));
                                Device.this.recentDeviceData = deviceData;
                            }
                        }
                    }

                    @Override
                    public void onFailure(String sensor, Exception exception) {

                    }
                });
                sensor.init();
                sensors.put(sensorName, sensor);
            }
        }
    }

    public void clearData() {
        this.sensorData.clear();
    }

    public void stop() {

    }

    public DeviceData getRecentDeviceData() {
        return recentDeviceData;
    }

    public int currentBatteryLevel() {

        long currentTime = System.currentTimeMillis();
        if (lastBatteryLevel == -1 || (currentTime - lastBatteryLevelTime) > Config.BATTERY_LEVEL_CACHE_TIMEOUT) {
            Object[] data = (Object[]) this.batterySensor.retrieve();
            this.lastBatteryLevel = (Integer) data[0];
            this.charging = (Boolean) data[1];
            this.lastBatteryLevelTime = currentTime;
        }
        return lastBatteryLevel;
    }

    public Collection<Sensor> getAvailableSensors() {
        return this.sensors.values();
    }

    public Map<String, Object[]> getSensorData() {
        return sensorData;
    }

    public int computingPower(Sensor sensor) {

        int computingPower;
        int currentBatteryLevel = this.currentBatteryLevel();
        int minBatteryRequirement = sensor.getMinimumBatteryRequirement();

        Log.d(TAG, "Battery level: " + currentBatteryLevel);

        if (sensor == null || (!charging &&
                (currentBatteryLevel <= Config.MIN_BATTERY_LEVEL || currentBatteryLevel < minBatteryRequirement))) {
            // Battery level is below the minimum threshold/sensor requirement or no hardware support
            computingPower = 0;
        } else if (charging) {
            computingPower = 10;
        } else {
            computingPower = currentBatteryLevel / minBatteryRequirement;
        }

        return computingPower;
    }

    public int computingPower() {

        int computingPower;
        int currentBatteryLevel = this.currentBatteryLevel();

        if (!charging && currentBatteryLevel <= Config.MIN_BATTERY_LEVEL) {
            // Battery level is below the minimum threshold
            computingPower = 0;
        } else if (charging) {
            computingPower = 10;
        } else {
            computingPower = currentBatteryLevel / 10;
        }

        return computingPower;
    }
}
