package com.javahelps.smartagent.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Device monitor which is depending on system broadcasts.
 */

public abstract class SystemBroadcastMonitor extends BroadcastReceiver implements Monitor {

    protected final Context context;
    protected List<StateChangeListener> stateChangeListeners = new ArrayList<>();

    public SystemBroadcastMonitor(Context context) {
        this.context = context;
    }

    @Override
    public void stop() {
        this.context.unregisterReceiver(this);
    }

    @Override
    public void addStateChangeListener(StateChangeListener listener) {
        this.stateChangeListeners.add(listener);
    }

    @Override
    public void removeStateChangeListener(StateChangeListener listener) {
        this.stateChangeListeners.remove(listener);
    }

    protected final void notifyListeners(String state, Object... extras) {
        for (StateChangeListener listener : this.stateChangeListeners) {
            listener.onStateChanged(state, extras);
        }
    }
}
