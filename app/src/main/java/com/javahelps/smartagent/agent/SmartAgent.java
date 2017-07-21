package com.javahelps.smartagent.agent;

import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

import com.javahelps.smartagent.cluster.Cluster;
import com.javahelps.smartagent.sensor.Sensor;
import com.javahelps.smartagent.util.Config;

import java.util.Map;


public class SmartAgent {

    private static final String TAG = "SmartAgent";

    private final Context context;
    private final Device device;
    private final Cluster cluster;
    private final AlarmManager alarmManager;

    public SmartAgent(Context context) {

        this.context = context;
        this.device = new Device(context);
        this.cluster = new Cluster(context);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void start() {
        this.device.init();
    }

    public void stop() {
        this.device.stop();
    }

    public void collectSensorData() {

        for (Sensor sensor : this.device.getAvailableSensors()) {
            int computingPower = this.device.computingPower(sensor);

            Log.i(TAG, String.format("Computing power for %s is %d", sensor.toString(), computingPower));
            if (computingPower > 0) {
                // Able to process locally
                if (computingPower >= Config.NORMAL_COMPUTING_POWER) {
                    sensor.retrieve();
                }
            }
        }

        Map<String, Object[]> data = this.device.getSensorData();
        Log.i(TAG, data.toString());
    }
}
