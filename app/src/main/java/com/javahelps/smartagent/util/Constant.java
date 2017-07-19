package com.javahelps.smartagent.util;

/**
 * Created by gobinath on 18/07/17.
 */
public class Constant {

    private Constant() {

    }

    public static class State {

        private State() {

        }

        public static final String CONNECTED_TO_WIFI = "CONNECTED_TO_WIFI";

        public static final String DISCONNECTED_FROM_WIFI = "DISCONNECTED_FROM_WIFI";

        public static final String POWER_CONNECTED = "POWER_CONNECTED";

        public static final String POWER_DISCONNECTED = "POWER_DISCONNECTED";

        public static final String BATTERY_LOW = "BATTERY_LOW";

        public static final String BATTERY_OKAY = "BATTERY_OKAY";
    }

    public static class Sensor {

        private Sensor() {

        }

        public static final String BATTERY = "BATTERY";

        public static final String LOCATION = "LOCATION";

        public static final String PRESSURE = "PRESSURE";

        public static final String TEMPERATURE = "TEMPERATURE";

        public static final String LIGHT = "LIGHT";

        public static final String HUMIDITY = "HUMIDITY";
    }
}
