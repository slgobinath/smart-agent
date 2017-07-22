package com.javahelps.smartagent.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.javahelps.smartagent.R;
import com.javahelps.smartagent.agent.SmartAgent;
import com.javahelps.smartagent.communication.D2DCommunicationComponent;
import com.javahelps.smartagent.util.Constant;
import com.javahelps.smartagent.util.GoogleAuthenticator;
import com.javahelps.smartagent.util.Logger;
import com.javahelps.smartagent.util.Session;
import com.javahelps.smartagent.util.Utility;
import com.javahelps.smartagent.view.fragment.HomeFragment;
import com.javahelps.smartagent.view.fragment.LogFragment;
import com.javahelps.smartagent.view.fragment.OnFragmentInteractionListener;
import com.javahelps.smartagent.view.fragment.PermissionFragment;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener,
        GoogleAuthenticator.UserChangeListener {

    private static final String TAG = "HomeActivity";
    private ProgressDialog progressDialog;
    private TextView txtUsername;
    private GoogleAuthenticator googleAuthenticator;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private D2DCommunicationComponent d2DCommunicationComponent;
    private SmartAgent smartAgent;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the user interface
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Loading...");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        this.txtUsername = (TextView) header.findViewById(R.id.txtUsername);

        this.homeFragment = new HomeFragment();
        this.homeFragment.setListener(this);
        this.changeFragment(this.homeFragment);

        this.initializeServices();
    }

    private void initializeServices() {
        this.googleAuthenticator = new GoogleAuthenticator(this, this);
        if (this.googleAuthenticator.getUser() == null) {
            this.googleAuthenticator.signIn();
        } else {
            this.onChange(this.googleAuthenticator.getUser());
        }

        this.d2DCommunicationComponent = new D2DCommunicationComponent(this);
        this.smartAgent = new SmartAgent(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleAuthenticator.RC_SIGN_IN) {
            this.googleAuthenticator.setActivityResult(data);
        } else if (requestCode == D2DCommunicationComponent.REQUEST_RESOLVE_ERROR) {
            if (resultCode == RESULT_OK) {
                d2DCommunicationComponent.connect();
            } else {
                Logger.e(TAG, "GoogleApiClient connection failed. Unable to resolve.");
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            this.homeFragment.setRunning(this.smartAgent.isActive());
            this.homeFragment.setListener(this);
            fragment = this.homeFragment;
        } else if (id == R.id.nav_permission) {
            fragment = new PermissionFragment();
        } else if (id == R.id.nav_manage) {
            fragment = new LogFragment();
        } else if (id == R.id.nav_logout) {
            this.googleAuthenticator.signOut();
        }

        if (fragment != null) {
            this.changeFragment(fragment);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void changeFragment(Fragment fragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Fragment fragment, String command) {
        if (fragment instanceof PermissionFragment) {
            if (Constant.Command.ALL_PERMISSIONS_GRANTED.equals(command)) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                toggle.setDrawerIndicatorEnabled(true);
            }
        } else if (fragment instanceof HomeFragment) {
            if (Constant.Command.START.equals(command)) {
                this.smartAgent.start();
            } else if (Constant.Command.STOP.equals(command)) {
                this.smartAgent.stop();
            }
        }
    }

    private boolean checkNonGrantedPermissions() {
        String[] nonGrantedPermissions = Utility.nonGrantedPermissions(getApplicationContext());

        if (nonGrantedPermissions.length > 0) {
            // There are some non granted permissions

            // Disable user from moving to any other fragments without providing permissions
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);

            // Show permission fragment
            changeFragment(new PermissionFragment());
            return true;
        }
        return false;
    }


    @Override
    public void onChange(FirebaseUser user) {
        this.hideProgressDialog();
        if (user == null) {
            Toast.makeText(this, "Please sign in to use this application", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            Toast.makeText(this, "Welcome back " + user.getDisplayName() + "!", Toast.LENGTH_SHORT).show();

            String email = user.getEmail();
            Session.INSTANCE.set(Constant.Common.USER_EMAIL, email);
            this.txtUsername.setText(email);
            this.checkNonGrantedPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        this.progressDialog.dismiss();
        this.smartAgent.stop();
        super.onDestroy();
    }

    @Override
    public void showProgressDialog() {
        this.progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        this.progressDialog.hide();
    }
}
