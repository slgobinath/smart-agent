package com.javahelps.smartagent.sensor;

public interface SensorListener {

    SensorListener NONE = new SensorListener() {
        @Override
        public void onSuccess(String sensor, Object... extras) {

        }

        @Override
        public void onFailure(String sensor, Exception exception) {

        }
    };

    void onSuccess(String sensor, Object... extras);

    void onFailure(String sensor, Exception exception);
}
