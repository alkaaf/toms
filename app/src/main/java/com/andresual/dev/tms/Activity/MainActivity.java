package com.andresual.dev.tms.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Controller.AuthController;
import com.andresual.dev.tms.Activity.Controller.GoogleController;
import com.andresual.dev.tms.Activity.Fragment.BerandaFragment;
import com.andresual.dev.tms.Activity.Manager.SessionDriverInfo;
import com.andresual.dev.tms.Activity.Manager.SessionManager;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.ReturnModel;
import com.andresual.dev.tms.Activity.Util.Netter;
import com.andresual.dev.tms.Activity.Util.Pref;
import com.andresual.dev.tms.Activity.Util.StringHashMap;
import com.andresual.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import io.fabric.sdk.android.Fabric;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    SessionManager sessionManager;
    SessionDriverInfo sessionDriverInfo;
    SharedPreferences sharedPreferences;
    Button btnSignIn;
    SignInButton btnLoginGoogle;
    TextView tvForgotPassword, tvRegister;
    EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String tokens, msg, idToken;
    ProgressDialog progressDialog;

    //    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
//    private GoogleSignInOptions gso;

    Integer ide;
    String idDriver, nama, email, password, alamat, kode, telp, kota, expiredToken, tokenServer;
    DriverModel driverActive;
    ArrayList<DriverModel> driverModelArrayList = new ArrayList<>();
//    String googleEmail, googleUsername;

    private static final int RC_SIGN_IN = 007;
    public static final int PERM_REQ = 12;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_REQ);
        }

//        gso = ((GoogleController) getApplication()).getGoogleSignInOptions();
//        mGoogleApiClient = ((GoogleController) getApplication()).getGoogleApiClient(MainActivity.this, this);

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS) // Before show message to turn on GPS be sure it is turned off.
        {
            buildAlertMessageNoGps();
        }


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                Log.d(TAG, "onAuthStateChanged:signed_in:" );
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        // session check
        Pref pref = new Pref(this);
        if (pref.checkDriver()) {
            startActivity(new Intent(this, PilihKendaraanActivity.class));
            finish();
        }

        btnSignIn = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_sign_up);
        etEmail = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLoginGoogle = findViewById(R.id.btn_login_google);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Signing you in...");

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Insert Email", Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Insert password", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.show();

                    DriverModel driverSignIn = new DriverModel();
                    driverSignIn.setEmail(etEmail.getText().toString());
                    driverSignIn.setPassword(etPassword.getText().toString());
                    driverSignIn.setToken(FirebaseInstanceId.getInstance().getToken());

                    String email = etEmail.getText().toString();
                    String pass = etPassword.getText().toString();
                    String token = FirebaseInstanceId.getInstance().getToken();
                    mAuth.signInWithEmailAndPassword(email, pass);
