package com.javahelps.smartagent.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.javahelps.smartagent.R;
import com.javahelps.smartagent.agent.SmartAgent;

public class MainActivity extends AppCompatActivity {

    private SmartAgent smartAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.smartAgent = new SmartAgent(this);
    }

    public void onStartClicked(View view) {
        this.smartAgent.start();
        Log.i("SmartAgent", "SmartAgent has been started");
    }

    public void onStopClicked(View view) {
        smartAgent.collectSensorData();
        smartAgent.stop();
        Log.i("SmartAgent", "Stopped the monitor");
    }
}
