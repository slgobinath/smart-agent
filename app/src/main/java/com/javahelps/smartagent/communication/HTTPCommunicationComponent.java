package com.javahelps.smartagent.communication;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class HTTPCommunicationComponent implements CommunicationComponent, Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = "HTTPCommunication";

    private final Gson gson = new Gson();

    private static final String END_POINT_URL = "http://10.0.2.2:8080/service";
    private final RequestQueue queue;
    private ResponseListener responseListener;

    public HTTPCommunicationComponent(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    @Override
    public void connect() {
        // Do nothing
        Log.i(TAG, "Starting the HTTP request queue");
        queue.start();
    }

    @Override
    public void disconnect() {
        // Do nothing
        Log.i(TAG, "Stopping the HTTP request queue");
        queue.stop();
    }

    @Override
    public void send(DeviceData deviceData) {
        Log.i(TAG, "Sending the device data to the server");
        JsonRequest<String> request = new JsonRequest<String>(Request.Method.POST, END_POINT_URL,
                gson.toJson(deviceData), this, this) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String status = response.statusCode == 200 ? "SUCCESS" : "FAILED";
                return Response.success(status, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(request);
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Server response: " + response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void setListener(ResponseListener listener) {
        this.responseListener = listener;
    }
}
