package com.javahelps.smartagent.agent;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.javahelps.smartagent.communication.DeviceData;
import com.javahelps.smartagent.coordination.CoordinationComponent;
import com.javahelps.smartagent.sensor.Sensor;
import com.javahelps.smartagent.util.Config;
import com.javahelps.smartagent.util.Logger;
import com.javahelps.smartagent.util.TaskScheduler;

import java.util.TimerTask;


public class SmartAgent {

    private static final String TAG = "SmartAgent";

    private final Context context;
    private final Device device;
    private boolean active;
    private CoordinationComponent coordinationComponent;
    private final TaskScheduler collectingScheduler;
    private final Handler handler;

    public SmartAgent(FragmentActivity activity) {

        this.context = activity;
        this.device = new Device(context);
        this.handler = new Handler();
        this.coordinationComponent = CoordinationComponent.getInstance(activity);
        this.collectingScheduler = new TaskScheduler(new TaskScheduler.Task() {
            @Override
            public void execute() {
                collectSensorData();
            }
        }, Config.DATA_COLLECTION_INTERVAL);
    }

    public void start() {
        Log.i(TAG, "Starting the smart agent");
        this.device.init();
        this.active = true;
        this.collectingScheduler.start();
    }

    public void stop() {
        Log.i(TAG, "Stopping the smart agent");
        this.active = false;
        this.collectingScheduler.stop();
        this.device.stop();
    }

    public boolean isActive() {
        return active;
    }

    public void collectSensorData() {

        if (!active) {
            return;
        }

        // Schedule to send data after Config.TIME_TO_COLLECT_DATA milliseconds
        this.handler.postDelayed(new TimerTask() {
            @Override
            public void run() {
                sendDeviceData();
            }
        }, Config.TIME_TO_COLLECT_DATA);

        Logger.i(TAG, "Collecting sensor data");
        this.device.clearData();
        for (Sensor sensor : this.device.getAvailableSensors()) {
            int computingPower = this.device.computingPower(sensor);
            if (computingPower > 0) {
                // Able to process locally
                if (computingPower >= Config.LOW_COMPUTING_POWER) {
                    sensor.retrieve();
                }
            }
        }
    }

    public void sendDeviceData() {

        if (!active) {
            return;
        }
        DeviceData deviceData = this.device.getRecentDeviceData();
        if (deviceData != null) {
            Logger.i(TAG, "Sending the sensor data to coordinator");
            this.coordinationComponent.send(deviceData);
        } else {
            Logger.i(TAG, "No sensor data to send");
        }
    }
}
