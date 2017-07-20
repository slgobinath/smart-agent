package com.javahelps.smartagent.cluster;

import android.content.Context;

import com.javahelps.smartagent.monitor.Monitor;
import com.javahelps.smartagent.monitor.StateChangeListener;
import com.javahelps.smartagent.monitor.WiFiMonitor;
import com.javahelps.smartagent.util.Constant;


public class Cluster {

    private final Context context;
    private Monitor wifiMonitor;
    private String clusterName;

    public Cluster(Context context) {
        this.context = context;
        this.wifiMonitor = new WiFiMonitor(context);
        this.wifiMonitor.addStateChangeListener(new StateChangeListener() {
            @Override
            public void onStateChanged(String state, Object... extras) {
                if (Constant.State.CONNECTED_TO_WIFI.equals(state)) {
                    Cluster.this.clusterName = (String) extras[0];
                    Cluster.this.broadcast();
                } else {
                    Cluster.this.clusterName = null;
                    Cluster.this.reset();
                }
            }
        });
    }

    public void start() {
        this.wifiMonitor.start();
    }

    public void stop() {
        this.wifiMonitor.stop();
    }

    public void broadcast() {

    }

    public void reset() {

    }
}
