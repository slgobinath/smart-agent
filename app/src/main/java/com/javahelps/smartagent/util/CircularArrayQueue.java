package com.javahelps.smartagent.util;

public class CircularArrayQueue<T> {
    private final int DEFAULT_CAPACITY = 50;
    private int rear, count;
    private T[] queue;

    public CircularArrayQueue() {
        rear = count = 0;
        queue = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    public void enqueue(T element) {

        queue[rear] = element;
        rear = (rear + 1) % queue.length;
        if (count < DEFAULT_CAPACITY) {
            count++;
        }
    }

    public String toString() {
        String result = "";
        int scan = 0;

        while (scan < count) {
            if (queue[scan] != null) {
                result += queue[scan].toString() + "\n";
            }
            scan++;
        }

        return result;

    }

}
