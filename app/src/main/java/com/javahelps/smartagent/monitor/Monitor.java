package com.javahelps.smartagent.monitor;

/**
 * Created by gobinath on 18/07/17.
 */

public interface Monitor {

    void start();

    void stop();

    void addStateChangeListener(StateChangeListener listener);

    void removeStateChangeListener(StateChangeListener listener);
}
