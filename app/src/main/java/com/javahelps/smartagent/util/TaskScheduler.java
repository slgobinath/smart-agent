package com.javahelps.smartagent.util;


import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TaskScheduler {

    private static final String TAG = "TaskScheduler";

    private Timer timer;
    private long intervalInMillis;
    private int intervalInSec;
    private Task task;


    public TaskScheduler(Task task, int intervalInSec) {
        if (task == null) {
            throw new NullPointerException("Task cannot be null");
        }
        this.task = task;
        this.intervalInSec = intervalInSec;
        this.intervalInMillis = intervalInSec * 1000;
    }

    public void start() {

        if (this.timer == null) {
            this.timer = new Timer(true);
        } else {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        int second = calendar.get(Calendar.SECOND);
        second += (this.intervalInSec - second % this.intervalInSec);
        calendar.set(Calendar.SECOND, second);

        Log.d(TAG, "Scheduling from: " + calendar.getTime());

        this.timer.scheduleAtFixedRate(new TimerTask() {
            public synchronized void run() {
                TaskScheduler.this.task.execute();
            }
        }, calendar.getTime(), intervalInMillis);
    }

    public void stop() {

        if (this.timer == null) {
            return;
        }
        this.timer.cancel();
        this.timer = null;
    }

    public interface Task {
        void execute();
    }
}
