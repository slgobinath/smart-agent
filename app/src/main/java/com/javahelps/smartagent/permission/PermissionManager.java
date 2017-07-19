package com.javahelps.smartagent.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.javahelps.smartagent.annotation.RequiredPermissions;

/**
 * Created by gobinath on 18/07/17.
 */

public class PermissionManager {

    private Context context;
    private Activity activity;

    public PermissionManager(Context context) {
        this.context = context;
    }

    public boolean verify(String... permissions) {
        boolean allGranted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this.context, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        return allGranted;
    }

    public boolean verify(Class<?> clazz) {
        boolean allGranted = true;
        RequiredPermissions annotation = clazz.getAnnotation(RequiredPermissions.class);
        if (annotation != null) {
            String[] requiredPermissions = annotation.value();
            allGranted = this.verify(requiredPermissions);
            if (!allGranted && annotation.request()) {
                // Request for the permission
            }
        }
        return allGranted;
    }
}
