package com.javahelps.smartagent.util;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.javahelps.smartagent.R;

public class GoogleAuthenticator implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleAuthenticator";
    public static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private FragmentActivity activity;
    private FirebaseUser user;
    private UserChangeListener userChangeListener;

    public GoogleAuthenticator(FragmentActivity activity, UserChangeListener userChangeListener) {

        this.activity = activity;
        this.userChangeListener = userChangeListener;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        this.googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.user = firebaseAuth.getCurrentUser();
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        this.activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        this.firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        GoogleAuthenticator.this.userChangeListener.onChange(null);
                    }
                });
    }

    public void revokeAccess() {

        firebaseAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        GoogleAuthenticator.this.userChangeListener.onChange(null);
                    }
                });
    }

    public void setActivityResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            this.firebaseAuthWithGoogle(account);
        } else {
            this.userChangeListener.onChange(null);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this.activity, "Failed to connect due to Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        this.userChangeListener.showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Logger.d(TAG, "Successfully signed in");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            GoogleAuthenticator.this.userChangeListener.onChange(user);
                        } else {
                            Logger.w(TAG, "Failed to sign in", task.getException());
                            Toast.makeText(GoogleAuthenticator.this.activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            GoogleAuthenticator.this.userChangeListener.onChange(null);
                        }
                        GoogleAuthenticator.this.userChangeListener.hideProgressDialog();
                    }
                });
    }

    public interface UserChangeListener {

        void onChange(FirebaseUser user);

        void showProgressDialog();

        void hideProgressDialog();
    }
}