//                    AuthController.getmInstance().setDriverActive(driverSignIn);
//                    AuthController.getmInstance().signInApi(MainActivity.this);

                    signIn(email, pass, token);
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Harap perbolehkan seluruh permisi untuk menggunakan aplikasi", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void signIn(String email, String password, String token) {
        StringHashMap shm = new StringHashMap()
                .putMore("email", email)
                .putMore("password", password)
                .putMore("tokenreg", token);
        new Netter(this).byAmik(Request.Method.POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String message = obj.getString("message");
                    toast(message);
                    if (status == 200) {
                        String token = obj.getString("token");
                        // save login data
                        DriverModel driver = new Gson().fromJson(obj.getString("data"), DriverModel.class);
                        driver.setToken(token);
                        new Pref(MainActivity.this).putModelDriver(driver);
                        startActivity(new Intent(MainActivity.this, PilihKendaraanActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Netter.getDefaultErrorListener(this, new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }), Netter.Byamik.LOGINAPP, shm);
        //        queue.add(new StringRequest());
    }


//    private void signInGoogle() {
//        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(intent, RC_SIGN_IN);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //hasil mengembalikan dari launching intent dari google sign in api.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }

  /*  private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount userAccount = result.getSignInAccount();
//            idToken = userAccount.getIdToken();
            googleUsername = userAccount.getDisplayName();
            googleEmail = userAccount.getEmail();

            final Map<String, String> params = new HashMap<>();
            params.put("f", "savelogingoogle");
            params.put("email", googleEmail);
            Log.i("signingoogle", params.toString());

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/byamik.asp",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("response", response);
                            try {
                                JSONObject obj = new JSONObject(response);
//                            Log.i("token", obj.getString("token"));

                                ReturnModel returnModel = new ReturnModel(obj.getInt("status"), obj.getString("message"), obj);

                                if (returnModel.getStatusCode() == 200 || returnModel.getStatusCode() == -200) {
                                    Log.i("salah", String.valueOf(returnModel.getStatusCode()));
                                }

                                if (returnModel.getStatusCode() == 200) {
                                    System.out.println("token" + obj.getString("token"));
                                    tokenServer = obj.getString("token");
//                                    driverActive.setToken(token);
                                    Log.i("onResponse: ", tokenServer);

                                    JSONObject data = obj.getJSONObject("data");
                                    Log.i("onResponse: ", data.toString());
//                                    driverModelArrayList = new ArrayList<>();

                                    for (int i = 0; i < data.length(); i++) {
                                        ide = data.getInt("id");
                                        email = data.getString("email");
                                        password = data.getString("password");
                                        alamat = data.getString("alamat");
                                        kode = data.getString("kode");
                                        telp = data.getString("telp");
                                        kota = data.getString("kota");
                                        expiredToken = data.getString("expiredtoken");
                                        idDriver = data.getString("id_driver");
                                        nama = data.getString("nama");
                                    }
                                }
                                handleSignInGoogle(returnModel);

                            } catch (Throwable t) {
                                Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            queue.add(sr);

//            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//            intent.putExtra("username", displayedUsername);
//            intent.putExtra("email", userEmail);
//            startActivity(intent);
        }
    }*/

    public void handleSignIn(ReturnModel returnModel) {
        Log.e("handle", "status code = " + returnModel.getStatusCode());
        Log.e("handle", "message = " + returnModel.getMessage());
        if (returnModel.getStatusCode() == 200) {
            Toast.makeText(MainActivity.this, returnModel.getMessage(), Toast.LENGTH_SHORT).show();
            //masukkan data ke shared preferences
            sessionManager.createLoginSession(
                    AuthController.getmInstance().getDriverActive().getToken(),
                    AuthController.getmInstance().getDriverActive().getIdDriver(),
                    AuthController.getmInstance().getDriverActive().getEmail());
            sessionDriverInfo.createLoginSession(
                    AuthController.getmInstance().getDriverActive().getUsername(),
                    AuthController.getmInstance().getDriverActive().getEmail(),
                    AuthController.getmInstance().getDriverActive().getAlamat(),
                    AuthController.getmInstance().getDriverActive().getTelp(),
                    AuthController.getmInstance().getDriverActive().getKota());
            Intent intent = new Intent(MainActivity.this, PilihKendaraanActivity.class);
            startActivity(intent);
        } else if (returnModel.getStatusCode() == 101) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
        } else if (returnModel.getStatusCode() == 300) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, returnModel.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void handleSignInGoogle(ReturnModel returnModel) {
        Log.e("handle", "status code = " + returnModel.getStatusCode());
        Log.e("handle", "message = " + returnModel.getMessage());
        if (returnModel.getStatusCode() == 200) {
            Toast.makeText(MainActivity.this, returnModel.getMessage(), Toast.LENGTH_SHORT).show();
            //masukkan data ke shared preferences
            sessionManager.createLoginSession(
                    tokenServer,
                    idDriver,
                    email);
            sessionDriverInfo.createLoginSession(
                    nama,
                    email,
                    alamat,
                    telp,
                    kota);
            Intent intent = new Intent(MainActivity.this, PilihKendaraanActivity.class);
            startActivity(intent);
            finish();
        } else if (returnModel.getStatusCode() == 101) {
            noRegisterAlert();
//            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//            intent.putExtra("email", googleEmail);
//            intent.putExtra("username", googleUsername);
//            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

//    private class fetchCity extends AsyncTask<Void, Void, String> {
//
//        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            this.progressDialog.setMessage("Please wait...");
//            this.progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            try {
//                tokens = FirebaseInstanceId.getInstance().getToken();
//                Thread.sleep(1000);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            return tokens;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//            msg = tokens;
//        }
//    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setMessage("This app needs GPS for sharing location. Please turn on the GPS")
                .setCancelable(false)
                .setTitle("Hi!")
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    @SuppressWarnings("unused") final DialogInterface dialog,
                                    @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                @SuppressWarnings("unused") final int id) {
                                System.exit(0);

                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void noRegisterAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setMessage("Email anda belum terdaftar oleh administrator")
                .setCancelable(false)
                .setTitle("Hi!")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    @SuppressWarnings("unused") final DialogInterface dialog,
                                    @SuppressWarnings("unused") final int id) {
                            }
                        });

        final AlertDialog alert = builder.create();
        alert.show();
    }
}