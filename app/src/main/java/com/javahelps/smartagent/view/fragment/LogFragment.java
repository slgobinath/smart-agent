package com.javahelps.smartagent.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javahelps.smartagent.R;
import com.javahelps.smartagent.util.Logger;

public class LogFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private TextView txtLog;
    private Handler handler = new Handler();

    public LogFragment() {
        // Required empty public constructor
    }

    public void setListener(OnFragmentInteractionListener mListener) {
        this.listener = mListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.txtLog = (TextView) view.findViewById(R.id.txtLog);
        this.txtLog.setText(Logger.getLogs());
        Logger.registerListener(new Logger.LogListener() {
            @Override
            public void onLogChange(String tag, final String message) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        txtLog.append("\n" + message);
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
