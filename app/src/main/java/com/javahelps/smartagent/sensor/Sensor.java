package com.javahelps.smartagent.sensor;

import android.content.Context;

import com.javahelps.smartagent.permission.PermissionManager;
import com.javahelps.smartagent.util.Config;
import com.javahelps.smartagent.util.Constant;

public abstract class Sensor {

    protected final Context context;
    private PermissionManager permissionManager;
    protected SensorListener listener = SensorListener.NONE;
    private final int minimumBatteryRequirement;

    protected Sensor(Context context) {
        this(context, Config.MIN_BATTERY_LEVEL);
    }

    protected Sensor(Context context, int minimumBatteryRequirement) {
        this.context = context;
        this.minimumBatteryRequirement = minimumBatteryRequirement;
        this.permissionManager = new PermissionManager(context);
        this.hasRequiredPermissions();
    }

    public static Sensor create(Context context, String sensorName) {

        Sensor sensor = null;
        switch (sensorName) {
            case Constant.Sensor.HUMIDITY:
                sensor = new HumiditySensor(context);
                break;
            case Constant.Sensor.LIGHT:
                sensor = new LightSensor(context);
                break;
            case Constant.Sensor.LOCATION:
                sensor = new LocationSensor(context);
                break;
            case Constant.Sensor.PRESSURE:
                sensor = new PressureSensor(context);
                break;
            case Constant.Sensor.TEMPERATURE:
                sensor = new TemperatureSensor(context);
                break;
            case Constant.Sensor.BATTERY:
                sensor = new BatterySensor(context);
                break;
        }
        if (sensor != null && !sensor.isAvailable() && !sensor.hasRequiredPermissions()) {
            sensor = null;
        }
        return sensor;
    }

    private boolean hasRequiredPermissions() {
        return permissionManager.verify(this.getClass());
    }

    public int getMinimumBatteryRequirement() {
        return minimumBatteryRequirement;
    }

    public abstract void init();

    protected abstract Object process();

    public abstract boolean isAvailable();

    public final Object retrieve() {
        this.listener.onSuccess(this.toString(), null);
        return this.process();
    }

    public void setListener(SensorListener listener) {
        this.listener = listener;
    }
}
