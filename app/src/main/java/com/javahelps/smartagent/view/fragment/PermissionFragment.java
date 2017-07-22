package com.javahelps.smartagent.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.javahelps.smartagent.R;
import com.javahelps.smartagent.util.Constant;
import com.javahelps.smartagent.util.Feature;
import com.javahelps.smartagent.util.Utility;


public class PermissionFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnGrantPermissions;
    private Context context;
    private final int PERMISSION_REQUEST_CODE = 100;
    private OnFragmentInteractionListener listener;

    public PermissionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permission, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerFeatures);
        this.btnGrantPermissions = (Button) view.findViewById(R.id.btnGrantPermissions);

        // Get all the features
        Feature[] features = Utility.getAllFeatures(this.context);
        final String[] nonGrantedPermissions = Utility.nonGrantedPermissions(this.context);

        if (nonGrantedPermissions.length != 0) {
            btnGrantPermissions.setVisibility(View.VISIBLE);
            btnGrantPermissions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(nonGrantedPermissions, PERMISSION_REQUEST_CODE);
                    }
                }
            });
        } else {
            btnGrantPermissions.setVisibility(View.GONE);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        RecyclerView.Adapter<FeatureViewHolder> adapter = new FeatureAdapter(features);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                btnGrantPermissions.setVisibility(View.GONE);
                listener.onFragmentInteraction(this, Constant.Command.ALL_PERMISSIONS_GRANTED);
            }
        }
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

    private class FeatureAdapter extends RecyclerView.Adapter<FeatureViewHolder> {
        private Feature[] features;

        public FeatureAdapter(Feature[] features) {
            this.features = features;
        }

        @Override
        public FeatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context ctx = parent.getContext();
            View itemView = LayoutInflater.from(ctx).inflate(R.layout.layout_feature_item, parent, false);
            return new FeatureViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FeatureViewHolder holder, int position) {
            Feature feature = features[position];
            holder.setFeature(feature);
        }

        @Override
        public int getItemCount() {
            return features.length;
        }
    }

    private class FeatureViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;
        private TextView txtFeature;
        private TextView txtDescription;

        public FeatureViewHolder(View itemView) {
            super(itemView);

            this.imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            this.txtFeature = (TextView) itemView.findViewById(R.id.txtFeature);
            this.txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
        }

        public void setFeature(Feature feature) {
            this.imgIcon.setImageDrawable(feature.getIcon());
            this.txtFeature.setText(feature.getName());
            this.txtDescription.setText(feature.getDescription());
        }
    }
}
