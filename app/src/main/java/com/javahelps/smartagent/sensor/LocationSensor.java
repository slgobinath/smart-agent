package com.javahelps.smartagent.sensor;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.javahelps.smartagent.annotation.RequiredPermissions;
import com.javahelps.smartagent.util.Constant;

@RequiredPermissions({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
public class LocationSensor extends Sensor implements OnSuccessListener<Location>, OnFailureListener {


    private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationSensor(Context context) {
        super(context, 20);
    }

    @Override
    public void init() {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.context);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public Object process() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
        return null;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        this.listener.onFailure(Constant.Sensor.LOCATION, e);
    }

    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            this.listener.onSuccess(Constant.Sensor.LOCATION, location.getLatitude(), location.getLongitude(), location.getAccuracy());
        } else {
            this.listener.onSuccess(Constant.Sensor.LOCATION, 0.0, 0.0, 0.0f);
        }
    }

    @Override
    public boolean isAvailable() {
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public String toString() {
        return Constant.Sensor.LOCATION;
    }
}
