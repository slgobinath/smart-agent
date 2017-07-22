package com.javahelps.smartagent.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javahelps.smartagent.R;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("HomeFragment", "Connecting");
                mListener.onFragmentInteraction(HomeFragment.this, "CONNECT");
            }
        });

        view.findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("HomeFragment", "Sending http request");
                mListener.onFragmentInteraction(HomeFragment.this, "SEND");
            }
        });

        view.findViewById(R.id.btnDisconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("HomeFragment", "Disconnecting");
                mListener.onFragmentInteraction(HomeFragment.this, "DISCONNECT");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
