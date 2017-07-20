package com.javahelps.smartagent.util;

/**
 * Created by gobinath on 19/07/17.
 */

public class Config {

    private Config() {

    }

    public static final String[] SUPPORTED_SENSORS = {
            /*Constant.Sensor.BATTERY,*/
            Constant.Sensor.HUMIDITY,
            Constant.Sensor.LIGHT,
            Constant.Sensor.LOCATION,
            Constant.Sensor.PRESSURE,
            Constant.Sensor.TEMPERATURE
    };

    public static final int BATTERY_LEVEL_CACHE_TIMEOUT = 1000;

    public static final int MIN_BATTERY_LEVEL = 15;

    public static final int HIGH_COMPUTING_POWER = 8;

    public static final int NORMAL_COMPUTING_POWER = 5;

    public static final int LOW_COMPUTING_POWER = 2;
}