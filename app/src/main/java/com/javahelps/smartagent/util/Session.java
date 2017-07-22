package com.javahelps.smartagent.util;

import java.util.HashMap;
import java.util.Map;

public enum Session {
    INSTANCE;

    private final Map<String, Object> map = new HashMap<>();

    public <T> void set(String key, T value) {
        this.map.put(key, value);
    }

    public <T> T get(String key) {
        return (T) this.map.get(key);
    }
}
