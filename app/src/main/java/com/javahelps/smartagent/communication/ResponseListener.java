package com.javahelps.smartagent.communication;

public interface ResponseListener {

    void onSuccess(CommunicationComponent communicationComponent, Object data);

    void onFail(CommunicationComponent communicationComponent, Object data);
}
