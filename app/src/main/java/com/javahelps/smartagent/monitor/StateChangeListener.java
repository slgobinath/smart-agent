package com.javahelps.smartagent.monitor;

/**
 * Created by gobinath on 18/07/17.
 */

public interface StateChangeListener {

    void onStateChanged(String state, Object... extras);
}
