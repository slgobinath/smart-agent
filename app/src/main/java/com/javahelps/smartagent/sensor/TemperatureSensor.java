package com.javahelps.smartagent.sensor;

import android.content.Context;
import android.hardware.SensorManager;

import com.javahelps.smartagent.util.Constant;

public class TemperatureSensor extends EnvironmentSensor {

    private SensorManager sensorManager;
    private android.hardware.Sensor sensor;

    public TemperatureSensor(Context context) {
        super(context, android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    @Override
    public String toString() {
        return Constant.Sensor.TEMPERATURE;
    }
}
