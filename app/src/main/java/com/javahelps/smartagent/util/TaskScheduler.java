package com.javahelps.smartagent.util;


import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TaskScheduler {

    private static final String TAG = "TaskScheduler";

    private Timer timer;
    private static final long INTERVAL = 10000;  // 10 sec
    private Task task;


    public TaskScheduler(Task task) {
        if (task == null) {
            throw new NullPointerException("Task cannot be null");
        }
        this.task = task;
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
        second += (10 - second % 10);
        calendar.set(Calendar.SECOND, second);

        Log.d(TAG, "Scheduling from: " + calendar.getTime());

        this.timer.scheduleAtFixedRate(new TimerTask() {
            public synchronized void run() {
                TaskScheduler.this.task.execute();
            }
        }, calendar.getTime(), INTERVAL);
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
