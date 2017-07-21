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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.javahelps.smartagent.R;
import com.javahelps.smartagent.util.Constant;
import com.javahelps.smartagent.util.GoogleAuthenticator;
import com.javahelps.smartagent.util.Utility;
import com.javahelps.smartagent.view.fragment.HomeFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Loading...");
        this.googleAuthenticator = new GoogleAuthenticator(this, this);

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

        changeFragment(new HomeFragment());

        if (this.googleAuthenticator.getUser() == null) {
            this.googleAuthenticator.signIn();
        } else {
            this.onChange(this.googleAuthenticator.getUser());
            this.checkNonGrantedPermissions();
        }
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
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Log.i("HomeActivity", "Navigation item selected");
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragment = new HomeFragment();
        } else if (id == R.id.nav_permission) {
            fragment = new PermissionFragment();
        } else if (id == R.id.nav_manage) {

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

    /**
     * Change the active fragment to the given one.
     *
     * @param fragment
     */
    private void changeFragment(Fragment fragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Fragment fragment, String command) {
        if (fragment instanceof PermissionFragment) {
            if (Constant.Command.ALL_PERMISSIONS_GRANTED.equals(command)) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                toggle.setDrawerIndicatorEnabled(true);
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
            this.txtUsername.setText(user.getEmail());
        }
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
