package com.javahelps.smartagent.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.javahelps.smartagent.util.Constant;

/**
 * Monitor the device connecting to a new WiFi terminal and disconnecting from an already connected terminal.
 */

public class WiFiMonitor extends SystemBroadcastMonitor {

    private static final String UNKNOWN_SSID = "<unknown ssid>";
    private String currentSSID;

    public WiFiMonitor(Context context) {
        super(context);
    }

    @Override
    public void start() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        this.context.registerReceiver(this, intentFilter);
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null) {
            switch (info.getState()) {
                case CONNECTED:
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    if (!UNKNOWN_SSID.equals(ssid) && !ssid.equals(this.currentSSID)) {
                        this.currentSSID = ssid;
                        this.notifyListeners(Constant.State.CONNECTED_TO_WIFI, this.currentSSID);
                    }
                    break;
                case DISCONNECTED:
                    if (this.currentSSID != null) {
                        this.notifyListeners(Constant.State.DISCONNECTED_FROM_WIFI, this.currentSSID);
                        this.currentSSID = null;
                    }
                    break;
            }
        }
    }
}
