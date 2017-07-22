package com.javahelps.smartagent.communication;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.javahelps.smartagent.util.Logger;
import com.javahelps.smartagent.util.Utility;

public class HTTPCommunicationComponent implements CommunicationComponent, Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = "HTTPCommunication";

    private final Gson gson = new Gson();
    private final Context context;
    private static final String END_POINT_URL = "http://129.100.227.230:8080/service";
    private final RequestQueue queue;

    public HTTPCommunicationComponent(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    @Override
    public void connect() {
        // Do nothing
        Logger.d(TAG, "Starting the HTTP request queue");
        queue.start();
    }

    @Override
    public void disconnect() {
        // Do nothing
        Logger.d(TAG, "Stopping the HTTP request queue");
        queue.stop();
    }

    @Override
    public void send(DeviceData deviceData) {

        if (!Utility.isNetworkAvailable(this.context)) {
            Logger.i(TAG, "No internet to send data to the server");
            return;
        }
        Logger.i(TAG, "Sending the data to the server");
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
        Logger.i(TAG, "Response from the server: " + response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Logger.i(TAG, "Error in sending data to the server");
    }

    @Override
    public void setListener(ResponseListener listener) {
        // Do nothing
    }
}
