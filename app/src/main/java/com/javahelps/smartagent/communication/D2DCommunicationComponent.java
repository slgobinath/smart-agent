package com.javahelps.smartagent.communication;

import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.gson.Gson;

public class D2DCommunicationComponent implements CommunicationComponent, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "D2DCommunication";

    public static final int REQUEST_RESOLVE_ERROR = 1001;

    private final FragmentActivity activity;
    private GoogleApiClient googleApiClient;
    private final Gson gson = new Gson();
    private Message activeMessage;
    private MessageListener messageListener;

    public D2DCommunicationComponent(FragmentActivity activity) {
        this.activity = activity;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        messageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Found message: " + messageAsString);
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + messageAsString);
            }
        };
    }

    @Override
    public void connect() {
        Log.i(TAG, "Connect to client");
        googleApiClient.connect();
    }

    @Override
    public void disconnect() {
        if (googleApiClient.isConnected()) {
            unpublish();
            unsubscribe();
            googleApiClient.disconnect();
        }
    }

    @Override
    public void send(DeviceData deviceData) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        publish("Hello World 2");
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e(TAG, "GoogleApiClient disconnected with cause: " + cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this.activity, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "GoogleApiClient connection failed");
        }
    }

    public void publish(String message) {
        Log.i(TAG, "Publishing message: " + message);
        activeMessage = new Message(message.getBytes());
        Nearby.Messages.publish(googleApiClient, activeMessage);
    }

    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        if (activeMessage != null) {
            Nearby.Messages.unpublish(googleApiClient, activeMessage);
            activeMessage = null;
        }
    }

    // Subscribe to receive messages.
    private void subscribe() {
        Log.i(TAG, "Subscribing.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.Messages.subscribe(googleApiClient, messageListener, SubscribeOptions.DEFAULT);
    }

    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.Messages.unsubscribe(googleApiClient, messageListener);
    }

}
