package com.spil.dev.tms.Activity.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.spil.dev.tms.Activity.Model.KendaraanModel;
import com.spil.dev.tms.Activity.PilihKendaraanActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andresual on 2/20/2018.
 */

public class KendaraanController extends Activity {

    private static KendaraanController mInstance;
    public static KendaraanController getmInstance() {
        if (mInstance == null) {
            mInstance = new KendaraanController();
        }
        return mInstance;
    }

    KendaraanModel kendaraanActive;
    ArrayList<KendaraanModel> kendaraanModelArrayList = new ArrayList<>();

    //GETTER SETTER//
    public KendaraanModel getKendaraanActive() {
        return kendaraanActive;
    }

    public void setKendaraanActive(KendaraanModel kendaraanActive) {
        this.kendaraanActive = kendaraanActive;
    }

    public ArrayList<KendaraanModel> getKendaraanModelArrayList() {
        return kendaraanModelArrayList;
    }

    public void setKendaraanModelArrayList(ArrayList<KendaraanModel> kendaraanModelArrayList) {
        this.kendaraanModelArrayList = kendaraanModelArrayList;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void fetchKendaraan(PilihKendaraanActivity pilihKendaraanActivity) {

        final ProgressDialog progressDialog = new ProgressDialog(pilihKendaraanActivity);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        final HashMap<String, String> params = new HashMap<>();
        params.put("f", "getListKendaraanDriver");
        params.put("iddriver", AuthController.getmInstance().getDriverActive().getIdDriver());
        Log.i("fetchKendaraan", params.toString());

        RequestQueue queue = Volley.newRequestQueue(pilihKendaraanActivity);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://manajemenkendaraan.com/tms/webservice.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray kendaraanArray = obj.getJSONArray("data");
                            kendaraanModelArrayList = new ArrayList<>();
                            for (int i= 0;i < kendaraanArray.length(); i++) {
                                JSONObject hasil = kendaraanArray.getJSONObject(i);
                                Log.i("haha", hasil.toString());
                                KendaraanModel kendaraanModel = new KendaraanModel();
//                                kendaraanModel.setIdDriver(hasil.getString("id_driver"));
                                kendaraanModel.setIdNopol(hasil.getString("nopol"));
                                kendaraanModel.setIdKendaraan(hasil.getString("idkendaraan"));
                                kendaraanModelArrayList.add(kendaraanModel);
                                Log.i("idkendaraann", kendaraanModel.getIdKendaraan());
                            }
                        } catch (Throwable t){
                            Log.i("tms", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        queue.add(sr);
    }
}
