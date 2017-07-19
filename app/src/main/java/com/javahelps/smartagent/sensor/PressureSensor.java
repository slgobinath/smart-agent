package com.javahelps.smartagent.sensor;

import android.content.Context;
import android.hardware.SensorManager;

import com.javahelps.smartagent.util.Constant;

public class PressureSensor extends EnvironmentSensor {

    private SensorManager sensorManager;
    private android.hardware.Sensor sensor;

    public PressureSensor(Context context) {
        super(context, android.hardware.Sensor.TYPE_PRESSURE);
    }

    @Override
    public String toString() {
        return Constant.Sensor.PRESSURE;
    }
}
