package com.andresual.dev.tms.Activity.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Fragment.AkunFragment;
import com.andresual.dev.tms.Activity.Fragment.BerandaFragment;
import com.andresual.dev.tms.Activity.MainActivity;
import com.andresual.dev.tms.Activity.Model.DriverModel;
import com.andresual.dev.tms.Activity.Model.RegisterModel;
import com.andresual.dev.tms.Activity.Model.ReturnModel;
import com.andresual.dev.tms.Activity.RegisterActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by andresual on 1/30/2018.
 */

public class AuthController {

    Context context;

    private static AuthController mInstance;

    public static AuthController getmInstance() {
        if (mInstance == null) {
            mInstance = new AuthController();
        }
        return mInstance;
    }

    String id;

    public static String emailPass;

    public static String getEmail() {
        return emailPass;
    }

    private DriverModel driverActive;
    private RegisterModel registerModel;

    private ArrayList<DriverModel> driverModelArrayList;

    public ArrayList<DriverModel> getDriverModelArrayList() {
        return driverModelArrayList;
    }

    public void setDriverModelArrayList(ArrayList<DriverModel> driverModelArrayList) {
        this.driverModelArrayList = driverModelArrayList;
    }

    public static void setmInstance(AuthController mInstance) {
        AuthController.mInstance = mInstance;
    }

    public DriverModel getDriverActive() {
        return driverActive;
    }

    public void setDriverActive(DriverModel driverActive) {
        this.driverActive = driverActive;
    }

    public RegisterModel getRegisterModel() {
        return registerModel;
    }

    public void setRegisterModel(RegisterModel registerModel) {
        this.registerModel = registerModel;
    }

    public void signUpApi(final RegisterActivity registerActivity) {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "app_register");
        params.put("email", registerModel.getEmail());
        params.put("nama", registerModel.getName());
        params.put("alamat", registerModel.getAddress());
        params.put("kota", registerModel.getCity());
        params.put("nohp", registerModel.getPhone());
        params.put("nopol", registerModel.getNopol());
        params.put("id", "1");
        params.put("password", registerModel.getPassword());
//        params.put("tokenreg", registerModel.getRegtoken());
        Log.i("signup", params.toString());

        RequestQueue queue = Volley.newRequestQueue(registerActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/byamik.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("tms", obj.getString("status"));
                            Log.i("tms", obj.getString("message"));

                            ReturnModel returnModel = new ReturnModel(obj.getInt("status"), obj.getString("message"), obj);
                            if (returnModel.getStatusCode() == -100 || returnModel.getStatusCode() == -200) {

                            } else {
                                if (returnModel.getStatusCode() == 200) {

                                }
                                registerActivity.handleSignUp(returnModel);
                            }
                        } catch (Throwable t) {
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(sr);
    }

    public void signInApi(final MainActivity mainActivity) {
        final Map<String, String> params = new HashMap<>();
        params.put("f", "loginapp");
        params.put("email", driverActive.getEmail());
        params.put("password", driverActive.getPassword());
        params.put("tokenreg", driverActive.getToken());
        Log.i("signin", params.toString());

        RequestQueue queue = Volley.newRequestQueue(mainActivity);
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
                                String token = obj.getString("token");
                                driverActive.setToken(token);

                                JSONObject data = obj.getJSONObject("data");
                                driverModelArrayList = new ArrayList<>();

                                for (int i = 0; i < data.length(); i++) {
                                    driverActive.setId(data.getInt("id"));
                                    driverActive.setEmail(data.getString("email"));
                                    driverActive.setPassword(data.getString("password"));
                                    driverActive.setAlamat(data.getString("alamat"));
                                    driverActive.setKode(data.getString("kode"));
                                    driverActive.setTelp(data.getString("telp"));
                                    driverActive.setKota(data.getString("kota"));
                                    driverActive.setExpiredToken(data.getString("expiredtoken"));
                                    driverActive.setIdDriver(data.getString("id_driver"));
                                    driverActive.setUsername(data.getString("nama"));
                                    driverModelArrayList.add(driverActive);
                                    id = data.getString("id_driver");
                                }
                            }
                            mainActivity.handleSignIn(returnModel);
                            emailPass = driverActive.getEmail();

                            Log.i("driverss", id);
                            Intent intent = new Intent(context, BerandaFragment.class);
                            intent.putExtra("driverid", id);
                            context.startActivity(intent);

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
    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public void displayMessage(String toastString) {
        Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
    }
}