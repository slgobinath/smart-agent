package com.javahelps.smartagent.sensor;

import android.content.Context;

import com.javahelps.smartagent.util.Constant;

public class HumiditySensor extends EnvironmentSensor {

    public HumiditySensor(Context context) {
        super(context, android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    @Override
    public String toString() {
        return Constant.Sensor.HUMIDITY;
    }
}
