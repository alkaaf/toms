package com.spil.dev.tms.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.spil.dev.tms.Activity.Controller.AuthController;
import com.spil.dev.tms.Activity.Controller.GoogleController;
import com.spil.dev.tms.Activity.Controller.OperationalController;
import com.spil.dev.tms.Activity.Model.CityModel;
import com.spil.dev.tms.Activity.Model.RegisterModel;
import com.spil.dev.tms.Activity.Model.ReturnModel;
import com.spil.dev.tms.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    Spinner spCity;
    String email = null, username = null;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<CityModel> cityModelArrayList = new ArrayList<>();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mGoogleApiClient = ((GoogleController) getApplication()).getGoogleApiClient(RegisterActivity.this, this);

        fetchCity();

        email = this.getIntent().getStringExtra("email");
        username = this.getIntent().getStringExtra("username");

        final EditText etEmail = findViewById(R.id.til_email);
        final EditText etPassword = findViewById(R.id.til_password);
        final EditText etConfirmPassword = findViewById(R.id.til_confirm_password);
        final EditText etFullName = findViewById(R.id.til_fullname);
        final EditText etCompanyName = findViewById(R.id.til_company_name);
        final EditText etAddress = findViewById(R.id.til_address);
        final EditText etNopol = findViewById(R.id.til_nopol);
        spCity = findViewById(R.id.sp_city);
        final EditText etPhone = findViewById(R.id.til_phone);
        Button btnSignUp = findViewById(R.id.btn_save);
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        etEmail.setText(email);
        etFullName.setText(username);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Insert name", Toast.LENGTH_SHORT).show();
                } else if (etCompanyName.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Insert email", Toast.LENGTH_SHORT).show();
                } else if (etFullName.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Insert phone number", Toast.LENGTH_SHORT).show();
                } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                } else if (etConfirmPassword.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                } else if (etAddress.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Insert address", Toast.LENGTH_SHORT).show();
                } else if (etPhone.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Insert phone", Toast.LENGTH_SHORT).show();
                }

                else {

                    RegisterModel driverRegister = new RegisterModel();
                    driverRegister.setEmail(etEmail.getText().toString());
                    driverRegister.setCompany(etCompanyName.getText().toString());
                    driverRegister.setName(etFullName.getText().toString());
                    driverRegister.setPassword(etPassword.getText().toString());
                    driverRegister.setPassword(etConfirmPassword.getText().toString());
                    driverRegister.setAddress(etAddress.getText().toString());
                    driverRegister.setPhone(etPhone.getText().toString());
                    driverRegister.setCity(spCity.getSelectedItem().toString());
                    driverRegister.setNopol(etNopol.getText().toString());
//                    driverRegister.setRegtoken(msg);

                    AuthController.getmInstance().setRegisterModel(driverRegister);
                    AuthController.getmInstance().signUpApi(RegisterActivity.this);
                }
            }
        });

//        OperationalController.getmInstance().fetchCity(this);
    }

    public void handleSignUp(ReturnModel returnModel) {
        Log.e("handle", "status code = " + returnModel.getStatusCode());
        Log.e("handle", "message = " + returnModel.getMessage());

        if (returnModel.getStatusCode() == 200) {
            Intent intent = new Intent(this, RegisterConfirmationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, returnModel.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    private class fetchCity extends AsyncTask<Void, Void, Void> {
//
//        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            this.progressDialog.setMessage("Loading data...");
//            this.progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                OperationalController.getmInstance().fetchCity(RegisterActivity.this);
//                Thread.sleep(3000);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//            spCity.setAdapter(new ArrayAdapter<CityModel>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, OperationalController.getmInstance().getCityModelArrayList()));
//            super.onPostExecute(result);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent login = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }

    public void fetchCity() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        final Map<String, String> params = new HashMap<>();
        params.put("f", "getcity");
        Log.i("city", params.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, "http://manajemenkendaraan.com/tms/webservice.asp?f=getcity",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray city = obj.getJSONArray("city");
                            cityModelArrayList = new ArrayList<>();
                            for (int i = 0; i < city.length(); i++) {
                                JSONObject hasil = city.getJSONObject(i);
                                CityModel cityModel = new CityModel();
                                cityModel.setId(hasil.getString("id"));
                                cityModel.setNamaKota(hasil.getString("nama"));
                                cityModelArrayList.add(cityModel);

                                spCity.setAdapter(new ArrayAdapter<CityModel>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, cityModelArrayList));
                                spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String city = spCity.getSelectedItem().toString();
                                        Log.i("kota terpilih", city);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                progressDialog.dismiss();
                            }
                        } catch (Throwable t) {
                            Log.i("urbankabinet", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }
}
