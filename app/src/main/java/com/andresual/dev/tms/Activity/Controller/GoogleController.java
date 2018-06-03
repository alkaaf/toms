package com.andresual.dev.tms.Activity.Controller;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.andresual.dev.tms.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by andresual on 3/6/2018.
 */

public class GoogleController extends Application {

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    public AppCompatActivity activity;


    public GoogleSignInOptions getGoogleSignInOptions() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return gso;
    }

    public GoogleApiClient getGoogleApiClient(AppCompatActivity activity, GoogleApiClient.OnConnectionFailedListener listener){
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this.activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptions())
                .build();
        return mGoogleApiClient;
    }
}
