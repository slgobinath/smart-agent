package com.javahelps.smartagent.coordination;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.javahelps.smartagent.communication.CommunicationComponent;
import com.javahelps.smartagent.communication.D2DCommunicationComponent;
import com.javahelps.smartagent.communication.DeviceData;
import com.javahelps.smartagent.communication.HTTPCommunicationComponent;
import com.javahelps.smartagent.communication.ResponseListener;
import com.javahelps.smartagent.communication.SensorData;


public class CoordinationComponent implements ResponseListener {

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

    public void send(SensorData sensorData) {

    }

    /**
     * Send the data of this device to other devices in the cluster.
     *
     * @param deviceData
     */
    public void send(DeviceData deviceData) {

        this.currentDeviceData = deviceData;
        this.clusterCommunication.send(deviceData);
    }

    public void send(int computingPower) {

        this.clusterCommunication.connect();
        this.clusterCommunication.publish(Integer.toString(computingPower));

        Looper looper = new Looper();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        }, 45);
    }

    @Override
    public void onSuccess(CommunicationComponent communicationComponent, Object data) {

        if (communicationComponent == this.clusterCommunication) {
            DeviceData deviceData = (DeviceData) data;
            if (this.currentDeviceData.getComputingPower() >= deviceData.getComputingPower()) {
                this.currentDeviceData.merge(deviceData);
            } else {
                // Don't send some other device will send it
            }
        }
    }

    @Override
    public void onFail(CommunicationComponent communicationComponent, Object data) {

    }
}
