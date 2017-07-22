package com.javahelps.smartagent.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class EnvironmentSensor extends Sensor implements SensorEventListener {

    private SensorManager sensorManager;
    private android.hardware.Sensor sensor;
    private int sensorType;

    public EnvironmentSensor(Context context, int sensorType) {
        super(context);
        this.sensorType = sensorType;
    }

    @Override
    public final void init() {
        sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(this.sensorType);
    }

    @Override
    public final Object process() {
        this.sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_FASTEST);
        return null;
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        this.listener.onSuccess(this.toString(), event.values[0], (float) event.accuracy);
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public final void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }

    @Override
    public final boolean isAvailable() {
        return ((SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE)).getDefaultSensor(this.sensorType) != null;
    }

}
