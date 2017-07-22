package com.javahelps.smartagent.coordination;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.javahelps.smartagent.communication.CommunicationComponent;
import com.javahelps.smartagent.communication.D2DCommunicationComponent;
import com.javahelps.smartagent.communication.DeviceData;
import com.javahelps.smartagent.communication.HTTPCommunicationComponent;
import com.javahelps.smartagent.communication.ResponseListener;


public class CoordinationComponent implements ResponseListener {

    private static final String TAG = "CoordinationComponent";

    private final D2DCommunicationComponent clusterCommunication;
    private final HTTPCommunicationComponent serviceCommunication;
    private static volatile CoordinationComponent coordinationComponent;
    private DeviceData currentDeviceData;

    private CoordinationComponent(FragmentActivity activity) {
        this.clusterCommunication = new D2DCommunicationComponent(activity);
        this.serviceCommunication = new HTTPCommunicationComponent(activity);
        this.clusterCommunication.setListener(this);
        this.serviceCommunication.setListener(this);
    }

    public static CoordinationComponent getInstance(FragmentActivity activity) {
        if (coordinationComponent == null) {
            synchronized (CoordinationComponent.class) {
                if (coordinationComponent == null) {
                    coordinationComponent = new CoordinationComponent(activity);
                }
            }
        }
        return coordinationComponent;
    }

    /**
     * Send the data of this device to other devices in the cluster.
     *
     * @param deviceData
     */
    public void send(DeviceData deviceData) {

        this.currentDeviceData = deviceData;
        this.clusterCommunication.send(deviceData);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clusterCommunication.disconnect();
                if (currentDeviceData.isActive()) {
                    Log.i(TAG, "Sending device data " + currentDeviceData.getUser());
                }
            }
        }, 100);
    }

    @Override
    public void onSuccess(CommunicationComponent communicationComponent, Object data) {

        if (communicationComponent == this.clusterCommunication) {
            DeviceData deviceData = (DeviceData) data;
            if (this.currentDeviceData.getComputingPower() >= deviceData.getComputingPower()) {
                this.currentDeviceData.merge(deviceData);
            } else {
                // Don't send some other device will send it
                this.currentDeviceData.setActive(false);
                this.clusterCommunication.disconnect();
            }
        }
    }

    @Override
    public void onFail(CommunicationComponent communicationComponent, Object data) {

    }
}
