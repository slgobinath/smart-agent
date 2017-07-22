package com.javahelps.smartagent.util;

import android.util.Log;


public class Logger {

    private static CircularArrayQueue<String> logs = new CircularArrayQueue<>();
    private static LogListener logListener;

    private Logger() {

    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
        logs.enqueue(message);
        if (logListener != null) {
            logListener.onLogChange(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable throwable) {
        Log.w(tag, message, throwable);
    }

    public static String getLogs() {
        return logs.toString();
    }

    public static void registerListener(LogListener listener) {
        logListener = listener;
    }

    public interface LogListener {
        void onLogChange(String tag, String message);
    }
}
