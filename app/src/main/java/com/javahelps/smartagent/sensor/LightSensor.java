package com.javahelps.smartagent.sensor;

import android.content.Context;

import com.javahelps.smartagent.util.Constant;

public class LightSensor extends EnvironmentSensor {

    public LightSensor(Context context) {
        super(context, android.hardware.Sensor.TYPE_LIGHT);
    }

    @Override
    public String toString() {
        return Constant.Sensor.LIGHT;
    }
}
