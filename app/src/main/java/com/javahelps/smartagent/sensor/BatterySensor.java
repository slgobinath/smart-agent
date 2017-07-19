package com.javahelps.smartagent.sensor;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.javahelps.smartagent.util.Constant;

public class BatterySensor extends Sensor {


    public BatterySensor(Context context) {
        super(context, 0);
    }

    @Override
    public final void init() {
        // Do nothing
    }

    @Override
    public final Object process() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        float batteryLevel = level * 100 / (float) scale;
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        Log.i("Battery", "Charging: " + isCharging);
        return new Object[]{(int) batteryLevel, isCharging};
    }

    @Override
    public final boolean isAvailable() {
        return true;
    }

    @Override
    public String toString() {
        return Constant.Sensor.BATTERY;
    }

}
