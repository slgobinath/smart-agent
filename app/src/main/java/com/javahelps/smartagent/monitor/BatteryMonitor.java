package com.javahelps.smartagent.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.javahelps.smartagent.util.Constant;

/**
 * Monitor the battery state change.
 */
public class BatteryMonitor extends SystemBroadcastMonitor {


    public BatteryMonitor(Context context) {
        super(context);
    }

    @Override
    public void start() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        this.context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        switch (action) {
            case Intent.ACTION_POWER_CONNECTED:
                this.notifyListeners(Constant.State.POWER_CONNECTED);
                break;

            case Intent.ACTION_POWER_DISCONNECTED:
                this.notifyListeners(Constant.State.POWER_DISCONNECTED);
                break;

            case Intent.ACTION_BATTERY_OKAY:
                this.notifyListeners(Constant.State.BATTERY_OKAY);
                break;

            case Intent.ACTION_BATTERY_LOW:
                this.notifyListeners(Constant.State.BATTERY_LOW);
                break;
        }
    }
}
