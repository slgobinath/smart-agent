package com.javahelps.smartagent.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.javahelps.smartagent.R;
import com.javahelps.smartagent.util.Constant;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private Button btnStart;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.btnStart = (Button) view.findViewById(R.id.btnStart);
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.Command.START.equals(btnStart.getText().toString())) {
                    // Start
                    btnStart.setText(Constant.Command.STOP);
                    listener.onFragmentInteraction(HomeFragment.this, Constant.Command.START);
                    Toast.makeText(getActivity(), "Smart Agent is started", Toast.LENGTH_SHORT).show();
                } else {
                    // Stop
                    btnStart.setText(Constant.Command.START);
                    listener.onFragmentInteraction(HomeFragment.this, Constant.Command.STOP);
                    Toast.makeText(getActivity(), "Smart Agent is stopped", Toast.LENGTH_SHORT).show();
                }
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
