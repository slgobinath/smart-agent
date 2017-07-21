package com.javahelps.smartagent.coordination;

import android.support.v4.app.FragmentActivity;

import com.javahelps.smartagent.communication.D2DCommunicationComponent;
import com.javahelps.smartagent.communication.HTTPCommunicationComponent;
import com.javahelps.smartagent.communication.SensorData;


public class CoordinationComponent {

    private final D2DCommunicationComponent clusterCommunication;
    private final HTTPCommunicationComponent serviceCommunication;
    private static volatile CoordinationComponent coordinationComponent;

    private CoordinationComponent(FragmentActivity activity) {
        this.clusterCommunication = new D2DCommunicationComponent(activity);
        this.serviceCommunication = new HTTPCommunicationComponent(activity);
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

    public void send(int computingPower) {
        this.clusterCommunication.connect();
        this.clusterCommunication.publish(Integer.toString(computingPower));
    }
}
