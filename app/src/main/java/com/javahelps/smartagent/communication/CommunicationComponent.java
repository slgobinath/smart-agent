package com.javahelps.smartagent.communication;

public interface CommunicationComponent {

    void connect();

    void disconnect();

    void send(DeviceData deviceData);

    void setListener(ResponseListener listener);
}
